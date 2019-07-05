package chatter.messaging.cache;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.UserEventTopic;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class DistributionCache implements Serializable {

    private Map<Long, UserEventTopic> messageCacheMap;

    public DistributionCache(HazelcastInstanceProvider hazelcastInstanceProvider) {
        messageCacheMap = hazelcastInstanceProvider.getHazelcastInstance().getMap("userCache");
    }

    public UserEventTopic get(long id) {
        return messageCacheMap.get(id);
    }

    public synchronized void add(long id, UserEventTopic userEventTopic) {
        messageCacheMap.put(id, userEventTopic);
    }

    public synchronized void pop(long id) {
        messageCacheMap.remove(id);
    }
}
