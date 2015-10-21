package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class CategoryBookEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    private final Integer categoryId;
    private final Integer bookId;
    private float siblingOrder;

    /**
     * @param categoryId
     * @param bookId
     */
    public CategoryBookEntity(Integer categoryId, Integer bookId) {
        super();
        this.categoryId = categoryId;
        this.bookId = bookId;
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

    /**
     * @return the bookId
     */
    public Integer getBookId() {
        return bookId;
    }

    public float getSiblingOrder() {
        return this.siblingOrder;
    }

    public void setSiblingOrder(float siblingOrder) {
        this.siblingOrder = siblingOrder;
    }
}
