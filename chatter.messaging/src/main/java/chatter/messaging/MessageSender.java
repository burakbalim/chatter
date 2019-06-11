package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.event.Event;
import chatter.messaging.event.IEvent;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.MessageCache;
import chatter.messaging.model.MessageEvent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageSender {

    private DistributionCache distributionCache;
    private IEvent event;

    public MessageSender(DistributionCache distributionCache, IEvent event) {
        this.distributionCache = distributionCache;
        this.event = event;
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
        Event<MessageEvent> eventModel = new Event<>();
        eventModel.setTopic(messageCache.getEventTopic());
        eventModel.setEventPayload(eventPayload);
        eventModel.setEventOwner("MessageSender");
        eventModel.setEventId(UUID.randomUUID().toString());
        return eventModel;
    }
}
