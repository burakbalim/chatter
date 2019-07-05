package chatter.messaging.model;

import java.io.Serializable;
import java.util.Objects;

public class UserEventTopic implements Serializable {

    private Long userId;
    private String eventTopic;

    public UserEventTopic(Long userId, String eventTopic) {
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
        UserEventTopic that = (UserEventTopic) o;
        return Objects.equals(eventTopic, that.eventTopic) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTopic, userId);
    }
}
