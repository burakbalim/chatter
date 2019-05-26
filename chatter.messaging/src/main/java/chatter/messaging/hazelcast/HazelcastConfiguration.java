package chatter.messaging.hazelcast;

import com.hazelcast.config.Config;

public class HazelcastConfiguration {

    private Config config;
    private String messagingTopic;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getMessagingTopic() {
        return messagingTopic;
    }

    public void setMessagingTopic(String messagingTopic) {
        this.messagingTopic = messagingTopic;
    }
}
