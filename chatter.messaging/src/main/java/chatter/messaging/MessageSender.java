package chatter.messaging;

import chatter.messaging.cache.ChatterCache;
import chatter.messaging.event.Event;
import chatter.messaging.event.IEvent;
import chatter.messaging.event.IEventImpl;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.ConnectedUserModel;
import chatter.messaging.model.MessageEvent;

public class MessageSender {

    private static MessageSender instance;
    private OnlineUser onlineUser = OnlineUser.getInstance();
    private ChatterCache chatterCache = ChatterCache.getInstance();
    private IEvent event = IEventImpl.getInstance();

    private MessageSender() {

    }

    public static synchronized MessageSender getInstance() {
        if (instance == null) {
            instance = new MessageSender();
        }
        return instance;
    }

    public void send(CommunicationModel communicationModel) {
        String message = communicationModel.getMessage();

        communicationModel.getUsers().stream().map(user -> onlineUser.get(user)).forEach(connectedUserModel -> {
            writeMessage(connectedUserModel, message);
        });

        writeMessage(onlineUser.get(communicationModel.getSenderUser()), message);
    }

    private void writeMessage(ConnectedUserModel connectedUserModel, String message) {
        event.fire(getEvent(connectedUserModel, message));
    }

    private Event getEvent(ConnectedUserModel connectedUserModel, String message) {
        MessageEvent eventPayload = new MessageEvent();
        eventPayload.setUserId(connectedUserModel.getUser().getId());
        eventPayload.setMessage(message);
        Event eventModel = new Event();
        eventModel.setTopic(chatterCache.getMessageTopicName());
        eventModel.setEventPayload(eventPayload);
        return eventModel;
    }
}
