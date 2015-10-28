package org.lliira.bookworm.core.persist.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookAuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;

public class AuthorMapperTest extends AbstractTest {

    private AuthorMapper authorMapper;

    @BeforeMethod
    public void prepareMapper() {
        this.authorMapper = BookwormHelper.get(AuthorMapper.class);
    }

    @Test
    public void testInsert() {
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        AuthorEntity author = new AuthorEntity();
        author.setName(name);
        author.setDescription(description);

        // test insert
        final int count = authorMapper.insert(author);
        Assert.assertEquals(count, 1);
        compare(author, null, name, description);

        final Integer id = author.getId();
        Assert.assertNotNull(id);

        // test get to make sure insert succeeded.
        author = authorMapper.select(id);
        compare(author, id, name, description);

        // test insert with null description
        final String name2 = "name-" + random.nextInt();
        final AuthorEntity author2 = new AuthorEntity();
        author2.setName(name2);
        Assert.assertEquals(this.authorMapper.insert(author2), 1);
        Assert.assertNotNull(author2.getId());
        Assert.assertNull(author2.getDescription());
        final AuthorEntity author3 = this.authorMapper.select(author2.getId());
        compare(author3, author2);
    }

    @Test
    public void testUpdate() {
        AuthorEntity author = PersistTestHelper.createAuthor();
        final Integer id = author.getId();
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        author.setName(name);
        author.setDescription(description);

        final int count = authorMapper.update(author);
        Assert.assertEquals(count, 1);
        compare(author, id, name, description);

        author = authorMapper.select(id);
        compare(author, id, name, description);

        // test update author with null description
        author.setDescription(null);
        Assert.assertEquals(this.authorMapper.update(author), 1);
        Assert.assertNull(author.getDescription());
        final AuthorEntity author2 = this.authorMapper.select(author.getId());
        compare(author2, author);
    }

    @Test
    public void testDelete() {
        AuthorEntity author = PersistTestHelper.createAuthor();
        final Integer id = author.getId();
        final int count = authorMapper.delete(author);
        Assert.assertEquals(count, 1);
        author = authorMapper.select(id);
        Assert.assertNull(author);
    }

    @Test
    public void testSelectByBook() {
        final AuthorEntity author1 = PersistTestHelper.createAuthor();
        final AuthorEntity author2 = PersistTestHelper.createAuthor();
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();

        final BookAuthorMapper bookAuthorMapper = BookwormHelper.get(BookAuthorMapper.class);

        final BookAuthorEntity book1Author1 = new BookAuthorEntity(book1.getId(), author1.getId());
        final BookAuthorEntity book1Author2 = new BookAuthorEntity(book1.getId(), author2.getId());
        final BookAuthorEntity book2Author2 = new BookAuthorEntity(book2.getId(), author2.getId());
        bookAuthorMapper.insert(book1Author1);
        bookAuthorMapper.insert(book1Author2);
        bookAuthorMapper.insert(book2Author2);

        final List<AuthorEntity> authors = authorMapper.selectByBook(book1);
        compare(authors, author1, author2);
    }

    @Test
    public void testSelectByName() {
        // create some default test author
        final AuthorEntity author1 = PersistTestHelper.createAuthor();
        final AuthorEntity author2 = PersistTestHelper.createAuthor();
        List<AuthorEntity> authors = authorMapper.selectByName("%author%");
        compare(authors, author1, author2);

        // modify an author, and search for it
        author1.setName("a new test author");
        authorMapper.update(author1);

        authors = authorMapper.selectByName("%test%");
        compare(authors, author1);
    }

    private void compare(final Collection<AuthorEntity> actual, final AuthorEntity... expected) {
        Assert.assertEquals(actual.size(), expected.length);
        // create a map to store expected authors
        final Map<Integer, AuthorEntity> map = new HashMap<Integer, AuthorEntity>(expected.length);
        for (final AuthorEntity author : expected) {
            map.put(author.getId(), author);
        }
        for (final AuthorEntity author : actual) {
            final AuthorEntity expectedAuthor = map.get(author.getId());
            if (expectedAuthor != null) compare(author, expectedAuthor);
            else Assert.assertTrue(false, "unexpected author");
        }
    }

    private void compare(final AuthorEntity actual, final AuthorEntity expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
    }

    private void compare(final AuthorEntity actual, final Integer id, final String name,
            final String description) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getDescription(), description);
        if (id != null) Assert.assertEquals(actual.getId(), id);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testInsertNullName() {
        final AuthorEntity author = new AuthorEntity();
        author.setDescription("desc-" + random.nextInt());
        this.authorMapper.insert(author);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testUpdateNullName() {
        final AuthorEntity author = PersistTestHelper.createAuthor();
        author.setName(null);
        this.authorMapper.update(author);
    }
}
