package chatter.messaging.exception;

import chatter.common.exception.OrchestrationException;

public class ConnectionManagerException extends OrchestrationException {

    public ConnectionManagerException(String message) {
        super(message);
    }

    public ConnectionManagerException(String message, Exception innerException) {
        super(message, innerException);
    }

}
