package org.lliira.bookworm.core.persist;

import java.util.Calendar;
import java.util.Random;

import org.lliira.bookworm.core.TestHelper;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public class PersistTestHelper {

    public static AuthorData createAuthor() {
        final Random random = TestHelper.getRandom();
        final AuthorData author = new AuthorData();
        author.setName("author-name-" + random.nextInt());
        author.setDescription("author-desc-" + random.nextInt());

        final AuthorMapper authorMapper = BookwormHelper.get(AuthorMapper.class);
        authorMapper.insert(author);

        return author;
    }

    public static BookData createBook() {
        final Random random = TestHelper.getRandom();
        final BookData book = new BookData();
        book.setName("book-name-" + random.nextInt());
        book.setSortedName("sorted-name-" + random.nextInt());
        book.setDescription("book-desc-" + random.nextInt());

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(1000));
        book.setPublishDate(calendar.getTime());

        final BookMapper bookMapper = BookwormHelper.get(BookMapper.class);
        bookMapper.insert(book);

        return book;
    }

    public static CategoryData createCategory(final CategoryData parent) {
        final Random random = TestHelper.getRandom();
        final CategoryMapper categoryMapper = BookwormHelper.get(CategoryMapper.class);

        final float siblingIndex;
        if (parent == null) siblingIndex = categoryMapper.selectMaxRootIndex() + 1;
        else siblingIndex = categoryMapper.selectMaxSiblingIndex(parent) + 1;

        final CategoryData category = new CategoryData();
        category.setName("category-name-" + random.nextInt());
        category.setSiblingIndex(siblingIndex);
        category.setDescription("category-desc-" + random.nextInt());
        if (parent != null) category.setParentId(parent.getId());

        categoryMapper.insert(category);

        return category;
    }
}
