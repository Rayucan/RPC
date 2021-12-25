package core;

import core.serializer.CommonSerializer;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/28 17:11
 */
public interface RpcServer {
    void start();
    
    void setSerializer(CommonSerializer serializer);
    
    <T> void publishService(Object service, Class<T> serviceClass);
}
