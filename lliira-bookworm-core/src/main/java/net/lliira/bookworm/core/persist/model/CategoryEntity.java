package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class CategoryEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Integer parentId;
    private float siblingIndex;
    private String description;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parentId
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public float getSiblingIndex() {
        return this.siblingIndex;
    }

    public void setSiblingIndex(float siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
