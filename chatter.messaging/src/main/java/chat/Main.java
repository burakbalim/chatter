package chat;

import chatter.messaging.Server;
import chatter.messaging.cache.ChatterCache;
import chatter.messaging.hazelcast.HazelcastConfiguration;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.config.Config;

public class Main {

    public static void main(String[] args) {
        int socket = 2001;

        ChatterCache chatterCache = ChatterCache.getInstance();
        chatterCache.setMessageTopicName("messaging-" + socket);

        HazelcastConfiguration hazelcastCfg = new HazelcastConfiguration();
        Config config = new Config();
        hazelcastCfg.setConfig(config);

        HazelcastInstanceProvider hazelcastInstanceProvider = HazelcastInstanceProvider.getInstance();

        hazelcastInstanceProvider.start();

        Server server = new Server();


        server.build(socket);
        server.start();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            hazelcastInstanceProvider.stop();
        }));
    }
}
