package core;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import core.netty.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RpcMessageChecker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:53
 */

public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    
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
        logger.info("调用方法：{}#{}",
                method.getDeclaringClass().getName(),
                method.getName());
        
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),
                method.getDeclaringClass().getName(),
                method.getName(),
                args,
                method.getParameterTypes());

        RpcResponse rpcResponse = null;
        
        if (rpcClient instanceof NettyClient){
            CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
        
            try {
                rpcResponse = completableFuture.get();
            }catch (Exception e){
                logger.error("RpcClientProxy:请求发送失败",e);
                return null;
            }
        }

        RpcMessageChecker.check(rpcRequest,rpcResponse);
        
        return rpcResponse.getData();
    }
}
