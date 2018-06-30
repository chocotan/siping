package io.loli.siping.exception;

public class CommentRejectException extends RuntimeException {
    public CommentRejectException() {
    }

    public CommentRejectException(String message) {
        super(message);
    }

    public CommentRejectException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentRejectException(Throwable cause) {
        super(cause);
    }

    public CommentRejectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
