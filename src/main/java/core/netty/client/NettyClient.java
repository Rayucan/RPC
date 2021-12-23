package core.netty.client;

import core.RpcClient;
import core.codec.CommonDecoder;
import core.codec.CommonEncoder;
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

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 21:34
 */
public class NettyClient implements RpcClient {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    
    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    
    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }
    
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host,port).sync();
            logger.info("客户端连接到服务器{}：{}",host,port);
            Channel channel = future.channel();
            
            if (channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()){
                        logger.info(String.format("客户端发送消息：%s", rpcRequest.toString()));
                    }else {
                        logger.error("发送消息时发生错误：",future1.cause());
                    }
                });
            }
            channel.closeFuture().sync();
            
            //通过 AttributeKey 以阻塞的方式获取返回的结果
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            return rpcResponse.getData();
        } catch (InterruptedException e) {
            logger.error("发送消息时发生错误：",e);
        }
        return null;
    }
}
