package chatter.messaging.event;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public abstract class EventHandler implements MessageListener<Object> {

    protected abstract void handle(Event event);
    public abstract void register();

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    public EventHandler(HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    }

    @Override
    public void onMessage(Message<Object> message) {
        Event messageObject = (Event) message.getMessageObject();

        handle(messageObject);
    }

    protected void onRegister(String topic){
        this.hazelcastInstanceProvider.addListener(topic, this);
    }
}
