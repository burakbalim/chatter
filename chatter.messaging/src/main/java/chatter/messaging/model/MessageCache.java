package chatter.messaging.model;

import java.io.Serializable;
import java.util.Objects;

public class MessageCache implements Serializable {

    private String eventTopic;
    private Long userId;

    public MessageCache(String eventTopic, Long userId) {
        this.eventTopic = eventTopic;
        this.userId = userId;
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageCache that = (MessageCache) o;
        return Objects.equals(eventTopic, that.eventTopic) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTopic, userId);
    }
}
