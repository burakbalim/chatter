package chatter.messaging;

import chatter.messaging.model.ConnectedUserModel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUser {

    private ConcurrentHashMap<Long, ConnectedUserModel> concurrentHashMap = new ConcurrentHashMap<>();

    public ConnectedUserModel get(long id) {
        return this.concurrentHashMap.get(id);
    }

    public synchronized void add(long id, ConnectedUserModel connectedUserModel) {
        this.concurrentHashMap.put(id, connectedUserModel);
    }

    public synchronized void pop(long id) {
        this.concurrentHashMap.remove(id);
    }
}
