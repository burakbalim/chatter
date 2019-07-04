package chatter;

import chatter.messaging.IService;
import chatter.messaging.Server;
import chatter.messaging.ServiceTracing;
import chatter.messaging.cache.ChatterCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ServiceTracing serviceTracing;

    private ChatterCache chatterCache;

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    @Autowired
    private ApplicationContext appContext;

    public Main(Server server, ServiceTracing serviceTracing, ChatterCache chatterCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.server = server;
        this.chatterCache = chatterCache;
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
        this.serviceTracing = serviceTracing;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        closeIfInterrupt();

        int port = 2001;

        chatterCache.setMessageTopicName("messaging-" + port);

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
