package org.lliira.bookworm.core.persist;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;

public class PersistTestHelper {

	public static AuthorEntity createAuthor(String name, String description) {
		AuthorEntity author = new AuthorEntity();
		author.setName(name);
		author.setDescription(description);
		
		
		
		return author;
	}

}
