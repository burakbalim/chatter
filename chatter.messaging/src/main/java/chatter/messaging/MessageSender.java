package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.event.Event;
import chatter.messaging.event.IEvent;
import chatter.messaging.event.IEventImpl;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.MessageCache;
import chatter.messaging.model.MessageEvent;

public class MessageSender {

    private static MessageSender instance;
    private DistributionCache distributionCache = DistributionCache.getInstance();
    private IEvent event = IEventImpl.getInstance();

    private MessageSender() {

    }

    public static synchronized MessageSender getInstance() {
        if (instance == null) {
            instance = new MessageSender();
        }
        return instance;
    }

    void send(CommunicationModel communicationModel) {
        String message = communicationModel.getMessage();

        communicationModel.getUserIds().stream().map(userId ->
                distributionCache.get(userId)).forEach(messageCache -> writeMessage(messageCache, message));

        writeMessage(distributionCache.get(communicationModel.getSenderUser()), message);
    }

    private void writeMessage(MessageCache connectedUserModel, String message) {
        event.fire(getEvent(connectedUserModel, message));
    }

    private Event getEvent(MessageCache messageCache, String message) {
        MessageEvent eventPayload = new MessageEvent();
        eventPayload.setUserId(messageCache.getUserId());
        eventPayload.setMessage(message);
        Event eventModel = new Event();
        eventModel.setTopic(messageCache.getEventTopic());
        eventModel.setEventPayload(eventPayload);
        return eventModel;
    }
}
