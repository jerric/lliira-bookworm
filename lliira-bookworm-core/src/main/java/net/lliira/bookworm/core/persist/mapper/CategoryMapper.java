package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public interface CategoryMapper {

    int insert(CategoryData category);

    int update(CategoryData category);
    
    int updateParentToNull(CategoryData category);

    int delete(CategoryData category);

    CategoryData select(int categoryId);

    List<CategoryData> selectRoots();

    List<CategoryData> selectByParent(CategoryData parent);

    List<CategoryData> selectByBook(BookData book);
    
    float selectMaxRootIndex();
    
    float selectMaxSiblingIndex(CategoryData parent);
}
