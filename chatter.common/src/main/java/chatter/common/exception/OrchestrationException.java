package chatter.common.exception;

public class OrchestrationException extends RuntimeException {

    private String message;

    public OrchestrationException(String message) {
        super(message);
    }

    public OrchestrationException(String message, Throwable innerException) {
        super(message, innerException);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message + " Inner Exception " + super.getCause();
    }
}
