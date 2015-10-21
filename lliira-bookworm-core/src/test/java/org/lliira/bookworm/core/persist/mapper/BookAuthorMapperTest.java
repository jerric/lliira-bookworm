package org.lliira.bookworm.core.persist.mapper;

import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookAuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;

public class BookAuthorMapperTest extends AbstractTest {

    private BookAuthorMapper bookAuthorMapper;

    @BeforeMethod
    public void prepareMapper() {
        bookAuthorMapper = BookwormHelper.get(BookAuthorMapper.class);
    }

    @Test
    public void testInsert() {
        final AuthorEntity author = PersistTestHelper.createAuthor();
        final BookEntity book = PersistTestHelper.createBook();
        final BookAuthorEntity bookAuthor = new BookAuthorEntity(book.getId(), author.getId());
        final int count = bookAuthorMapper.insert(bookAuthor);
        Assert.assertEquals(count, 1);
    }

    @Test
    public void testDelete() {
        final AuthorEntity author = PersistTestHelper.createAuthor();
        final BookEntity book = PersistTestHelper.createBook();
        final BookAuthorEntity bookAuthor = new BookAuthorEntity(book.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor);
        final int count = bookAuthorMapper.delete(bookAuthor);
        Assert.assertEquals(count, 1);
    }

    @Test
    public void testDeleteByAuthor() {
        final AuthorEntity author = PersistTestHelper.createAuthor();
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookAuthorEntity bookAuthor1 = new BookAuthorEntity(book1.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor1);
        final BookEntity book2 = PersistTestHelper.createBook();
        final BookAuthorEntity bookAuthor2 = new BookAuthorEntity(book2.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor2);
        final int count = bookAuthorMapper.deleteByAuthor(author);
        Assert.assertEquals(count, 2);
    }

    @Test
    public void testDeleteByBook() {
        final AuthorEntity author1 = PersistTestHelper.createAuthor();
        final BookEntity book = PersistTestHelper.createBook();
        final BookAuthorEntity bookAuthor1 = new BookAuthorEntity(book.getId(), author1.getId());
        bookAuthorMapper.insert(bookAuthor1);
        final AuthorEntity author2 = PersistTestHelper.createAuthor();
        final BookAuthorEntity bookAuthor2 = new BookAuthorEntity(book.getId(), author2.getId());
        bookAuthorMapper.insert(bookAuthor2);
        final int count = bookAuthorMapper.deleteByBook(book);
        Assert.assertEquals(count, 2);
    }
}
