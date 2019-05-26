package chatter.messaging.cache;

public class ChatterCache {

    private static ChatterCache instance;
    private String messageTopicName;

    private ChatterCache() {

    }

    public static synchronized ChatterCache getInstance() {
        if (instance == null) {
            instance = new ChatterCache();
        }
        return instance;
    }

    public String getMessageTopicName() {
        return messageTopicName;
    }

    public void setMessageTopicName(String messageTopicName) {
        this.messageTopicName = messageTopicName;
    }
}
