package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class BookAuthorEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer bookId;
    private Integer authorId;
    
    public BookAuthorEntity(int id) {
        this.id = id;
    }
    
    public BookAuthorEntity(final int bookId, final int authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

}
