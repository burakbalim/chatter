package chatter.messaging.event;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.core.ITopic;
import org.springframework.stereotype.Component;

@Component
public class IEventImpl implements IEvent {

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    public IEventImpl(HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    }

    @Override
    public void fire(Event ievent) {
        ITopic<Object> topic = hazelcastInstanceProvider.getHazelcastInstance().getTopic(ievent.getTopic());
        topic.publish(ievent.getEventPayload());
    }
}
