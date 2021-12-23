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
     * 
     * 详细解释：
     * 在 RpcRequest 反序列化时，由于其中有一个字段是 Object 数组，在反序列化时序列化器会根据字段类型进行反序列化，
     * 而 Object 就是一个十分模糊的类型，会出现反序列化失败的现象，
     * 这时就需要 RpcRequest 中的另一个字段 ParamTypes 来获取到 Object 数组中的每个实例的实际类，辅助反序列化，
     * 这就是 handleRequest() 方法的作用。
     * 
     * 根本原因：
     * 上面提到的这种情况不会在其他序列化方式中出现，因为其他序列化方式是转换成字节数组，会记录对象的信息，
     * 而 JSON 方式本质上只是转换成 JSON 字符串，会丢失对象的类型信息。
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
