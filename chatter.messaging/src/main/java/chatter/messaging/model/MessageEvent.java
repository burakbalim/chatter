package chatter.messaging.model;

import chatter.messaging.event.EventPayload;

public class MessageEvent extends EventPayload {

    private Long userId;
    private String message;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
