package core.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.entity.RpcRequest;
import common.enumeration.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 16:26
 */
public class JsonSerializer implements CommonSerializer{
    
    public static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时发生错误：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        } catch (Exception e) {
            logger.error("反序列化时发生错误：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 由于使用 JSON 序列化和反序列化
     * 无法保证反序列化后仍为原实例类型
     * 需要重新判断
     * @param obj
     * @return
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParameterTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParameterTypes()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes,clazz);
            }
        }
        return rpcRequest;
    }


    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
