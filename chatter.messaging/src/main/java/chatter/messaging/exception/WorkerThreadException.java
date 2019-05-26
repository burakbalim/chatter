package chatter.messaging.exception;

public class WorkerThreadException extends OrchestractionException {

    public WorkerThreadException(String message) {
        super(message);
    }

    public WorkerThreadException(String message, Exception innerException) {
        super(message, innerException);
    }
}
