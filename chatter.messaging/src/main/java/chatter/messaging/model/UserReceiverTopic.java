package chatter.messaging.model;

import java.io.Serializable;
import java.util.Objects;

public class UserReceiverTopic implements Serializable {

    private Long userId;
    private String messageBusName;

    public UserReceiverTopic(Long userId, String messageBusName) {
        this.messageBusName = messageBusName;
        this.userId = userId;
    }

    public String getMessageBusName() {
        return messageBusName;
    }

    public void setMessageBusName(String messageBusName) {
        this.messageBusName = messageBusName;
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
        UserReceiverTopic that = (UserReceiverTopic) o;
        return Objects.equals(messageBusName, that.messageBusName) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageBusName, userId);
    }
}
