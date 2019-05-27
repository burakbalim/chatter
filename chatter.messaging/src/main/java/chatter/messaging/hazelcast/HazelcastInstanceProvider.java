package chatter.messaging.hazelcast;

import chatter.messaging.bus.MessagingBus;
import chatter.messaging.cache.ChatterCache;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

public class HazelcastInstanceProvider {

    private static HazelcastInstanceProvider instance;
    private ChatterCache chatterCache = ChatterCache.getInstance();
    private HazelcastInstance hazelcastInstance;

    private HazelcastInstanceProvider() {

    }

    public static synchronized HazelcastInstanceProvider  getInstance() {
        if (instance == null) {
            instance = new HazelcastInstanceProvider();
        }
        return instance;
    }

    public void start() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        prepareEventMessageListener();
    }

    public void stop(){
        hazelcastInstance.shutdown();
    }

    private void prepareEventMessageListener() {
        ITopic<Object> topic = hazelcastInstance.getTopic(chatterCache.getMessageTopicName());
        topic.addMessageListener(new MessagingBus());
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
