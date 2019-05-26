package chatter.messaging.exception;

public class OrchestractionException extends RuntimeException {

    private Exception innerException;
    private String message;

    public OrchestractionException(String message) {
        super(message);
    }

    public OrchestractionException(String message, Exception innerException) {
        super(message);
        this.message = message;
        this.innerException = innerException;
    }

    @Override
    public String getMessage() {
        if (innerException != null) {
            return message + " Inner Exception " + innerException;
        }

        return message;
    }
}
