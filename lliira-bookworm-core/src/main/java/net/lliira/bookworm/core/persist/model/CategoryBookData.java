package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class CategoryBookData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private int categoryId;
    private int bookId;
    private float siblingIndex;

    public CategoryBookData(final int id) {
        this.id = id;
    }

    /**
     */
    public CategoryBookData(int categoryId, int bookId, float siblingIndex) {
        this.categoryId = categoryId;
        this.bookId = bookId;
        this.siblingIndex = siblingIndex;
    }

    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the bookId
     */
    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public float getSiblingIndex() {
        return this.siblingIndex;
    }

    public void setSiblingIndex(float siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    @Override
    public String toString() {
        return String.format("id=%d, book=%d, cat=%d, sibling=%.3f", id, bookId, categoryId, siblingIndex);
    }
}
