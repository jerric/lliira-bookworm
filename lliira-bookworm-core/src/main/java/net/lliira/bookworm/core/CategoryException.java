package net.lliira.bookworm.core;

public class CategoryException extends BookwormException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CategoryException() {
        super();
    }

    public CategoryException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(Throwable cause) {
        super(cause);
    }

}
