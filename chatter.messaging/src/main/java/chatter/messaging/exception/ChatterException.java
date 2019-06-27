package chatter.messaging.exception;

public class ChatterException extends Exception {

    //private ChatterCode chatterCode;
    private Exception innerException;
    private String message;

    public ChatterException(String message, Exception innerException) {
        this.message = message;
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
