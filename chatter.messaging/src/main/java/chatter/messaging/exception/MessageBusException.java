package chatter.messaging.exception;

public class MessageBusException extends ChatterException {

    public MessageBusException(Exception innerException, String message) {
        super(message,innerException);
    }
}
