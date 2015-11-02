package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryBookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public interface CategoryBookMapper {

    CategoryBookData select(int id);

    List<CategoryBookData> selectByCategory(CategoryData category);

    List<CategoryBookData> selectByBook(BookData book);

    /**
     * select the categoryBooks under the same category, and with the siblingIndexes equal or greater than the
     * given index, excluding the current book.
     * 
     * @param categoryBook
     * @return
     */
    List<CategoryBookData> selectByMinSibling(CategoryBookData categoryBook);

    int insert(CategoryBookData categoryBook);

    int update(CategoryBookData categoryBook);

    int delete(CategoryBookData categoryBook);

    int deleteByList(List<CategoryBookData> categoryBooks);

    int deleteByBook(BookData book);

    int deleteByCategory(CategoryData category);

    int deleteByCategories(List<CategoryData> categories);

}
