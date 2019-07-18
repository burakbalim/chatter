package chatter;

import chatter.messaging.IService;
import chatter.messaging.Server;
import chatter.messaging.ServiceTracing;
import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ServiceTracing serviceTracing;

    private ChatterConfCache chatterConfCache;

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    @Autowired
    private ApplicationContext appContext;

    public Main(Server server, ServiceTracing serviceTracing, ChatterConfCache chatterConfCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.server = server;
        this.chatterConfCache = chatterConfCache;
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
        this.serviceTracing = serviceTracing;
    }

    public static void main(String[] args) {

        getConfiguration(args);

        SpringApplication.run(Main.class, args);
    }

    private static void getConfiguration(String[] args) {
        for (String item : args) {
            String prefix = "--config";
            if(item.startsWith(prefix)) {
                String configFile = item.split(prefix + "=")[1];
                System.out.println(configFile);
            }
        }
    }

    @Override
    public void run(String... args) {
        closeIfInterrupt();

        int port = 2002;

        chatterConfCache.setMessageTopicName("messaging-" + port);

        serviceTracing.start();

        registerForEmployeeBean();

        server.build(port);
        server.start();
    }

    public void close() {
        System.exit(200);
    }

    private void closeIfInterrupt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            hazelcastInstanceProvider.close();
        }));
    }

    private void registerForEmployeeBean() {
        String[] beanDefinitionNames = appContext.getBeanDefinitionNames();
        for(String item : beanDefinitionNames) {
            Object bean = appContext.getBean(item);
            if(bean instanceof EventHandler) {
                EventHandler eventHandler = (EventHandler) bean;
                eventHandler.register();
            }
            else if (bean instanceof IService) {
                serviceTracing.addService((IService) bean);
            }
        }
    }
}
