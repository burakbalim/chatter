package chatter.messaging.event;

public class Event {

    private String topic;
    private EventPayload eventPayload;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public EventPayload getEventPayload() {
        return eventPayload;
    }

    public void setEventPayload(EventPayload eventPayload) {
        this.eventPayload = eventPayload;
    }
}
