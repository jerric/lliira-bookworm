package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.entity.BookEntity;
import net.lliira.bookworm.core.persist.entity.CategoryBookEntity;
import net.lliira.bookworm.core.persist.entity.CategoryEntity;

public interface CategoryBookMapper {

	int insert(CategoryBookEntity categoryBook);
	
	int delete(CategoryBookEntity categoryBook);
	
	int deleteByList(List<CategoryBookEntity> categoryBooks);
	
	int deleteByBook(BookEntity book);
	
	int deleteByCategory(CategoryEntity category);
	
	int deleteByCategories(List<CategoryEntity> categories);
	
}
