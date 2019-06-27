package chatter.messaging;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class ServiceTracing {

    private Logger logger = Logger.getLogger(ServiceTracing.class.getName());

    private List<IService> applicationMainService = new ArrayList<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public void addService(IService iService) {
        this.applicationMainService.add(iService);
    }

    public void start() {
        process();
    }

    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(checkApplicationService(), 20, 20000, TimeUnit.SECONDS);
    }

    private Runnable checkApplicationService() {
        return () -> {
            applicationMainService.forEach(service -> {
                if (service.state().equals(ServiceState.STOPPED)) {
                    logger.log(Level.INFO, "Service is not running. Trying to start. Service: {0}", service.getName());
                    service.start();
                }
                else {
                    logger.log(Level.SEVERE, "Service is running. Service: {0}", service.getName());
                }
            });
        };
    }
}
