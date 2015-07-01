package org.lliira.bookworm.core.persist.mapper;

import java.util.Random;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;

import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthorMapperTest {

	private final Random random = TestHelper.getRandom();

	@Test
	public void testInsert() {
		String name = "name-" + random.nextInt();
		String description = "desc-" + random.nextInt();
		AuthorEntity authorEntity = new AuthorEntity();
		authorEntity.setName(name);
		authorEntity.setDescription(description);

		AuthorMapper authorMapper = TestHelper.getContext().getBean(AuthorMapper.class);

		// test insert
		int count = authorMapper.insert(authorEntity);
		Assert.assertEquals(count, 1);
		compare(authorEntity, null, name, description);

		Integer id = authorEntity.getId();
		Assert.assertNotNull(id);

		// test get to make sure insert succeeded.
		authorEntity = authorMapper.select(id);
		compare(authorEntity, id, name, description);
	}

	private void compare(AuthorEntity authorEntity, Integer id, String name, String description) {
		Assert.assertEquals(authorEntity.getName(), name);
		Assert.assertEquals(authorEntity.getDescription(), description);
		if (id != null) Assert.assertEquals(authorEntity.getId(), id);
	}
}
