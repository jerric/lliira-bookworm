package net.lliira.bookworm.core.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Book {

    private final Set<Author> authors = new HashSet<>();
    private final Map<Category, Float> categories = new HashMap<>();

    private final Long id;
    private String name;
    private String sortedName;
    private Date publishDate;

    public Book(final long id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    public String getSortedName() {
        return this.sortedName;
    }

    public void setSortedName(final String sortedName) {
        this.sortedName = sortedName;
    }

    /**
     * @return the publishDate
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * @param publishDate
     *            the publishDate to set
     */
    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
    }

    public Set<Author> getAuthors() {
        return Collections.unmodifiableSet(this.authors);
    }

    public boolean addAuthor(final Author author) {
        return this.authors.add(author);
    }

    public boolean removeAuthor(final Author author) {
        return this.authors.remove(author);
    }

    public Map<Category, Float> getCategories() {
        return Collections.unmodifiableMap(this.categories);
    }

    public Float setCategory(final Category category, float siblingIndex) {
        return this.categories.put(category, siblingIndex);
    }

    public Float removeCategory(final Category category) {
        return this.categories.remove(category);
    }
}
