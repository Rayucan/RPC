package core.netty.client;

import core.RpcClient;
import core.codec.CommonDecoder;
import core.codec.CommonEncoder;
import core.loadbalancer.RandomLoadBalancer;
import core.registry.NacosServiceDiscovery;
import core.registry.NacosServiceRegistry;
import core.registry.ServiceDiscovery;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 21:34
 */
public class NettyClient implements RpcClient {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup group;
    
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;
    
    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }
    
    public NettyClient(){
        this.serviceDiscovery = new NacosServiceDiscovery(new RandomLoadBalancer());
        this.serializer = CommonSerializer.getByCode(0);
    }
    
    
    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null){
            logger.error("未设置序列化器");
        }

        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            
            if (!channel.isActive()){
                group.shutdownGracefully();
                return null;
            }
            
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()){
                    logger.info(String.format("客户端发送消息:%s",rpcRequest.toString()));
                }else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    logger.info("发送消息时发生错误:",future1.cause());
                }
            });
            
        } catch (Exception e) {
            logger.error("发送消息时发生错误：",e);
        }
        return resultFuture;
    }
}
