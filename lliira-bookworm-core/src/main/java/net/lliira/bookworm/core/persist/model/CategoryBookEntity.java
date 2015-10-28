package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class CategoryBookEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer categoryId;
    private Integer bookId;
    private float siblingIndex;
    
    public CategoryBookEntity(final int id) {
        this.id = id;
    }

    /**
     */
    public CategoryBookEntity(int categoryId, int bookId, float siblingIndex) {
        this.categoryId = categoryId;
        this.bookId = bookId;
        this.siblingIndex = siblingIndex;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the bookId
     */
    public Integer getBookId() {
        return bookId;
    }
    
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public float getSiblingIndex() {
        return this.siblingIndex;
    }

    public void setSiblingIndex(float siblingIndex) {
        this.siblingIndex = siblingIndex;
    }
}
