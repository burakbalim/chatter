package chatter.messaging.exception;

import chatter.common.exception.OrchestrationException;

public class ServerException extends OrchestrationException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Exception innerException) {
        super(message, innerException);
    }

}
