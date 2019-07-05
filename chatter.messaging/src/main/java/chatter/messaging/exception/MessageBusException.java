package chatter.messaging.exception;

import chatter.common.exception.ChatterException;

public class MessageBusException extends ChatterException {

    public MessageBusException(String message, Exception innerException) {
        super(message, innerException);
    }
}
