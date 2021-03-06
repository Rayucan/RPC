package core.provider;

import common.enumeration.RpcError;
import common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 15:01
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    
    //map 和 set 都为 static
    //是为了保证全局唯一的注册信息，同时创建 RpcServer 时就不需要传入了
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();
    
    @Override
    public synchronized <T> void addServiceProvider(T service,Class<T> serviceClass) throws RpcException {
        String serviceName = serviceClass.getCanonicalName();
        
        if (registeredService.contains(serviceName))
            return;
        
        registeredService.add(serviceName);
        
        serviceMap.put(serviceName,service);
        
        logger.info("向接口：{} 注册服务：{}",service.getClass().getInterfaces(),serviceName);
    }

    @Override
    public synchronized Object getServiceProvider(String serviceName) throws RpcException {
        Object service = serviceMap.get(serviceName);
        if (service == null)
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        return service;
    }
}
