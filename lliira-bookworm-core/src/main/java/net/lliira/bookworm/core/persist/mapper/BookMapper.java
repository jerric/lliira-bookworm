package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookEntity;
import net.lliira.bookworm.core.persist.entity.CategoryEntity;

public interface BookMapper {

	int insert(BookEntity book);
	
	int update(BookEntity book);
	
	int delete(BookEntity book);
	
	BookEntity selete(int id);
	
	List<BookEntity> selectByName(String pattern);
	
	List<BookEntity> selectByAuthor(AuthorEntity author);
	
	List<BookEntity> selectByCategory(CategoryEntity category);
	
	List<BookEntity> selectByCategories(List<CategoryEntity> categories);
}
