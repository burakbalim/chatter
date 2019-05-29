package chatter.messaging.hazelcast;

import chatter.messaging.bus.MessagingBus;
import chatter.messaging.cache.ChatterCache;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HazelcastInstanceProvider {

    private static HazelcastInstanceProvider instance;
    private ChatterCache chatterCache;
    private HazelcastInstance hazelcastInstance;
    private MessagingBus messagingBus;

    public HazelcastInstanceProvider(ChatterCache chatterCache, MessagingBus messagingBus) {
        this.chatterCache = chatterCache;
        this.messagingBus = messagingBus;
    }

    @PostConstruct
    public void init() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public void close(){
        hazelcastInstance.shutdown();
    }

    public void prepareEventMessageListener() {
        ITopic<Object> topic = hazelcastInstance.getTopic(chatterCache.getMessageTopicName());

        topic.addMessageListener(messagingBus);
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
