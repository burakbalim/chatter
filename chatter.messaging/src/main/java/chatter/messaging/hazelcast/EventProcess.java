package chatter.messaging.hazelcast;

import chatter.messaging.cache.ChatterCache;
import chatter.messaging.bus.MessagingBus;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

public class EventProcess {

    private ChatterCache chatterCache = ChatterCache.getInstance();

    public void start(HazelcastInstance hazelcastInstance) {
        ITopic<Object> topic = hazelcastInstance.getTopic(chatterCache.getMessageTopicName());
        topic.addMessageListener(new MessagingBus());
    }
}
