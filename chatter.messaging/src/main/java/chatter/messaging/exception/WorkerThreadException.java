package chatter.messaging.exception;

import chatter.common.exception.OrchestrationException;

public class WorkerThreadException extends OrchestrationException {

    public WorkerThreadException(String message) {
        super(message);
    }

    public WorkerThreadException(String message, Exception innerException) {
        super(message, innerException);
    }
}
