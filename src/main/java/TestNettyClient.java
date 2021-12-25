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
        RpcClient client = new NettyClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(114514, "this is a message");
        String result = helloService.hello(helloObject);
        System.out.println(result);
    }
}
