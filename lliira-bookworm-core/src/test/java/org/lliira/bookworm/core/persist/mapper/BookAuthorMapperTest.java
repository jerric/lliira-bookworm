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
		AuthorEntity author = PersistTestHelper.createAuthor();
		BookEntity book = PersistTestHelper.createBook();
		BookAuthorEntity bookAuthor = new BookAuthorEntity(author.getId(), book.getId());
		int count = bookAuthorMapper.insert(bookAuthor);
		Assert.assertEquals(count, 1);
	}

	@Test
	public void testDelete() {
		AuthorEntity author = PersistTestHelper.createAuthor();
		BookEntity book = PersistTestHelper.createBook();
		BookAuthorEntity bookAuthor = new BookAuthorEntity(author.getId(), book.getId());
		bookAuthorMapper.insert(bookAuthor);
		int count = bookAuthorMapper.delete(bookAuthor);
		Assert.assertEquals(count, 1);
	}

	@Test
	public void testDeleteByAuthor() {
		AuthorEntity author = PersistTestHelper.createAuthor();
		BookEntity book1 = PersistTestHelper.createBook();
		BookAuthorEntity bookAuthor1 = new BookAuthorEntity(author.getId(), book1.getId());
		bookAuthorMapper.insert(bookAuthor1);
		BookEntity book2 = PersistTestHelper.createBook();
		BookAuthorEntity bookAuthor2 = new BookAuthorEntity(author.getId(), book2.getId());
		bookAuthorMapper.insert(bookAuthor2);
		int count = bookAuthorMapper.deleteByAuthor(author);
		Assert.assertEquals(count, 2);
	}

	@Test
	public void testDeleteByBook() {
		AuthorEntity author1 = PersistTestHelper.createAuthor();
		BookEntity book = PersistTestHelper.createBook();
		BookAuthorEntity bookAuthor1 = new BookAuthorEntity(author1.getId(), book.getId());
		bookAuthorMapper.insert(bookAuthor1);
		AuthorEntity author2 = PersistTestHelper.createAuthor();
		BookAuthorEntity bookAuthor2 = new BookAuthorEntity(author2.getId(), book.getId());
		bookAuthorMapper.insert(bookAuthor2);
		int count = bookAuthorMapper.deleteByBook(book);
		Assert.assertEquals(count, 2);
	}
}
