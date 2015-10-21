package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public interface CategoryMapper {

	int insert(CategoryEntity category);
	
	int update(CategoryEntity category);
	
	int delete(CategoryEntity category);
	
	CategoryEntity select(int categoryId);
	
	List<CategoryEntity> selectByParent(CategoryEntity parent);
	
	List<CategoryEntity> selectByBook(BookEntity book);
}
