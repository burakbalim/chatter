package chatter.messaging.event;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import org.springframework.stereotype.Component;

public abstract class EventHandler implements MessageListener<Object> {

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    public EventHandler(HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    }

    public abstract void handle(EventPayload event);
    public abstract void register();

    @Override
    public void onMessage(Message<Object> message) {
        EventPayload messageObject = (EventPayload) message.getMessageObject();

        handle(messageObject);
    }

    protected void onRegister(String topic){
        this.hazelcastInstanceProvider.addListener(topic, this);
    }

}
