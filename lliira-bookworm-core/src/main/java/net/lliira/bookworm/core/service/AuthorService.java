package net.lliira.bookworm.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.AuthorException;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookAuthorData;
import net.lliira.bookworm.core.persist.model.BookData;

public class AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    public Author create(final String name, final String description) throws AuthorException {
        final AuthorData author = new AuthorData();
        author.setName(name);
        author.setDescription(description);
        validate(author);
        
        if (1 == this.authorMapper.insert(author)) return author;
        else throw new AuthorException("Creating author failed.");
    }

    public Author update(final Author author) throws AuthorException {
        final AuthorData authorData = new AuthorData(author);
        validate(authorData);
        
        if (1 == this.authorMapper.update(authorData)) return authorData;
        else throw new AuthorException("Updating author failed.");
    }

    private void validate(final AuthorData author) throws AuthorException {
        String name = author.getName();
        if (name == null || name.trim().isEmpty())
            throw new AuthorException("Author name cannot be null or empty");
        author.setName(name.trim());
        
        if (author.getDescription() != null) author.setDescription(author.getDescription().trim());
    }

    public void delete(final Author author) throws AuthorException {
        final AuthorData authorData = new AuthorData(author);

        // before deleting the author need to delete all references to it.
        this.bookAuthorMapper.deleteByAuthor(authorData);

        if (1 != this.authorMapper.delete(authorData)) throw new AuthorException("Deleting author failed");
    }

    public Author get(final int id) {
        return this.authorMapper.select(id);
    }

    public Set<Author> get(String namePattern) {
        // normalize the pattern
        namePattern = (namePattern == null || namePattern.isEmpty()) ? namePattern = "%"
                : "%" + namePattern.replace('*', '%') + "%";
        final List<AuthorData> authors = this.authorMapper.selectByName(namePattern);
        return new HashSet<>(authors);
    }

    /**
     * Get the authors of a book
     * 
     * @param book
     * @return
     */
    public Set<Author> get(final Book book) {
        final List<AuthorData> authors = this.authorMapper.selectByBook(new BookData(book));
        return new HashSet<>(authors);
    }

    /**
     * Set the authors of a book.
     * 
     * @param book
     * @param authors
     * @return
     * @throws AuthorException
     */
    public void setBookAuthor(final Book book, final Collection<Author> authors) throws AuthorException {
        final BookData bookData = new BookData(book);

        // get previous authors of the book
        final List<BookAuthorData> prevBookAuthors = this.bookAuthorMapper.selectByBook(bookData);
        final Map<Integer, BookAuthorData> prevMap = new HashMap<>(prevBookAuthors.size());
        final Map<Integer, Author> newMap = new HashMap<>(authors.size());
        for (final BookAuthorData bookAuthor : prevBookAuthors) {
            prevMap.put(bookAuthor.getAuthorId(), bookAuthor);
        }

        // determine the entries to be inserted and removed
        final List<BookAuthorData> removes = new ArrayList<>();
        final List<BookAuthorData> inserts = new ArrayList<>();
        for (final Author author : authors) {
            newMap.put(author.getId(), author);
            if (!prevMap.containsKey(author.getId()))
                inserts.add(new BookAuthorData(book.getId(), author.getId()));
        }
        for (final BookAuthorData bookAuthor : prevBookAuthors) {
            if (!newMap.containsKey(bookAuthor.getAuthorId())) removes.add(bookAuthor);
        }

        // insert new authors
        for (final BookAuthorData bookAuthor : inserts) {
            if (1 != this.bookAuthorMapper.insert(bookAuthor))
                throw new AuthorException(String.format("Inserting book-author failed, book=%d, author=%d",
                        bookAuthor.getBookId(), bookAuthor.getAuthorId()));
        }

        // delete old authors
        for (final BookAuthorData bookAuthor : removes) {
            if (removes.size() != this.bookAuthorMapper.deleteByList(removes))
                throw new AuthorException(String.format("Deleting book-author failed, book=%d, author=%d",
                        bookAuthor.getBookId(), bookAuthor.getAuthorId()));
        }
    }
}
