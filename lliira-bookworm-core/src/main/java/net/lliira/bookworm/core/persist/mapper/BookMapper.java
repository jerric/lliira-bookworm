package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public interface BookMapper {

    int insert(BookData book);

    int update(BookData book);

    int delete(BookData book);

    BookData select(int id);

    List<BookData> selectByName(String pattern);

    List<BookData> selectByAuthor(AuthorData author);

    List<BookData> selectByCategory(CategoryData category);

    List<BookData> selectByCategories(List<CategoryData> categories);
}
