package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookAuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookEntity;

public interface BookAuthorMapper {

	int insert(BookAuthorEntity bookAuthor);
	
	int delete(BookAuthorEntity bookAuthor);
	
	int deleteByList(List<BookAuthorEntity> bookAuthors);
	
	int deleteByAuthor(AuthorEntity author);
	
	int deleteByBook(BookEntity book);
}
