package chatter.messaging.cache;

import chatter.messaging.model.ConnectedUserModel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUser {

    private ConcurrentHashMap<Long, ConnectedUserModel> concurrentHashMap = new ConcurrentHashMap<>();

    public ConnectedUserModel get(long id) {
        return this.concurrentHashMap.get(id);
    }

    public void put(long id, ConnectedUserModel connectedUserModel) {
        this.concurrentHashMap.put(id, connectedUserModel);
    }

    public void pop(long id) {
        this.concurrentHashMap.remove(id);
    }
}
