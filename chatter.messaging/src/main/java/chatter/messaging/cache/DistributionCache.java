package chatter.messaging.cache;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.MessageCache;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class DistributionCache implements Serializable {

    private static DistributionCache instance;
    private Map<Long, MessageCache> messageCacheMap;
    //TODO change to cache provider
    private HazelcastInstanceProvider hazelcastInstanceProvider;

    private DistributionCache(HazelcastInstanceProvider hazelcastInstanceProvider) {
        messageCacheMap = hazelcastInstanceProvider.getHazelcastInstance().getMap("userCache");
    }

    public MessageCache get(long id) {
        return messageCacheMap.get(id);
    }

    public synchronized void add(MessageCache messageCache) {
        messageCacheMap.put(messageCache.getUserId(), messageCache);
    }

    public synchronized void pop(long id) {
        messageCacheMap.remove(id);
    }
}
