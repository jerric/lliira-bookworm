package org.lliira.bookworm.core.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
    public void testSetAuthorsWithDelete() throws BookException, AuthorException {
        final Book book = TestHelper.createBook();
        final Author[] authors = TestHelper.createAuthors(3);
        this.bookService.setAuthors(book, Arrays.asList(authors[0], authors[1]));

        // then set another group of authors to cause a deletion
        this.bookService.setAuthors(book, Arrays.asList(authors[1], authors[2]));
        Assert.assertEquals(this.bookService.get(authors[0]).size(), 0);

        final List<Book> list1 = this.bookService.get(authors[1]);
        Assert.assertEquals(list1.size(), 1);
        compare(list1.get(0), book);

        final List<Book> list2 = this.bookService.get(authors[2]);
        Assert.assertEquals(list2.size(), 1);
        compare(list2.get(0), book);
    }

    @Test
    public void testSetCategoriesWithInsert() throws BookException, CategoryException {
        final Book[] books = TestHelper.createBooks(2);
        final Category[] categories = TestHelper.createCategories(null, 2);
        final Map<Category, Float> map0 = toMap(1F, categories);
        final Map<Category, Float> map1 = toMap(2F, categories[0]);
        this.bookService.setCategories(books[0], map0);
        this.bookService.setCategories(books[1], map1);

        final Map<Float, Book> list0 = this.bookService.get(categories[0]);
        Assert.assertEquals(list0.size(), 2);
        compare(list0.get(1F), books[0]);
        compare(list0.get(2F), books[1]);

        final Map<Float, Book> list1 = this.bookService.get(categories[1]);
        Assert.assertEquals(list1.size(), 1);
        compare(list1.get(2F), books[0]);
    }

    @Test
    public void testSetCategoriesWithDeleteUpdate() throws BookException, CategoryException {
        final Book book = TestHelper.createBook();
        final Category[] categories = TestHelper.createCategories(null, 3);
        final Map<Category, Float> map1 = toMap(1, categories[0], categories[1]);
        this.bookService.setCategories(book, map1);

        final Map<Category, Float> map2 = toMap(1, categories[1], categories[2]);
        this.bookService.setCategories(book, map2);

        // deleted
        final Map<Float, Book> list0 = this.bookService.get(categories[0]);
        Assert.assertEquals(list0.size(), 0);

        // updated from 2F to 1F
        final Map<Float, Book> list1 = this.bookService.get(categories[1]);
        Assert.assertEquals(list1.size(), 1);
        compare(list1.get(1F), book);

        // inserted
        final Map<Float, Book> list2 = this.bookService.get(categories[2]);
        Assert.assertEquals(list2.size(), 1);
        compare(list2.get(2F), book);
    }

    @Test
    public void testSetCategoriesWithPartialNormalize() throws BookException, CategoryException {
        final Book[] books = TestHelper.createBooks(4);
        final Category category = TestHelper.createCategory(null);
        this.bookService.setCategories(books[0], Collections.singletonMap(category, 1F));
        this.bookService.setCategories(books[2], Collections.singletonMap(category, 2F));
        this.bookService.setCategories(books[3], Collections.singletonMap(category, 4F));

        this.bookService.setCategories(books[1], Collections.singletonMap(category, 2F));

        final Map<Float, Book> map = this.bookService.get(category);
        Assert.assertEquals(map.size(), 4);
        compare(map.get(1F), books[0]);
        compare(map.get(2F), books[1]);
        compare(map.get(3F), books[2]);
        compare(map.get(4F), books[3]);
    }

    @Test
    public void testSetCategoriesWithFullNormalize() throws BookException, CategoryException {
        final Book[] books = TestHelper.createBooks(4);
        final Category category = TestHelper.createCategory(null);
        this.bookService.setCategories(books[1], Collections.singletonMap(category, 1F));
        this.bookService.setCategories(books[2], Collections.singletonMap(category, 2F));
        this.bookService.setCategories(books[3], Collections.singletonMap(category, 3F));

        this.bookService.setCategories(books[0], Collections.singletonMap(category, 1F));

        final Map<Float, Book> map = this.bookService.get(category);
        Assert.assertEquals(map.size(), 4);
        compare(map.get(1F), books[0]);
        compare(map.get(2F), books[1]);
        compare(map.get(3F), books[2]);
        compare(map.get(4F), books[3]);
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

    private Map<Category, Float> toMap(float start, final Category... categories) {
        final Map<Category, Float> map = new HashMap<>(categories.length);
        for (final Category category : categories) {
            map.put(category, start++);
        }
        return map;
    }
}
