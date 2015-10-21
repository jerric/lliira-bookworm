package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;

public interface AuthorMapper {

	public int insert(AuthorEntity author);

	public int update(AuthorEntity author);

	public int delete(AuthorEntity author);

	public AuthorEntity select(int authorId);
	
	public List<AuthorEntity> selectByName(String pattern);

	public List<AuthorEntity> selectByBook(BookEntity book);
}
