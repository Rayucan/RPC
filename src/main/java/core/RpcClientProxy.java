package core;

import common.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:53
 */

public class RpcClientProxy implements InvocationHandler {
    public static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    
    private final RpcClient rpcClient;
    
    public RpcClientProxy(RpcClient rpcClient){
        this.rpcClient = rpcClient;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        RpcRequest rpcRequest = RpcRequest.builder()
////                .interfaceName(method.getDeclaringClass().getName())
////                .methodName(method.getName())
////                .parameters(args)
////                .parameterTypes(method.getParameterTypes())
////                .build();
////
////        SocketClient rpcClient = new SocketClient(host,port);
////        
////        return ((RpcResponse)rpcClient.sendRequest(rpcRequest)).getData();
        
        logger.info("调用方法：{}#{}",
                method.getDeclaringClass().getName(),
                method.getName());
        
        RpcRequest rpcRequest = new RpcRequest(
                method.getDeclaringClass().getName(), 
                method.getName(), 
                args, 
                method.getParameterTypes());
        
        return rpcClient.sendRequest(rpcRequest);
    }
}
