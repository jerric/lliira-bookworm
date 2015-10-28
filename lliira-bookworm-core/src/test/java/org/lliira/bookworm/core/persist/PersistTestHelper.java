package org.lliira.bookworm.core.persist;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.lliira.bookworm.core.TestHelper;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public class PersistTestHelper {

    public static AuthorEntity createAuthor() {
        final Random random = TestHelper.getRandom();
        final AuthorEntity author = new AuthorEntity();
        author.setName("author-name-" + random.nextInt());
        author.setDescription("author-desc-" + random.nextInt());

        final AuthorMapper authorMapper = BookwormHelper.get(AuthorMapper.class);
        authorMapper.insert(author);

        return author;
    }

    public static BookEntity createBook() {
        final Random random = TestHelper.getRandom();
        final BookEntity book = new BookEntity();
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

    public static CategoryEntity createCategory(final CategoryEntity parent) {
        final Random random = TestHelper.getRandom();
        final CategoryMapper categoryMapper = BookwormHelper.get(CategoryMapper.class);

        final List<CategoryEntity> siblings = (parent == null) ? categoryMapper.selectRoots()
                : categoryMapper.selectByParent(parent);
        float siblingIndex = 0;
        for (final CategoryEntity sibling : siblings) {
            if (sibling.getSiblingIndex() >= siblingIndex) siblingIndex = sibling.getSiblingIndex();
        }
        siblingIndex = (float) Math.floor(siblingIndex) + 1;

        final CategoryEntity category = new CategoryEntity();
        category.setName("category-name-" + random.nextInt());
        category.setSiblingIndex(siblingIndex);
        category.setDescription("category-desc-" + random.nextInt());
        if (parent != null) category.setParentId(parent.getId());

        categoryMapper.insert(category);

        return category;
    }
}
