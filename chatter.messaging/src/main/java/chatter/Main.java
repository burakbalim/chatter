package chatter;

import chatter.common.exception.ChatterException;
import chatter.common.exception.OrchestrationException;
import chatter.common.util.ChatterUtil;
import chatter.common.util.ConfigurationHelper;
import chatter.messaging.IService;
import chatter.messaging.Server;
import chatter.messaging.ServiceState;
import chatter.messaging.ServiceTracing;
import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.exception.ServerException;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.common.model.ChatterConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Objects;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ServiceTracing serviceTracing;

    private ChatterConfCache chatterConfCache;

    private ApplicationContext appContext;

    private ConfigurationHelper configurationHelper;

    public Main(ApplicationContext appContext,
                Server server, ServiceTracing serviceTracing,
                ChatterConfCache chatterConfCache, ConfigurationHelper configurationHelper) {
        this.server = server;
        this.chatterConfCache = chatterConfCache;
        this.serviceTracing = serviceTracing;
        this.appContext = appContext;
        this.configurationHelper = configurationHelper;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        closeIfInterrupt();

        populateConfigurationCache(args);

        registerForEmployeeBean();

        serviceTracing.start();
        server.build(chatterConfCache.getChatterConfiguration().getPort());
        server.start();
    }

    private void registerForEmployeeBean() {
        String[] beanDefinitionNames = appContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).map(item -> appContext.getBean(item)).forEach(bean -> {
            if (bean instanceof EventHandler) {
                EventHandler eventHandler = (EventHandler) bean;
                eventHandler.register();
            } else if (bean instanceof IService) {
                serviceTracing.addService((IService) bean);
            }
        });
    }

    private void populateConfigurationCache(String... args) {
        ChatterConfiguration configuration;
        try {
            configuration = configurationHelper.getConfiguration(args);
            chatterConfCache.setChatterConfiguration(configuration);
            chatterConfCache.setMessageTopicName("messaging-" + configuration.getPort());
        } catch (ChatterException e) {
            throw new ServerException("Configuration read error", e);
        }
    }

    private void closeIfInterrupt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server.state() == ServiceState.RUNNING) {
                server.stop();
            }
            serviceTracing.stop();
        }));
    }

}
