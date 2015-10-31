package net.lliira.bookworm.core.persist.model;

import java.io.Serializable;

public class BookAuthorData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private int bookId;
    private int authorId;
    
    public BookAuthorData(int id) {
        this.id = id;
    }
    
    public BookAuthorData(final int bookId, final int authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

}
