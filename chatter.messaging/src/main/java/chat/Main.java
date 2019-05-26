package chat;

import chatter.messaging.cache.ChatterCache;
import chatter.messaging.Server;
import chatter.messaging.hazelcast.HazelcastConfiguration;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.config.Config;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ChatterCache chatterCache = ChatterCache.getInstance();
        chatterCache.setMessageTopicName("messaging-2001");

        HazelcastConfiguration hazelcastCfg = new HazelcastConfiguration();
        Config config = new Config();
        hazelcastCfg.setConfig(config);
        hazelcastCfg.setMessagingTopic("messaging-2001");
        HazelcastInstanceProvider hazelcastInstanceProvider = HazelcastInstanceProvider.getInstance();
        hazelcastInstanceProvider.start(hazelcastCfg);

        Server server = new Server();
        try {
            server.build(2001);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
