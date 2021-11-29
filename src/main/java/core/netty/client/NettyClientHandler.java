package core.netty.client;

import common.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 22:02
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    public static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
            logger.info(String.format("客户端接收到消息：%s", msg));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("调用过程时发生错误：");
        cause.printStackTrace();
        ctx.close();
    }
}
