package org.lliira.bookworm.core.persist.mapper;

import java.util.Arrays;
import java.util.List;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookAuthorData;
import net.lliira.bookworm.core.persist.model.BookData;

public class BookAuthorMapperTest extends AbstractTest {

    private BookAuthorMapper bookAuthorMapper;

    @BeforeMethod
    public void prepareMapper() {
        this.bookAuthorMapper = BookwormHelper.get(BookAuthorMapper.class);
    }

    @Test
    public void testSelectByAuthors() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookData book1 = PersistTestHelper.createBook();
        final BookData book2 = PersistTestHelper.createBook();
        final BookAuthorData book1Author = new BookAuthorData(book1.getId(), author.getId());
        final BookAuthorData book2Author = new BookAuthorData(book2.getId(), author.getId());

        Assert.assertEquals(this.bookAuthorMapper.insert(book1Author), 1);
        Assert.assertEquals(this.bookAuthorMapper.insert(book2Author), 1);

        final List<BookAuthorData> bookAuthors = this.bookAuthorMapper.selectByAuthor(author);
        Assert.assertEquals(bookAuthors.size(), 2);
        Assert.assertEquals(bookAuthors.get(0).getAuthorId(), author.getId());
        Assert.assertEquals(bookAuthors.get(1).getAuthorId(), author.getId());
        if (bookAuthors.get(0).getBookId() == book1.getId()) {
            Assert.assertEquals(bookAuthors.get(1).getBookId(), book2.getId());
        } else {
            Assert.assertEquals(bookAuthors.get(0).getBookId(), book2.getId());
            Assert.assertEquals(bookAuthors.get(1).getBookId(), book1.getId());
        }
    }

    @Test
    public void testSelectByBooks() {
        final BookData book = PersistTestHelper.createBook();
        final AuthorData author1 = PersistTestHelper.createAuthor();
        final AuthorData author2 = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor1 = new BookAuthorData(book.getId(), author1.getId());
        final BookAuthorData bookAuthor2 = new BookAuthorData(book.getId(), author2.getId());

        Assert.assertEquals(this.bookAuthorMapper.insert(bookAuthor1), 1);
        Assert.assertEquals(this.bookAuthorMapper.insert(bookAuthor2), 1);

        final List<BookAuthorData> bookAuthors = this.bookAuthorMapper.selectByBook(book);
        Assert.assertEquals(bookAuthors.size(), 2);
        Assert.assertEquals(bookAuthors.get(0).getBookId(), book.getId());
        Assert.assertEquals(bookAuthors.get(1).getBookId(), book.getId());
        if (bookAuthors.get(0).getAuthorId() == author1.getId()) {
            Assert.assertEquals(bookAuthors.get(1).getAuthorId(), author2.getId());
        } else {
            Assert.assertEquals(bookAuthors.get(0).getAuthorId(), author2.getId());
            Assert.assertEquals(bookAuthors.get(1).getAuthorId(), author1.getId());
        }

    }

    @Test
    public void testInsert() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookData book = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), author.getId());
        final int count = this.bookAuthorMapper.insert(bookAuthor);
        Assert.assertEquals(count, 1);
        Assert.assertNotNull(bookAuthor.getId());

        final BookAuthorData bookAuthor2 = this.bookAuthorMapper.select(bookAuthor.getId());
        compare(bookAuthor2, bookAuthor);
    }

    @Test
    public void testUpdate() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookData book1 = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor = new BookAuthorData(book1.getId(), author.getId());
        Assert.assertEquals(this.bookAuthorMapper.insert(bookAuthor), 1);

        final BookData book2 = PersistTestHelper.createBook();
        bookAuthor.setBookId(book2.getId());
        Assert.assertEquals(this.bookAuthorMapper.update(bookAuthor), 1);

        final BookAuthorData bookAuthor2 = this.bookAuthorMapper.select(bookAuthor.getId());
        Assert.assertEquals(bookAuthor2.getBookId(), book2.getId());
        Assert.assertEquals(bookAuthor2.getAuthorId(), author.getId());
    }

    @Test
    public void testDelete() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookData book = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor);
        final int count = bookAuthorMapper.delete(bookAuthor);
        Assert.assertEquals(count, 1);
    }

    @Test
    public void testDeleteByAuthor() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookData book1 = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor1 = new BookAuthorData(book1.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor1);
        final BookData book2 = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor2 = new BookAuthorData(book2.getId(), author.getId());
        bookAuthorMapper.insert(bookAuthor2);
        final int count = bookAuthorMapper.deleteByAuthor(author);
        Assert.assertEquals(count, 2);
    }

    @Test
    public void testDeleteByBook() {
        final AuthorData author1 = PersistTestHelper.createAuthor();
        final BookData book = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor1 = new BookAuthorData(book.getId(), author1.getId());
        bookAuthorMapper.insert(bookAuthor1);
        final AuthorData author2 = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor2 = new BookAuthorData(book.getId(), author2.getId());
        bookAuthorMapper.insert(bookAuthor2);
        final int count = bookAuthorMapper.deleteByBook(book);
        Assert.assertEquals(count, 2);
    }

    @Test
    public void testDeleteByList() {
        final AuthorData author1 = PersistTestHelper.createAuthor();
        final BookData book = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor1 = new BookAuthorData(book.getId(), author1.getId());
        bookAuthorMapper.insert(bookAuthor1);
        final AuthorData author2 = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor2 = new BookAuthorData(book.getId(), author2.getId());
        bookAuthorMapper.insert(bookAuthor2);
        final int count = bookAuthorMapper.deleteByList(Arrays.asList(bookAuthor1, bookAuthor2));
        Assert.assertEquals(count, 2);
    }

    private void compare(BookAuthorData actual, BookAuthorData expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getAuthorId(), expected.getAuthorId());
        Assert.assertEquals(actual.getBookId(), expected.getBookId());
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testInsertInvalidBook() {
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor = new BookAuthorData(1, author.getId());
        this.bookAuthorMapper.insert(bookAuthor);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testInsertInvalidAuthor() {
        final BookData book = PersistTestHelper.createBook();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), 1);
        this.bookAuthorMapper.insert(bookAuthor);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testInsertDuplicate() {
        final BookData book = PersistTestHelper.createBook();
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), author.getId());
        this.bookAuthorMapper.insert(bookAuthor);
        final BookAuthorData bookAuthor2 = new BookAuthorData(book.getId(), author.getId());
        this.bookAuthorMapper.insert(bookAuthor2);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testDeleteReferencedBook() {
        final BookData book = PersistTestHelper.createBook();
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), author.getId());
        this.bookAuthorMapper.insert(bookAuthor);

        final BookMapper bookMapper = BookwormHelper.get(BookMapper.class);
        bookMapper.delete(book);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testDeleteReferencedAuthor() {
        final BookData book = PersistTestHelper.createBook();
        final AuthorData author = PersistTestHelper.createAuthor();
        final BookAuthorData bookAuthor = new BookAuthorData(book.getId(), author.getId());
        this.bookAuthorMapper.insert(bookAuthor);

        final AuthorMapper authorMapper = BookwormHelper.get(AuthorMapper.class);
        authorMapper.delete(author);
    }
}
