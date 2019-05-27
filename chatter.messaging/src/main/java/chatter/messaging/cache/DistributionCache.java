package chatter.messaging.cache;

import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.MessageCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DistributionCache implements Serializable {

    private static DistributionCache instance;
    private Map<Long, MessageCache> messageCacheMap;

    private DistributionCache(String cacheType) {
        if(cacheType.equals("Hazelcast")) {
            HazelcastInstanceProvider hazelcastInstanceProvider = HazelcastInstanceProvider.getInstance();
            messageCacheMap = hazelcastInstanceProvider.getHazelcastInstance().getMap("userCache");
        }
        else {
            messageCacheMap = new HashMap<>();
        }
    }

    public static synchronized DistributionCache getInstance() {
        if (instance == null) {
            //TODO get cache on another configuration
            instance = new DistributionCache("Hazelcast");
        }

        return instance;
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
