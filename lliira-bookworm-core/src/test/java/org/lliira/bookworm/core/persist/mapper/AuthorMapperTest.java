package org.lliira.bookworm.core.persist.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookAuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookEntity;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;

import org.lliira.bookworm.core.TestHelper;
import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.springframework.transaction.TransactionStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthorMapperTest {

	private static final Random random = TestHelper.getRandom();
	
	private TransactionStatus status;
	private AuthorMapper authorMapper;
	

	@BeforeMethod
	public void setUp() {
		status = PersistTestHelper.beginTransaction();
		authorMapper = TestHelper.get(AuthorMapper.class);
	}
	
	@AfterMethod
	public void shutdown() {
		PersistTestHelper.rollback(status);
	}
	

	@Test
	public void testInsert() {
		String name = "name-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		AuthorEntity author = new AuthorEntity();
		author.setName(name);
		author.setDescription(description);

		// test insert
		int count = authorMapper.insert(author);
		Assert.assertEquals(count, 1);
		compare(author, null, name, description);

		Integer id = author.getId();
		Assert.assertNotNull(id);

		// test get to make sure insert succeeded.
		author = authorMapper.select(id);
		compare(author, id, name, description);
	}

	@Test
	public void testUpdate() {
		AuthorEntity author = PersistTestHelper.createAuthor();
		Integer id = author.getId();
		String name = "name-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		author.setName(name);
		author.setDescription(description);

		int count = authorMapper.update(author);
		Assert.assertEquals(count, 1);
		compare(author, id, name, description);

		author = authorMapper.select(id);
		compare(author, id, name, description);
	}

	@Test
	public void testDelete() {
		AuthorEntity author = PersistTestHelper.createAuthor();
		Integer id = author.getId();
		int count = authorMapper.delete(author);
		Assert.assertEquals(count, 1);
		author = authorMapper.select(id);
		Assert.assertNull(author);
	}

	@Test
	public void testSelectByBook() {
		AuthorEntity author1 = PersistTestHelper.createAuthor();
		AuthorEntity author2 = PersistTestHelper.createAuthor();
		BookEntity book1 = PersistTestHelper.createBook();
		BookEntity book2 = PersistTestHelper.createBook();

		BookAuthorMapper bookAuthorMapper = TestHelper.getContext().getBean(BookAuthorMapper.class);

		BookAuthorEntity book1Author1 = new BookAuthorEntity(book1.getId(), author1.getId());
		BookAuthorEntity book1Author2 = new BookAuthorEntity(book1.getId(), author2.getId());
		BookAuthorEntity book2Author2 = new BookAuthorEntity(book2.getId(), author2.getId());
		bookAuthorMapper.insert(book1Author1);
		bookAuthorMapper.insert(book1Author2);
		bookAuthorMapper.insert(book2Author2);

		List<AuthorEntity> authors = authorMapper.selectByBook(book1);
		compare(authors, author1, author2);
	}

	@Test
	public void testSelectByName() {
		// create some default test author
		AuthorEntity author1 = PersistTestHelper.createAuthor();
		AuthorEntity author2 = PersistTestHelper.createAuthor();
		List<AuthorEntity> authors = authorMapper.selectByName("%author%");
		compare(authors, author1, author2);

		// modify an author, and search for it
		author1.setName("a new test author");
		authorMapper.update(author1);

		authors = authorMapper.selectByName("%test%");
		compare(authors, author1);
	}

	private void compare(Collection<AuthorEntity> actual, AuthorEntity... expected) {
		Assert.assertEquals(actual.size(), expected.length);
		// create a map to store expected authors
		Map<Integer, AuthorEntity> map = new HashMap<Integer, AuthorEntity>(expected.length);
		for (AuthorEntity author : expected) {
			map.put(author.getId(), author);
		}
		for (AuthorEntity author : actual) {
			AuthorEntity expectedAuthor = map.get(author.getId());
			if (expectedAuthor != null) compare(author, expectedAuthor);
			else Assert.assertTrue(false, "unexpected author");
		}
	}

	private void compare(AuthorEntity actual, AuthorEntity expected) {
		Assert.assertEquals(actual.getId(), expected.getId());
		Assert.assertEquals(actual.getName(), expected.getName());
		Assert.assertEquals(actual.getDescription(), expected.getDescription());
	}

	private void compare(AuthorEntity actual, Integer id, String name, String description) {
		Assert.assertEquals(actual.getName(), name);
		Assert.assertEquals(actual.getDescription(), description);
		if (id != null) Assert.assertEquals(actual.getId(), id);
	}
}
