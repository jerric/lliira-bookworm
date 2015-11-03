package org.lliira.bookworm.core.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.AuthorException;
import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.service.BookService;

import static net.lliira.bookworm.core.BookwormHelper.PRESICION;

public class BookServiceTest extends AbstractTest {

    private BookService bookService;

    @BeforeMethod
    public void prepareService() {
        this.bookService = BookwormHelper.get(BookService.class);
    }

    @Test
    public void testCreate() throws BookException {
        final String name = "name-" + random.nextInt();
        final String sortedName = "sorted-name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final Date publishDate = new Date();

        final Book book = this.bookService.create(name, sortedName, publishDate, description);
        Assert.assertTrue(book.getId() > 0);
        compare(book, name, sortedName, publishDate, description);

        final Book book2 = this.bookService.get(book.getId());
        compare(book2, book);
    }

    @Test
    public void testUpdate() throws BookException {
        final Book book = TestHelper.createBook();
        final String name = "name-" + random.nextInt();
        final String sortedName = "sorted-name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final Date publishDate = new Date();
        book.setName(name);
        book.setSortedName(sortedName);
        book.setPublishDate(publishDate);
        book.setDescription(description);

        this.bookService.update(book);
        compare(book, name, sortedName, publishDate, description);

        final Book book2 = this.bookService.get(book.getId());
        compare(book2, book);
    }

    @Test
    public void testDeleteWithAuthors() throws BookException, AuthorException {
        final Book book = TestHelper.createBook();
        final Author[] authors = TestHelper.createAuthors(2);
        this.bookService.setAuthors(book, Arrays.asList(authors));

        this.bookService.delete(book);

        Assert.assertNull(this.bookService.get(book.getId()));

        final List<Book> books = this.bookService.get(authors[0]);
        for (final Book b : books) {
            Assert.assertNotEquals(b.getId(), book.getId());
        }
    }

    @Test
    public void testDeleteWithCategories() throws BookException, CategoryException {
        final Book book = TestHelper.createBook();
        final Category[] categories = TestHelper.createCategories(null, 2);
        final Map<Category, Float> map = new HashMap<>(categories.length);
        for (final Category category : categories) {
            float siblingIndex = (float) random.nextInt(PRESICION * 100) / PRESICION;
            map.put(category, siblingIndex);
        }
        this.bookService.setCategories(book, map);

        this.bookService.delete(book);

        Assert.assertNull(this.bookService.get(book.getId()));

        final Map<Float, Book> books = this.bookService.get(categories[0]);
        for (final Book b : books.values()) {
            Assert.assertNotEquals(b.getId(), book.getId());
        }
    }

    @Test
    public void testSetAuthorsWithInsert() throws BookException, AuthorException {
        final Book[] books = TestHelper.createBooks(2);
        final Author[] authors = TestHelper.createAuthors(2);
        this.bookService.setAuthors(books[0], Arrays.asList(authors));
        this.bookService.setAuthors(books[1], Arrays.asList(authors[1]));

        final List<Book> list0 = this.bookService.get(authors[0]);
        Assert.assertEquals(list0.size(), 1);
        compare(list0.get(0), books[0]);

        final List<Book> list1 = this.bookService.get(authors[1]);
        Assert.assertEquals(list1.size(), 2);
        if (list1.get(0).getId() == books[0].getId()) {
            compare(list1.get(0), books[0]);
            compare(list1.get(1), books[1]);
        } else {
            compare(list1.get(0), books[1]);
            compare(list1.get(1), books[0]);
        }
    }

    @Test
    public void testSetAuthorsWithDelete() {
        Assert.assertTrue(false);
    }

    @Test
    public void testSetCategoriesWithInsert() {
        Assert.assertTrue(false);
    }

    @Test
    public void testSetCategoriesWithDelete() {
        Assert.assertTrue(false);
    }

    @Test
    public void testSetCategoriesWithUpdate() {
        Assert.assertTrue(false);
    }

    @Test
    public void testSetCategoriesWithFullNormalize() {
        Assert.assertTrue(false);
    }

    @Test
    public void testSetCategoriesWithPartialNormalize() {
        Assert.assertTrue(false);
    }

    private void compare(final Book actual, final String name, final String sortedName,
            final Date publishDate, final String description) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getSortedName(), sortedName);
        Assert.assertEquals(actual.getDescription(), description);

        // compare only the date portion
        final Calendar actualCal = Calendar.getInstance();
        actualCal.setTime(actual.getPublishDate());
        final Calendar expectedCal = Calendar.getInstance();
        expectedCal.setTime(publishDate);
        Assert.assertEquals(actualCal.get(Calendar.YEAR), expectedCal.get(Calendar.YEAR));
        Assert.assertEquals(actualCal.get(Calendar.MONTH), expectedCal.get(Calendar.MONDAY));
        Assert.assertEquals(actualCal.get(Calendar.DAY_OF_MONTH), expectedCal.get(Calendar.DAY_OF_MONTH));
    }

    private void compare(final Book actual, final Book expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getSortedName(), expected.getSortedName());
        Assert.assertEquals(actual.getPublishDate(), expected.getPublishDate());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
    }
}
