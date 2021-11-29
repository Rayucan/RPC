package core.codec;

import core.serializer.CommonSerializer;
import common.entity.RpcRequest;
import common.enumeration.PackageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Rayucan
 * @description 自定义编码器
 * @date Created on 2021/11/29 16:17
 */
public class CommonEncoder extends MessageToByteEncoder {
    
    public static final int MAGIC_NUMBER = 0xCAFEBABE;
    
    private final CommonSerializer serializer;
    
    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }
    
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);

        if (msg instanceof RpcRequest){
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        }else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
