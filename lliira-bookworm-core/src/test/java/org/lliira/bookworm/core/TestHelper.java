package org.lliira.bookworm.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import net.lliira.bookworm.core.AuthorException;
import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.service.AuthorService;
import net.lliira.bookworm.core.service.BookService;
import net.lliira.bookworm.core.service.CategoryService;

public class TestHelper {

    private static Random random;

    public static Random getRandom() {
        if (random == null) {
            synchronized (Random.class) {
                if (random == null) random = new Random();
            }
        }
        return random;
    }

    public static Author createAuthor() throws AuthorException {
        final Random random = getRandom();
        final String name = "book-name-" + random.nextInt();
        final String description = "book-desc-" + random.nextInt();

        final AuthorService authorService = BookwormHelper.get(AuthorService.class);
        return authorService.create(name, description);
    }

    public static Author[] createAuthors(final int count) throws AuthorException {
        final Author[] authors = new Author[count];
        for (int i = 0; i < count; i++) {
            authors[i] = createAuthor();
        }
        return authors;
    }

    public static Book createBook() throws BookException {
        final Random random = getRandom();
        final String name = "book-name-" + random.nextInt();
        final String sortedName = "sorted-name-" + random.nextInt();
        final String description = "book-desc-" + random.nextInt();

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(1000));
        final Date publishDate = calendar.getTime();
        final BookService bookService = BookwormHelper.get(BookService.class);
        return bookService.create(name, sortedName, publishDate, description);
    }

    public static Book[] createBooks(final int count) throws BookException {
        final Book[] books = new Book[count];
        for (int i = 0; i < count; i++) {
            books[i] = createBook();
        }
        return books;
    }

    public static Category createCategory(final Category parent) throws CategoryException {
        final Random random = TestHelper.getRandom();
        final CategoryService categoryService = BookwormHelper.get(CategoryService.class);
        final String name = "category-name-" + random.nextInt();
        final String description = "category-desc-" + random.nextInt();
        return categoryService.create(name, parent, description);
    }

    public static Category[] createCategories(final Category parent, final int count)
            throws CategoryException {
        final Category[] categories = new Category[count];
        for (int i = 0; i < count; i++) {
            categories[i] = createCategory(parent);
        }
        return categories;
    }
}
