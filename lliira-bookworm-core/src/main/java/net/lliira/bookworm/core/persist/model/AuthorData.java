package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

import net.lliira.bookworm.core.model.Author;

public class AuthorData implements Author, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;

    public AuthorData() {}

    public AuthorData(final Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.description = author.getDescription();
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
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Set the id of the author. This should only be used internally my the O/R mapper.
     * 
     * @param id
     *            the id of the author.
     * 
     */
    public void setId(final int id) {
        this.id = id;
    }

}
