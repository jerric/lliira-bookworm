package org.lliira.bookworm.core.service;

import java.util.Collections;
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
    public void testUpdate() {
        Assert.assertTrue(false);
    }

    @Test
    public void testDeleteWithReference() throws AuthorException, BookException {
        final Author author = TestHelper.createAuthor();
        final Book book1 = TestHelper.createBook();
        final Book book2 = TestHelper.createBook();

        this.authorService.setAuthorsToBook(book1, Collections.singletonList(author));
        this.authorService.setAuthorsToBook(book2, Collections.singletonList(author));

        this.authorService.delete(author);

        Assert.assertNull(this.authorService.get(author.getId()));
    }

    @Test
    public void testSetBookAuthorWithInsert() throws AuthorException, BookException {
        final Author author1 = TestHelper.createAuthor();
        final Author author2 = TestHelper.createAuthor();
        final Book book1 = TestHelper.createBook();
        final Book book2 = TestHelper.createBook();

        this.authorService.setAuthorsToBook(book1, Collections.singletonList(author1));
        this.authorService.setAuthorsToBook(book2, Collections.singletonList(author2));

        final Set<Author> authors = this.authorService.get(book1);
        Assert.assertEquals(authors.size(), 1);
        compare(authors.iterator().next(), author1);
    }

    @Test
    public void testSetBookAuthorWithDelete() throws AuthorException, BookException {
        Assert.assertTrue(false);
    }
    
    private void compare(final Author actual, final Author expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
    }
}
