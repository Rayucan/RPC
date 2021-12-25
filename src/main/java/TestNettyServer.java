import api.HelloService;
import api.HelloServiceImpl;
import core.provider.ServiceProviderImpl;
import core.provider.ServiceProvider;
import core.netty.server.NettyServer;
import core.serializer.KryoSerializer;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 22:05
 */
public class TestNettyServer {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9977);
        server.setSerializer(new KryoSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
