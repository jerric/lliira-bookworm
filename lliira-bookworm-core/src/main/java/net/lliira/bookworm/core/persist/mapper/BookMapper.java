package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public interface BookMapper {

	int insert(BookEntity book);
	
	int update(BookEntity book);
	
	int delete(BookEntity book);
	
	BookEntity select(int id);
	
	List<BookEntity> selectByName(String pattern);
	
	List<BookEntity> selectByAuthor(AuthorEntity author);
	
	List<BookEntity> selectByCategory(CategoryEntity category);
	
	List<BookEntity> selectByCategories(List<CategoryEntity> categories);
}
