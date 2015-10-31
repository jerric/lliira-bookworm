package net.lliira.bookworm.core.model;

public interface Author {

    int getId();
    
    String getName();
    
    String getDescription();

    void setName(final String name);
    
    void setDescription(final String description);
}
