package chatter.messaging.bus;

import chatter.messaging.OnlineUser;
import chatter.messaging.cache.ChatterCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.event.EventPayload;
import chatter.messaging.exception.ServerException;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.ConnectedUserModel;
import chatter.messaging.model.MessageEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class MessagingBus extends EventHandler {

    private OnlineUser onlineUser;
    private ChatterCache chatterCache;

    public MessagingBus(OnlineUser onlineUser, ChatterCache chatterCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        super(hazelcastInstanceProvider);
        this.onlineUser = onlineUser;
        this.chatterCache = chatterCache;
    }

    @Override
    public void handle(EventPayload event) {
        MessageEvent  messageEvent = (MessageEvent) event;
        ConnectedUserModel connectedUserModel = onlineUser.get(messageEvent.getUserId());

        try {
            Socket client = connectedUserModel.getClient();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(messageEvent.getMessage());
        } catch (IOException exception) {
            throw new ServerException("Message sender exception for model:" + connectedUserModel, exception);
        }
    }

    @Override
    public void register() {
        super.onRegister(chatterCache.getMessageTopicName());
    }
}
