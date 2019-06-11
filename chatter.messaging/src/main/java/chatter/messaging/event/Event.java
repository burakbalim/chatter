package chatter.messaging.event;

import java.io.Serializable;

public class Event<T extends Serializable> implements Serializable {

    private String topic;
    private String eventId;
    private String eventOwner;
    private T eventPayload;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public T getEventPayload() {
        return eventPayload;
    }

    public void setEventPayload(T eventPayload) {
        this.eventPayload = eventPayload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
