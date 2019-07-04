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

        communicationModel.getUserIds().stream().map(userId ->
                distributionCache.get(userId)).forEach(userEventTopicCache -> writeMessage(userEventTopicCache, message));

        writeMessage(distributionCache.get(communicationModel.getSenderUser()), message);
    }

    private void writeMessage(UserEventTopic connectedUserModel, String message) {
        event.fire(getEvent(connectedUserModel, message));
    }

    private Event getEvent(UserEventTopic userEventTopicCache, String message) {
        MessageEvent eventPayload = new MessageEvent();
        eventPayload.setUserId(userEventTopicCache.getUserId());
        eventPayload.setMessage(message);
        Event<MessageEvent> eventModel = new Event<>();
        eventModel.setTopic(userEventTopicCache.getEventTopic());
        eventModel.setEventPayload(eventPayload);
        eventModel.setEventOwner("MessageSender");
        eventModel.setEventId(UUID.randomUUID().toString());
        return eventModel;
    }
}
