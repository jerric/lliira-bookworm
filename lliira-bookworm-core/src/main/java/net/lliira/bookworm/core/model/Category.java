package net.lliira.bookworm.core.model;

public interface Category {

    int getId();

    String getName();

    Category getParent();

    float getSiblingIndex();

    String getDescription();

    void setName(final String name);

    void setParent(final Category parent);

    void setSiblingIndex(final float siblingIndex);

    void setDescription(final String description);
}
