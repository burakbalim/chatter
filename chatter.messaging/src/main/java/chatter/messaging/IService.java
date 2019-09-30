package chatter.messaging;

public interface IService {

    void start();

    void stop();

    ServiceState state();

    String getName();
}
