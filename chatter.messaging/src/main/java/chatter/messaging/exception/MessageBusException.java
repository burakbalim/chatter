package chatter.messaging.exception;

public class MessageBusException extends ChatterException {

    public MessageBusException(String message, Exception innerException) {
        super(message, innerException);
    }
}
