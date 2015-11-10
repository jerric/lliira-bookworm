package net.lliira.bookworm.core.model;

import net.lliira.bookworm.core.service.CategoryService;

public interface Category {

    int getId();

    String getName();

    Category getParent();

    float getSiblingIndex();

    String getDescription();
    
    CategoryService getCategoryService();

    void setName(final String name);

    void setParent(final Category parent);

    void setSiblingIndex(final float siblingIndex);

    void setDescription(final String description);
}
