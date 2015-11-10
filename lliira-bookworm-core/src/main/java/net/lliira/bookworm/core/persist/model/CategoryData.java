package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.service.CategoryService;

public class CategoryData implements Category, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private Integer parentId;
    private float siblingIndex;
    private String description;
    private CategoryService categoryService;

    public CategoryData() {}

    public CategoryData(final Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentId = (category.getParent() == null) ? null : category.getParent().getId();
        this.siblingIndex = category.getSiblingIndex();
        this.description = category.getDescription();
        this.categoryService = category.getCategoryService();
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
        return (parentId == null) ? null : this.categoryService.get(parentId);
    }

    @Override
    public void setParent(final Category parent) {
        this.parentId = (parent == null) ? null : parent.getId();
    }

    @Override
    public CategoryService getCategoryService() {
        return this.categoryService;
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
    }

    public CategoryData setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        return this;
    }
}
