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
		String name = "name-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		Date publishDate = new Date();
		BookEntity book = new BookEntity();
		book.setName(name);
		book.setDescription(description);
		book.setPublishDate(publishDate);

		// test insert
		int count = bookMapper.insert(book);
		Assert.assertEquals(count, 1);
		compare(book, null, name, description, publishDate);

		Integer id = book.getId();
		Assert.assertNotNull(id);

		// test get to make sure insert succeeded.
		book = bookMapper.select(id);
		compare(book, id, name, description, publishDate);
	}

	@Test
	public void testUpdate() {
		BookEntity book = PersistTestHelper.createBook();
		Integer id = book.getId();
		String name = "name-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		Date publishDate = new Date();
		book.setName(name);
		book.setDescription(description);
		book.setPublishDate(publishDate);

		int count = bookMapper.update(book);
		Assert.assertEquals(count, 1);
		compare(book, id, name, description, publishDate);

		book = bookMapper.select(id);
		compare(book, id, name, description, publishDate);
	}

	@Test
	public void testDelete() {
		BookEntity book = PersistTestHelper.createBook();
		Integer id = book.getId();
		int count = bookMapper.delete(book);
		Assert.assertEquals(count, 1);
		book = bookMapper.select(id);
		Assert.assertNull(book);
	}

	@Test
	public void testSelectByAuthor() {
		AuthorEntity author1 = PersistTestHelper.createAuthor();
		AuthorEntity author2 = PersistTestHelper.createAuthor();
		BookEntity book1 = PersistTestHelper.createBook();
		BookEntity book2 = PersistTestHelper.createBook();

		BookAuthorMapper bookAuthorMapper = BookwormHelper.getContext().getBean(BookAuthorMapper.class);

		BookAuthorEntity book1Author1 = new BookAuthorEntity(book1.getId(), author1.getId());
		BookAuthorEntity book1Author2 = new BookAuthorEntity(book1.getId(), author2.getId());
		BookAuthorEntity book2Author2 = new BookAuthorEntity(book2.getId(), author2.getId());
		bookAuthorMapper.insert(book1Author1);
		bookAuthorMapper.insert(book1Author2);
		bookAuthorMapper.insert(book2Author2);

		List<BookEntity> books = bookMapper.selectByAuthor(author2);
		compare(books, book1, book2);
	}

	@Test
	public void testSelectByName() {
		// create some default test author
		BookEntity book1 = PersistTestHelper.createBook();
		BookEntity book2 = PersistTestHelper.createBook();
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
		Map<Integer, BookEntity> map = new HashMap<>(expected.length);
		for (BookEntity book : expected) {
			map.put(book.getId(), book);
		}
		for (BookEntity book : actual) {
			BookEntity expectedBook = map.get(book.getId());
			if (expectedBook != null) compare(book, expectedBook);
			else Assert.assertTrue(false, "unexpected book");
		}
	}

	private void compare(BookEntity actual, BookEntity expected) {
		Assert.assertEquals(actual.getId(), expected.getId());
		Assert.assertEquals(actual.getName(), expected.getName());
		Assert.assertEquals(actual.getDescription(), expected.getDescription());
		compareDate(actual.getPublishDate(), expected.getPublishDate());
	}

	private void compare(BookEntity actual, Integer id, String name, String description, Date publishDate) {
		Assert.assertEquals(actual.getName(), name);
		Assert.assertEquals(actual.getDescription(), description);
		compareDate(actual.getPublishDate(), publishDate);

		if (id != null) Assert.assertEquals(actual.getId(), id);
	}

	private void compareDate(Date actual, Date expected) {
		// only compare the date portion of the data.
		Calendar actualDate = Calendar.getInstance();
		actualDate.setTime(actual);
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTime(expected);
		Assert.assertEquals(actualDate.get(Calendar.YEAR), expectedDate.get(Calendar.YEAR));
		Assert.assertEquals(actualDate.get(Calendar.MONTH), expectedDate.get(Calendar.MONTH));
		Assert.assertEquals(actualDate.get(Calendar.DAY_OF_MONTH), expectedDate.get(Calendar.DAY_OF_MONTH));
	}
}
