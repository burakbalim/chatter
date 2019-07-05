package chatter.common.exception;

public class OrchestrationException extends RuntimeException {

    private Exception innerException;
    private String message;

    public OrchestrationException(String message) {
        super(message);
    }

    public OrchestrationException(String message, Exception innerException) {
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
