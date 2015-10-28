package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryBookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public interface CategoryBookMapper {
    
    CategoryBookEntity select(int id);
    
    List<CategoryBookEntity> selectByCategory(CategoryEntity category);
    
    List<CategoryBookEntity> selectByBook(BookEntity book);

    int insert(CategoryBookEntity categoryBook);
    
    int update(CategoryBookEntity categoryBook);

    int delete(CategoryBookEntity categoryBook);

    int deleteByList(List<CategoryBookEntity> categoryBooks);

    int deleteByBook(BookEntity book);

    int deleteByCategory(CategoryEntity category);

    int deleteByCategories(List<CategoryEntity> categories);

}
