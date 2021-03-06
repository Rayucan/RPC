package core.netty.server;

import common.enumeration.RpcError;
import common.exception.RpcException;
import core.codec.CommonDecoder;
import core.codec.CommonEncoder;
import core.hook.ShutdownHook;
import core.loadbalancer.RandomLoadBalancer;
import core.provider.ServiceProvider;
import core.provider.ServiceProviderImpl;
import core.registry.NacosServiceRegistry;
import core.registry.ServiceRegistry;
import core.serializer.CommonSerializer;
import core.serializer.JsonSerializer;
import core.serializer.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.RpcServer;

import java.net.InetSocketAddress;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 16:07
 */
public class NettyServer implements RpcServer {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    
    private final String host;
    private final int port;
    
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    
    private CommonSerializer serializer;
    
    public NettyServer(String host, int port){
        this(host,port,new KryoSerializer());
    }

    public NettyServer(String host, int port, CommonSerializer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = serializer;
        
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();
        
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("??????????????????????????????",e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if (serializer == null){
            logger.error("?????????????????????");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        
        serviceProvider.addServiceProvider(service,serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host, port));
        
        start();
    }
}
