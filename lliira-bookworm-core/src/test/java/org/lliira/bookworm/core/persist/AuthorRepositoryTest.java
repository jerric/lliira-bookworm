package org.lliira.bookworm.core.persist;

import java.util.Random;

import net.lliira.bookworm.core.persist.AuthorStorage;
import net.lliira.bookworm.core.persist.entity.AuthorEntity;

import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthorRepositoryTest {

	private static final AuthorStorage authorStorage = TestHelper.get(AuthorStorage.class);
	private static final Random random = TestHelper.getRandom();

	public static AuthorEntity createAuthor() {
		return createAuthor("author-" + random.nextInt(), "desc-" + random.nextInt());
	}

	private static AuthorEntity createAuthor(String name, String description) {
		AuthorEntity authorData = new AuthorEntity();
		authorData.setName(name);
		authorData.setDescription(description);
		authorStorage.createAuthor(authorData);
		return authorData;
	}

	@Test
	public void testCreateAuthor() {
		String name = "author-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		AuthorEntity authorData = createAuthor(name, description);
		Long id = authorData.getId();
		Assert.assertNotNull(id);
		compare(authorData, id, name, description);

		authorData = authorStorage.getAuthor(id);
		compare(authorData, id, name, description);
	}

	@Test
	public void testUpdateAuthor() {
		AuthorEntity authorData = createAuthor();

		Long id = authorData.getId();
		String name = "author-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		authorData.setName(name);
		authorData.setDescription(description);
		authorStorage.updateAuthor(authorData);
		compare(authorData, id, name, description);

		authorData = authorStorage.getAuthor(id);
		compare(authorData, id, name, description);
	}

	@Test
	public void testDeleteAuthor() {
		AuthorEntity authorData = createAuthor();
		Long id = authorData.getId();
		authorStorage.deleteAuthor(authorData);
		authorData = authorStorage.getAuthor(id);
		Assert.assertNull(authorData);
	}

	private void compare(AuthorEntity authorData, Long id, String name, String description) {
		Assert.assertEquals(authorData.getId(), id);
		Assert.assertEquals(authorData.getName(), name);
		Assert.assertEquals(authorData.getDescription(), description);
	}
}
