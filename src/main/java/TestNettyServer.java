import api.HelloService;
import api.HelloServiceImpl;
import core.registry.DefaultServiceRegistry;
import core.registry.ServiceRegistry;
import core.netty.server.NettyServer;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 22:05
 */
public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
