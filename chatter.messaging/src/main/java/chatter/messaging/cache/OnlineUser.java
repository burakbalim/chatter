package chatter.messaging.cache;

import chatter.messaging.model.ConnectedUser;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUser {

    private ConcurrentHashMap<Long, ConnectedUser> concurrentHashMap = new ConcurrentHashMap<>();

    public ConnectedUser get(long id) {
        return this.concurrentHashMap.get(id);
    }

    public void put(long id, ConnectedUser connectedUser) {
        this.concurrentHashMap.put(id, connectedUser);
    }

    public void pop(long id) {
        this.concurrentHashMap.remove(id);
    }
}
