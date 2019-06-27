package chatter.messaging.event;

import chatter.messaging.exception.MessageBusException;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import java.util.logging.Logger;
import java.util.logging.Level;

public abstract class EventHandler implements MessageListener<Object> {

    private static Logger logger = Logger.getLogger(EventHandler.class.getName());

    protected abstract void handle(Event event) throws MessageBusException;
    public abstract void register();

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    public EventHandler(HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    }

    @Override
    public void onMessage(Message<Object> message) {
        Event messageObject = (Event) message.getMessageObject();

        try {
            handle(messageObject);
        } catch (MessageBusException e) {
            logger.log(Level.WARNING, "Occurred Exception in EventHandler. Exception: {0}", e);
        }
    }

    protected void onRegister(String topic){
        this.hazelcastInstanceProvider.addListener(topic, this);
    }
}
