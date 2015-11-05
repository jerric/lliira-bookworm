package net.lliira.bookworm.core.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.AuthorException;
import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookData;

public class AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Autowired
    private BookService bookService;

    public Author create(final String name, final String description) throws AuthorException {
        final AuthorData author = new AuthorData();
        author.setName(name);
        author.setDescription(description);
        validate(author);

        if (1 == this.authorMapper.insert(author)) return author;
        else throw new AuthorException("Creating author failed.");
    }

    public void update(final Author author) throws AuthorException {
        // always create a new data, just in case validation fails, and we don't want to change the author.
        final AuthorData authorData = new AuthorData(author);
        validate(authorData);

        if (1 != this.authorMapper.update(authorData)) throw new AuthorException("Updating author failed.");

        // set the value back, since they may be normalized
        author.setName(authorData.getName());
        author.setDescription(authorData.getDescription());
    }

    private void validate(final AuthorData author) throws AuthorException {
        final String name = author.getName();
        if (name == null || name.trim().isEmpty())
            throw new AuthorException("Author name cannot be null or empty");
        author.setName(name.trim());

        if (author.getDescription() != null) author.setDescription(author.getDescription().trim());
    }

    public void delete(final Author author) throws AuthorException {
        final AuthorData authorData = (author instanceof AuthorData) ? (AuthorData) author
                : new AuthorData(author);

        // before deleting the author need to delete all references to it.
        this.bookAuthorMapper.deleteByAuthor(authorData);

        if (1 != this.authorMapper.delete(authorData)) throw new AuthorException("Deleting author failed");
    }

    public Author get(final int id) {
        return this.authorMapper.select(id);
    }

    public Set<Author> get(String namePattern) {
        // normalize the pattern
        namePattern = BookwormHelper.normalizePattern(namePattern);
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
        final BookData bookData = (book instanceof BookData) ? (BookData) book : new BookData(book);
        final List<AuthorData> authors = this.authorMapper.selectByBook(bookData);
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
    public void setAuthorsToBook(final Book book, final Collection<Author> authors) throws AuthorException {
        try {
            this.bookService.setAuthors(book, authors);
        } catch (BookException ex) {
            throw new AuthorException(ex);
        }
    }
}
