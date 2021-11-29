import api.HelloObject;
import api.HelloService;
import core.netty.client.NettyClient;
import core.RpcClient;
import core.RpcClientProxy;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 22:07
 */
public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1",9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(114514, "哼哼啊啊啊啊");
        String result = helloService.hello(object);

        System.out.println(result);
    }
}
