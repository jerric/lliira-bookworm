package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

import net.lliira.bookworm.core.model.Category;

public class CategoryData implements Category, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private Integer parentId;
    private Category parent;
    private float siblingIndex;
    private String description;

    public CategoryData() {}

    public CategoryData(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parent = category.getParent();
        if (this.parent != null) this.parentId = this.parent.getId();
        this.siblingIndex = category.getSiblingIndex();
        this.description = category.getDescription();
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public float getSiblingIndex() {
        return this.siblingIndex;
    }

    @Override
    public void setSiblingIndex(float siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Category getParent() {
        return this.parent;
    }

    @Override
    public void setParent(final Category parent) {
        this.parent = parent;
        this.parentId = (parent == null) ? null : parent.getId();
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the parentId
     */
    public Integer getParentId() {
        return this.parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
        this.parent = null;
    }

}
