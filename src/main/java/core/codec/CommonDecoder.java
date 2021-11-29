package core.codec;

import core.serializer.CommonSerializer;
import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enumeration.PackageType;
import common.enumeration.RpcError;
import common.exception.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Rayucan
 * @description 自定义解码器
 * @date Created on 2021/11/29 16:19
 */
public class CommonDecoder extends ReplayingDecoder {
    public static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    public static final int MAGIC_NUMBER = 0xCAFEBABE;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //解码魔数
        int magic = in.readInt();
        
        if (magic != MAGIC_NUMBER){
            logger.error("不识别的协议包：{}",magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        //解码调用类型(请求、响应)
        int packageCode = in.readInt();
        
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
          logger.error("不识别的数据包：{}",packageCode);
          throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        //解码序列化器，保证服务端客户端统一标准
        int serializerCode = in.readInt();
        
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null){
            logger.error("不识别的反序列化器：{}",serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        //解码数据长度，设置这个字段的目的是防止粘包
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        //解码实际数据
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}
