package net.lliira.bookworm.core.service;

import java.util.Date;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.model.BookData;

public class BookService {

    public static String sortName(final String name) {
        final String nameLower = name.toLowerCase();
        if (nameLower.startsWith("a ")) return name.substring(2).trim() + ", A";
        if (nameLower.startsWith("an ")) return name.substring(3).trim() + ", An";
        if (nameLower.startsWith("the ")) return name.substring(4).trim() + ", The";
        return name;
    }

    @Autowired
    private BookMapper bookMapper;

    public List<Book> findByCategory(Category category, boolean recursive) {
        return null;
    }

    public List<Book> findByAuthor(Author author) {
        return null;
    }

    public List<Book> findByName(String name) {
        return null;
    }

    public Book findById(int id) {
        return null;
    }

    public Book create(String name, String sortedName, Date publishDate, String description)
            throws BookException {
        final BookData book = new BookData();
        book.setName(name);
        book.setSortedName(sortedName);
        book.setPublishDate(publishDate);
        book.setDescription(description);
        if (1 == this.bookMapper.insert(book)) return book;
        else throw new BookException("Creating book failed.");
    }

    public void update(Book book) {

    }

    public void delete(Book book) {

    }
}
