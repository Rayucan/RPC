package core.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enumeration.SerializerCode;
import common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/12/23 19:33
 * 
 * Kryo 在序列化时，先创建一个 OutputStream 流，使用 writeObject() 将其写入 Output 中
 * 最后调用 output.toByte() 即可获得对象的 byte[] 数组
 * 
 * 反序列化时，从 Input 对象中 readObject，只需要传入对象类型，
 * 而不必传入具体的每个属性的类型
 */
public class KryoSerializer implements CommonSerializer{
    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * Kryo 存在线程安全问题，官方建议是放在 ThreadLocal 里，即一个线程一个 Kryo
     */
    public static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
       Kryo kryo = new Kryo();
       
       kryo.register(RpcRequest.class);
       kryo.register(RpcResponse.class);
       kryo.setReferences(true);
       kryo.setRegistrationRequired(false);
       
       return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             Output output = new Output(out)){

            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            
            return output.toBytes();
            
        } catch (IOException e) {
            logger.error("序列化时发生错误:",e);
            throw new RpcException("序列化时发生错误");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             Input input = new Input(in)){

            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            
            return o;

        } catch (IOException e) {
            logger.error("反序列化时发生错误:",e);
            throw new RpcException("反序列化时发生错误");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
