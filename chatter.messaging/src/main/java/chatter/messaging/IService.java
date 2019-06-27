package chatter.messaging;

public interface IService {

    public void start();

    public void stop();

    public ServiceState state();

    public String getName();
}
