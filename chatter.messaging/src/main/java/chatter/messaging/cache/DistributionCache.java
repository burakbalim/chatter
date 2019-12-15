package chatter.messaging.cache;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.UserReceiverTopic;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class DistributionCache implements Serializable {

    private Map<Long, UserReceiverTopic> messageCacheMap;

    public DistributionCache(HazelcastInstanceProvider hazelcastInstanceProvider) {
        messageCacheMap = hazelcastInstanceProvider.getHazelcastInstance().getMap("userCache");
    }

    public UserReceiverTopic get(long id) {
        return messageCacheMap.get(id);
    }

    public synchronized void put(long id, UserReceiverTopic userReceiverTopic) {
        messageCacheMap.put(id, userReceiverTopic);
    }

    public synchronized void pop(long id) {
        messageCacheMap.remove(id);
    }
}
