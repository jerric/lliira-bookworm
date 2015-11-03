package net.lliira.bookworm.core.service;

import static net.lliira.bookworm.core.BookwormHelper.ds;
import static net.lliira.bookworm.core.BookwormHelper.getIncrement;
import static net.lliira.bookworm.core.BookwormHelper.normalizePattern;
import static net.lliira.bookworm.core.BookwormHelper.ns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.model.Author;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.persist.mapper.BookAuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.model.AuthorData;
import net.lliira.bookworm.core.persist.model.BookAuthorData;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryBookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

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

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Autowired
    private CategoryBookMapper categoryBookMapper;

    public Map<Float, Book> get(Category category) {
        final CategoryData categoryData = new CategoryData(category);
        final List<BookData> books = this.bookMapper.selectByCategory(categoryData);
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByCategory(categoryData);

        // convert books into a map for easy access
        final Map<Integer, Book> tempMap = new HashMap<>(books.size());
        for (final Book book : books) {
            tempMap.put(book.getId(), book);
        }

        final Map<Float, Book> bookMap = new LinkedHashMap<>(books.size());
        for (final CategoryBookData catBook : catBooks) {
            bookMap.put(catBook.getSiblingIndex(), tempMap.get(catBook.getBookId()));
        }
        return bookMap;
    }

    public List<Book> get(Author author) {
        final List<BookData> books = this.bookMapper.selectByAuthor(new AuthorData(author));
        return new ArrayList<>(books);
    }

    public List<Book> get(String namePattern) {
        // normalize pattern
        namePattern = normalizePattern(namePattern);
        final List<BookData> books = this.bookMapper.selectByName(namePattern);
        return new ArrayList<>(books);
    }

    public Book get(int id) {
        return this.bookMapper.select(id);
    }

    public Book create(String name, String sortedName, Date publishDate, String description)
            throws BookException {
        final BookData book = new BookData();
        book.setName(name);
        book.setSortedName(sortedName);
        book.setPublishDate(publishDate);
        book.setDescription(description);
        validate(book);
        if (1 == this.bookMapper.insert(book)) return book;
        else throw new BookException("Creating book failed.");
    }

    public void update(Book book) throws BookException {
        final BookData bookData = new BookData(book);
        validate(bookData);
        if (1 != this.bookMapper.update(bookData)) throw new BookException("Updating book failed.");
        
        // set the values back, since they may be normalized.
        book.setName(bookData.getName());
        book.setSortedName(bookData.getSortedName());
        book.setPublishDate(bookData.getPublishDate());
        book.setDescription(bookData.getDescription());
    }

    private void validate(final BookData book) throws BookException {
        // validate name
        final String name = book.getName();
        if (name == null || name.trim().isEmpty())
            throw new BookException("Book name cannot be null or empty");
        book.setName(name.trim());

        // normalize sorted name
        final String sortedName = book.getSortedName();
        book.setSortedName(
                (sortedName == null || sortedName.isEmpty()) ? sortName(book.getName()) : sortedName.trim());

        // normalize description
        if (book.getDescription() != null) book.setDescription(book.getDescription().trim());

        // normalize publish date by removing time portion
        final Date publishDate = book.getPublishDate();
        if (publishDate != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(publishDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            book.setPublishDate(calendar.getTime());
        }
    }

    public void delete(Book book) throws BookException {
        final BookData bookData = new BookData(book);

        // need to delete author & category associations first
        this.bookAuthorMapper.deleteByBook(bookData);
        this.categoryBookMapper.deleteByBook(bookData);

        if (1 != this.bookMapper.delete(new BookData(book))) throw new BookException("Updating book failed.");
    }

    public void setAuthors(final Book book, final Collection<Author> authors) throws BookException {
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
                throw new BookException(String.format("Inserting book-author failed, book=%d, author=%d",
                        bookAuthor.getBookId(), bookAuthor.getAuthorId()));
        }

        // delete old authors
        for (final BookAuthorData bookAuthor : removes) {
            if (removes.size() != this.bookAuthorMapper.deleteByList(removes))
                throw new BookException(String.format("Deleting book-author failed, book=%d, author=%d",
                        bookAuthor.getBookId(), bookAuthor.getAuthorId()));
        }
    }

    public void setCategories(final Book book, final Map<Category, Float> categories) throws BookException {
        final BookData bookData = new BookData(book);

        // get previous categories of the book
        final List<CategoryBookData> prevCatBooks = this.categoryBookMapper.selectByBook(bookData);
        final Map<Integer, CategoryBookData> prevMap = new HashMap<>(prevCatBooks.size());
        final Map<Integer, Category> newMap = new HashMap<>(categories.size());
        for (final CategoryBookData catBook : prevCatBooks) {
            prevMap.put(catBook.getCategoryId(), catBook);
        }

        // determine the entries to be inserted, updated or removed
        final List<CategoryBookData> removes = new ArrayList<>();
        final List<CategoryBookData> inserts = new ArrayList<>();
        final List<CategoryBookData> updates = new ArrayList<>();
        for (final Category category : categories.keySet()) {
            newMap.put(category.getId(), category);
            final CategoryBookData catBook = prevMap.get(category.getId());
            final float siblingIndex = categories.get(category);
            if (catBook == null) {
                inserts.add(new CategoryBookData(category.getId(), book.getId(), siblingIndex));
            } else if (catBook.getSiblingIndex() != siblingIndex) {
                updates.add(new CategoryBookData(category.getId(), book.getId(), siblingIndex));
            }
        }
        for (final CategoryBookData catBook : prevCatBooks) {
            if (!newMap.containsKey(catBook.getCategoryId())) removes.add(catBook);
        }

        // delete old associations
        for (final CategoryBookData catBook : removes) {
            if (removes.size() != this.categoryBookMapper.deleteByList(removes))
                throw new BookException(String.format("Deleting category-book failed, book=%d, category=%d",
                        catBook.getBookId(), catBook.getCategoryId()));
        }

        // insert new associations
        for (final CategoryBookData catBook : inserts) {
            if (1 != this.categoryBookMapper.insert(catBook))
                throw new BookException(String.format("Inserting category-book failed, book=%d, category=%d",
                        catBook.getBookId(), catBook.getCategoryId()));
            normalizeSiblingIndexes(catBook);
        }

        // update associations
        for (final CategoryBookData catBook : updates) {
            if (1 != this.categoryBookMapper.update(catBook))
                throw new BookException(String.format("Updating category-book failed, book=%d, category=%d",
                        catBook.getBookId(), catBook.getCategoryId()));
            normalizeSiblingIndexes(catBook);
        }
    }

    private void normalizeSiblingIndexes(final CategoryBookData categoryBook) {
        final int nsibling = ns(categoryBook.getSiblingIndex());

        // the list is assumed to be sorted by sibling index in ascending order.
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByMinSibling(categoryBook);
        // only need to normalize it when the next sibling index is the same as the current one
        if (catBooks.isEmpty() || ns(catBooks.get(0).getSiblingIndex()) == ns(categoryBook.getSiblingIndex()))
            return;

        // determine the increments
        final int increment = getIncrement(nsibling);

        int prevNSibling = nsibling;
        for (final CategoryBookData catBook : catBooks) {
            final int ns = ns(catBook.getSiblingIndex());
            if (prevNSibling != ns) break; // stop, no need to increment any further

            prevNSibling = prevNSibling + increment;
            catBook.setSiblingIndex(ds(prevNSibling));
            this.categoryBookMapper.update(catBook);
        }
    }
}
