package chatter.common.exception;

import java.text.MessageFormat;

public class ChatterException extends Exception {

    private Throwable innerException;
    private String message;

    public ChatterException(String message, Throwable innerException) {
        super(message, innerException);
        this.message = message;
        this.innerException = innerException;
    }

    public ChatterException(String message, Throwable innerException, Object... param) {
        this.message = MessageFormat.format(message, param);
        this.innerException = innerException;
    }

    public ChatterException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        if(innerException != null) {
            return message + " Inner Exception " + innerException;
        }
        return message;
    }
}
