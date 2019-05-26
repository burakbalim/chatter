package chatter.messaging.event;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.core.ITopic;

public class IEventImpl implements IEvent {

    private HazelcastInstanceProvider hazelcastInstance = HazelcastInstanceProvider.getInstance();

    private static IEventImpl instance;

    private IEventImpl() {

    }

    public static IEventImpl getInstance() {
        if (instance == null) {
            instance = new IEventImpl();
        }

        return instance;
    }

    @Override
    public void fire(Event ievent) {
        ITopic<Object> topic = hazelcastInstance.getHazelcastInstance().getTopic(ievent.getTopic());
        topic.publish(ievent.getEventPayload());
    }
}
