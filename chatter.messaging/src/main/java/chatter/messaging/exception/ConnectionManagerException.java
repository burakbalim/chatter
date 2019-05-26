package chatter.messaging.exception;

public class ConnectionManagerException extends OrchestractionException {

    public ConnectionManagerException(String message) {
        super(message);
    }

    public ConnectionManagerException(String message, Exception innerException) {
        super(message, innerException);
    }

}
