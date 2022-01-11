package core.provider;

import common.exception.RpcException;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 14:59
 * 
 * 保存和提供服务实例对象
 */
public interface ServiceProvider {
    /**
     * 注册服务信息
     * @param service 
     * @param <T> 
     */
    <T> void addServiceProvider(T service,Class<T> serviceClass) throws RpcException;

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    Object getServiceProvider(String serviceName) throws RpcException;
}
