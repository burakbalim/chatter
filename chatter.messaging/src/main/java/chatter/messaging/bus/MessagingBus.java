package chatter.messaging.bus;

import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.event.Event;
import chatter.messaging.event.EventHandler;
import chatter.messaging.exception.MessageBusException;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.ConnectedUser;
import chatter.messaging.model.MessageEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class MessagingBus extends EventHandler {

    private OnlineUser onlineUser;
    private ChatterConfCache chatterConfCache;

    public MessagingBus(OnlineUser onlineUser, ChatterConfCache chatterConfCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        super(hazelcastInstanceProvider);
        this.onlineUser = onlineUser;
        this.chatterConfCache = chatterConfCache;
    }

    @Override
    public void handle(Event event) throws MessageBusException {
        MessageEvent messageEvent = (MessageEvent) event.getEventPayload();
        ConnectedUser connectedUser = onlineUser.get(messageEvent.getUserId());

        if (connectedUser != null) {
            sendMessage(messageEvent, connectedUser);
        } else {
            putMessageQueueToSaving();
        }
    }

    private void sendMessage(MessageEvent messageEvent, ConnectedUser connectedUser) throws MessageBusException {
        try {
            Socket client = connectedUser.getClient();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(messageEvent.getMessage());
        } catch (IOException exception) {
            throw new MessageBusException("Exception while write message:" + connectedUser, exception);
        }
    }

    private void putMessageQueueToSaving() {

    }

    @Override
    public void register() {
        super.onRegister(chatterConfCache.getMessageTopicName());
    }
}
