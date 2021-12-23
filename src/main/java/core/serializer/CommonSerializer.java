package core.serializer;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 16:19
 */
public interface CommonSerializer {
    
    /**
     * 序列化
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @return
     */
    Object deserialize(byte[] bytes,Class<?> clazz);

    /**
     * 获取序列化器编号
     * @return
     */
    int getCode();

    /**
     * 根据已知编号，获取序列化器
     * @param code
     * @return
     */
    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
