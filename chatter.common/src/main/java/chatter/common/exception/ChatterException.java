package chatter.common.exception;

import java.text.MessageFormat;

public class ChatterException extends Exception {

    private Exception innerException;
    private String message;

    public ChatterException(String message, Exception innerException) {
        super(message);
        this.message = message;
        this.innerException = innerException;
    }

    public ChatterException(String message, Exception innerException, Object... param) {
        this.message = MessageFormat.format(message, param);
        this.innerException = innerException;
        //TEST - abc - test - test -test - aaaa - test - deneme
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
