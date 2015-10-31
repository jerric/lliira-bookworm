package net.lliira.bookworm.core;

public abstract class BookwormException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BookwormException() {
        super();
    }

    public BookwormException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BookwormException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookwormException(String message) {
        super(message);
    }

    public BookwormException(Throwable cause) {
        super(cause);
    }

}
