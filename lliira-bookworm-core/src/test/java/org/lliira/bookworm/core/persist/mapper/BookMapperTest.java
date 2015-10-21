package org.lliira.bookworm.core.persist.mapper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookAuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;

public class BookMapperTest extends AbstractTest {

    private BookMapper bookMapper;

    @BeforeMethod
    public void prepareMapper() {
        bookMapper = BookwormHelper.get(BookMapper.class);
    }

    @Test
    public void testInsert() {
        final String name = "name-" + random.nextInt();
        final String sortedName = "sorted-name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final Date publishDate = new Date();
        BookEntity book = new BookEntity();
        book.setName(name);
        book.setSortedName(sortedName);
        book.setDescription(description);
        book.setPublishDate(publishDate);

        // test insert
        final int count = bookMapper.insert(book);
        Assert.assertEquals(count, 1);
        compare(book, null, name, sortedName, description, publishDate);

        final Integer id = book.getId();
        Assert.assertNotNull(id);

        // test get to make sure insert succeeded.
        book = bookMapper.select(id);
        compare(book, id, name, sortedName, description, publishDate);
    }

    @Test
    public void testUpdate() {
        BookEntity book = PersistTestHelper.createBook();
        final Integer id = book.getId();
        final String name = "name-" + random.nextInt();
        final String sortedName = "sorted-name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final Date publishDate = new Date();
        book.setName(name);
        book.setSortedName(sortedName);
        book.setDescription(description);
        book.setPublishDate(publishDate);

        final int count = bookMapper.update(book);
        Assert.assertEquals(count, 1);
        compare(book, id, name, sortedName, description, publishDate);

        book = bookMapper.select(id);
        compare(book, id, name, sortedName, description, publishDate);
    }

    @Test
    public void testDelete() {
        BookEntity book = PersistTestHelper.createBook();
        final Integer id = book.getId();
        final int count = bookMapper.delete(book);
        Assert.assertEquals(count, 1);
        book = bookMapper.select(id);
        Assert.assertNull(book);
    }

    @Test
    public void testSelectByAuthor() {
        final AuthorEntity author1 = PersistTestHelper.createAuthor();
        final AuthorEntity author2 = PersistTestHelper.createAuthor();
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();

        final BookAuthorMapper bookAuthorMapper = BookwormHelper.getContext().getBean(BookAuthorMapper.class);

        final BookAuthorEntity book1Author1 = new BookAuthorEntity(book1.getId(), author1.getId());
        final BookAuthorEntity book1Author2 = new BookAuthorEntity(book1.getId(), author2.getId());
        final BookAuthorEntity book2Author2 = new BookAuthorEntity(book2.getId(), author2.getId());
        bookAuthorMapper.insert(book1Author1);
        bookAuthorMapper.insert(book1Author2);
        bookAuthorMapper.insert(book2Author2);

        final List<BookEntity> books = bookMapper.selectByAuthor(author2);
        compare(books, book1, book2);
    }

    @Test
    public void testSelectByName() {
        // create some default test author
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();
        List<BookEntity> authors = bookMapper.selectByName("%book%");
        compare(authors, book1, book2);

        // modify an author, and search for it
        book1.setName("a new test author");
        bookMapper.update(book1);

        authors = bookMapper.selectByName("%test%");
        compare(authors, book1);
    }

    private void compare(Collection<BookEntity> actual, BookEntity... expected) {
        Assert.assertEquals(actual.size(), expected.length);
        // create a map to store expected authors
        final Map<Integer, BookEntity> map = new HashMap<>(expected.length);
        for (final BookEntity book : expected) {
            map.put(book.getId(), book);
        }
        for (final BookEntity book : actual) {
            final BookEntity expectedBook = map.get(book.getId());
            if (expectedBook != null) compare(book, expectedBook);
            else Assert.assertTrue(false, "unexpected book");
        }
    }

    private void compare(BookEntity actual, BookEntity expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getSortedName(), expected.getSortedName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
        compareDate(actual.getPublishDate(), expected.getPublishDate());
    }

    private void compare(final BookEntity actual, final Integer id, final String name,
            final String sortedName, final String description, final Date publishDate) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getSortedName(), sortedName);
        Assert.assertEquals(actual.getDescription(), description);
        compareDate(actual.getPublishDate(), publishDate);

        if (id != null) Assert.assertEquals(actual.getId(), id);
    }

    private void compareDate(final Date actual, final Date expected) {
        // only compare the date portion of the data.
        final Calendar actualDate = Calendar.getInstance();
        actualDate.setTime(actual);
        final Calendar expectedDate = Calendar.getInstance();
        expectedDate.setTime(expected);
        Assert.assertEquals(actualDate.get(Calendar.YEAR), expectedDate.get(Calendar.YEAR));
        Assert.assertEquals(actualDate.get(Calendar.MONTH), expectedDate.get(Calendar.MONTH));
        Assert.assertEquals(actualDate.get(Calendar.DAY_OF_MONTH), expectedDate.get(Calendar.DAY_OF_MONTH));
    }
}
