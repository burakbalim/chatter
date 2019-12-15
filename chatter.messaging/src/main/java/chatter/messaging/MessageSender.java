package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.event.Event;
import chatter.messaging.event.IEvent;
import chatter.messaging.model.Communication;
import chatter.messaging.model.UserReceiverTopic;
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

    void send(Communication communication) {
        String message = communication.getMessage();

        communication.getSentUserIds().stream().map(userId -> distributionCache.get(userId)).forEach(userReceiverTopic -> fire(userReceiverTopic, message));

        UserReceiverTopic receiverTopic = distributionCache.get(communication.getSenderUserId());
        fire(receiverTopic, message);
    }

    private void fire(UserReceiverTopic userReceiverTopic, String message) {
        event.fire(getEvent(userReceiverTopic, message));
    }

    private Event getEvent(UserReceiverTopic userReceiverTopic, String message) {
        MessageEvent eventPayload = new MessageEvent();
        eventPayload.setUserId(userReceiverTopic.getUserId());
        eventPayload.setMessage(message);

        Event<MessageEvent> eventModel = new Event<>();
        eventModel.setTopic(userReceiverTopic.getMessageBusName());
        eventModel.setEventPayload(eventPayload);
        eventModel.setEventOwner(getClass().getName());
        eventModel.setEventId(UUID.randomUUID().toString());
        return eventModel;
    }
}
