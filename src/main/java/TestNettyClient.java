import api.HelloObject;
import api.HelloService;
import core.netty.client.NettyClient;
import core.RpcClient;
import core.RpcClientProxy;
import core.serializer.KryoSerializer;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 22:07
 */
public class TestNettyClient {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(114514, "THIS IS A MESSAGE");
        String result = helloService.hello(object);

        System.out.println(result);
    }
}
