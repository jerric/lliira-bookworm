package net.lliira.bookworm.core.persist.mapper;

import java.util.List;

import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookData;

public interface AuthorMapper {

    public int insert(AuthorData author);

    public int update(AuthorData author);

    public int delete(AuthorData author);

    public AuthorData select(int id);

    public List<AuthorData> selectByName(String pattern);

    public List<AuthorData> selectByBook(BookData book);
}
