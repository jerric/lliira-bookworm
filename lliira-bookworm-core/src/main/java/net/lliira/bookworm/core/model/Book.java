package net.lliira.bookworm.core.model;

import java.util.Date;

public interface Book {

    int getId();
    
    String getName();
    
    String getSortedName();
    
    Date getPublishDate();
    
    String getDescription();
    
    void setName(final String name);
    
    void setSortedName(final String sortedName);
    
    void setPublishDate(final Date publishDate);
    
    void setDescription(final String description);
}
