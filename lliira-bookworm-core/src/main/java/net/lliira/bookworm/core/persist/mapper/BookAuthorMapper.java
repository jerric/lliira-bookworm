package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookAuthorData;
import net.lliira.bookworm.core.persist.model.BookData;

public interface BookAuthorMapper {

    BookAuthorData select(int id);

    List<BookAuthorData> selectByAuthor(AuthorData author);

    List<BookAuthorData> selectByBook(BookData book);

    int insert(BookAuthorData bookAuthor);

    int update(BookAuthorData bookAuthor);

    int delete(BookAuthorData bookAuthor);

    int deleteByList(List<BookAuthorData> bookAuthors);

    int deleteByAuthor(AuthorData author);

    int deleteByBook(BookData book);

}
