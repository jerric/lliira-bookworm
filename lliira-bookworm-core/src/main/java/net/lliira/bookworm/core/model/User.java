package net.lliira.bookworm.core.model;

import java.util.Date;

public interface User {
    
    int getId();
    
    Date getRegisteredTime();
    
    String getEmail();

    String getName();
    
    boolean isActive();
    
    String getDescription();
    
    void setEmail(final String email);
    
    void setName(final String name);
    
    void setActive(final boolean active);
    
    void setDescription(final String description);
}
