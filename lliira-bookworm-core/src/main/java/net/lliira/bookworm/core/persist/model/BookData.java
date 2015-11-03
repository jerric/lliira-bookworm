package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;
import java.util.Date;

import net.lliira.bookworm.core.model.Book;

public class BookData implements Book, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String sortedName;
    private Date publishDate;
    private String description;

    public BookData() {}

    public BookData(final Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.sortedName = book.getSortedName();
        this.publishDate = book.getPublishDate();
        this.description = book.getDescription();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSortedName() {
        return this.sortedName;
    }

    @Override
    public Date getPublishDate() {
        return this.publishDate;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
        if (this.sortedName == null) this.sortedName = this.name;
    }

    @Override
    public void setSortedName(String sortedName) {
        this.sortedName = sortedName;
    }

    @Override
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the id of the book. This should only be used internally by the O/R mapper.
     * 
     * @param id
     *            the id of the book.
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
