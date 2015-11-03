package org.lliira.bookworm.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.AuthorException;
import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.service.AuthorService;

public class AuthorServiceTest extends AbstractTest {

    private AuthorService authorService;

    @BeforeMethod
    public void prepareService() {
        this.authorService = BookwormHelper.get(AuthorService.class);
    }

    @Test
    public void testCreate() throws AuthorException {
        final String name = "author-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final Author author = this.authorService.create(name, description);
        Assert.assertTrue(author.getId() > 0);
        Assert.assertEquals(author.getName(), name);
        Assert.assertEquals(author.getDescription(), description);

        // also make sure the author is properly saved
        final Author author2 = this.authorService.get(author.getId());
        compare(author2, author);
    }

    @Test
    public void testUpdate() throws AuthorException {
        final Author author = TestHelper.createAuthor();

        final String name = "author-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        author.setName(name);
        author.setDescription(description);

        this.authorService.update(author);
        compare(author, name, description);

        final Author author2 = this.authorService.get(author.getId());
        compare(author2, author);
    }

    @Test
    public void testDeleteWithReference() throws AuthorException, BookException {
        final Author author = TestHelper.createAuthor();
        final Book[] books = TestHelper.createBooks(2);

        this.authorService.setAuthorsToBook(books[0], Collections.singletonList(author));
        this.authorService.setAuthorsToBook(books[1], Collections.singletonList(author));

        this.authorService.delete(author);

        Assert.assertNull(this.authorService.get(author.getId()));
    }

    @Test
    public void testSetBookAuthorWithInsert() throws AuthorException, BookException {
        final Author[] authors = TestHelper.createAuthors(2);
        final Book[] books = TestHelper.createBooks(2);

        this.authorService.setAuthorsToBook(books[0], Collections.singletonList(authors[0]));
        this.authorService.setAuthorsToBook(books[1], Collections.singletonList(authors[1]));

        final Set<Author> set = this.authorService.get(books[0]);
        Assert.assertEquals(set.size(), 1);
        compare(set.iterator().next(), authors[0]);
    }

    @Test
    public void testSetBookAuthorWithDelete() throws AuthorException, BookException {
        final Book[] books = TestHelper.createBooks(2);
        final Author[] authors = TestHelper.createAuthors(3);
        this.authorService.setAuthorsToBook(books[0], Arrays.asList(authors[0], authors[1]));
        this.authorService.setAuthorsToBook(books[1], Arrays.asList(authors[0]));

        // update with delete
        this.authorService.setAuthorsToBook(books[0], Arrays.asList(authors[1], authors[2]));

        // check authors of book0
        final List<Author> list1 = new ArrayList<>(this.authorService.get(books[0]));
        Assert.assertEquals(list1.size(), 2);
        if (list1.get(0).getId() == authors[1].getId()) {
            compare(list1.get(0), authors[1]);
            compare(list1.get(1), authors[2]);
        } else {
            compare(list1.get(1), authors[1]);
            compare(list1.get(0), authors[2]);
        }

        // check authors of book1
        final Set<Author> set1 = this.authorService.get(books[1]);
        Assert.assertEquals(set1.size(), 1);
        compare(set1.iterator().next(), authors[0]);
    }

    private void compare(final Author actual, final String name, final String description) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getDescription(), description);
    }

    private void compare(final Author actual, final Author expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
    }
}
