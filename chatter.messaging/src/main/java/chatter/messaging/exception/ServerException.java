package chatter.messaging.exception;

public class ServerException extends OrchestractionException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Exception innerException) {
        super(message, innerException);
    }

}
