package core.netty.client;

import core.RpcClient;
import core.codec.CommonDecoder;
import core.codec.CommonEncoder;
import core.registry.NacosServiceRegistry;
import core.registry.ServiceRegistry;
import core.serializer.CommonSerializer;
import core.serializer.JsonSerializer;
import common.entity.RpcRequest;
import common.entity.RpcResponse;
import core.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RpcMessageChecker;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 21:34
 */
public class NettyClient implements RpcClient {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    
    private static final Bootstrap bootstrap;
    
    private final ServiceRegistry serviceRegistry;
    
    private CommonSerializer serializer;
    
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }
    
    public NettyClient(){
        this.serviceRegistry = new NacosServiceRegistry();
    }
    
    
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null){
            logger.error("未设置序列化器");
        }

        AtomicReference<Object> result = new AtomicReference<>(null);
        
        try {
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            
            if (channel.isActive()){
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()){
                        logger.info(String.format("客户端发送消息:%s",rpcRequest.toString()));
                    }else {
                        logger.info("发送消息时发生错误：",future.cause());
                    }
                });
                
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            }else {
                System.exit(0);
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时发生错误：",e);
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
