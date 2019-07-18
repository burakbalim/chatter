package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.event.Event;
import chatter.messaging.event.IEvent;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.UserEventTopic;
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

        communicationModel.getSentUserIds().stream().map(userId ->
                distributionCache.get(userId)).forEach(userEventTopic -> writeMessage(userEventTopic, message));

        writeMessage(distributionCache.get(communicationModel.getSenderUserId()), message);
    }

    private void writeMessage(UserEventTopic userEventTopic, String message) {
        event.fire(getEvent(userEventTopic, message));
    }

    private Event getEvent(UserEventTopic userEventTopic, String message) {
        MessageEvent eventPayload = new MessageEvent();
        eventPayload.setUserId(userEventTopic.getUserId());
        eventPayload.setMessage(message);

        Event<MessageEvent> eventModel = new Event<>();
        eventModel.setTopic(userEventTopic.getEventTopic());
        eventModel.setEventPayload(eventPayload);
        eventModel.setEventOwner(getClass().getName());
        eventModel.setEventId(UUID.randomUUID().toString());
        return eventModel;
    }
}
