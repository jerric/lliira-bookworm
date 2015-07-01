package org.lliira.bookworm.core.persist;

import java.util.Random;

import org.lliira.bookworm.core.TestHelper;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;

public class PersistTestHelper {

	public static AuthorEntity createAuthor() {
		Random random = TestHelper.getRandom();
		AuthorEntity authorEntity = new AuthorEntity();
		authorEntity.setName("author-name-" + random.nextInt());
		authorEntity.setDescription("author-desc-" + random.nextInt());

		AuthorMapper authorMapper = TestHelper.getContext().getBean(AuthorMapper.class);
		authorMapper.insert(authorEntity);

		return authorEntity;
	}

}
