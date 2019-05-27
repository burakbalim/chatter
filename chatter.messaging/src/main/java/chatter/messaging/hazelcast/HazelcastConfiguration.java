package chatter.messaging.hazelcast;

import com.hazelcast.config.Config;

public class HazelcastConfiguration {

    private Config config;

    public void setConfig(Config config) {
        this.config = config;
    }
}
