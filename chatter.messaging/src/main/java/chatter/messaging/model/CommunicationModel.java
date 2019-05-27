package chatter.messaging.model;

import java.io.Serializable;
import java.util.List;

public class CommunicationModel implements Serializable {

    private String message;
    private List<Long> userIds;
    private Long senderUser;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(Long senderUser) {
        this.senderUser = senderUser;
    }
}
