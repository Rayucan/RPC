package core.registry;

import common.exception.RpcException;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 14:59
 */
public interface ServiceRegistry {
    /**
     * 注册服务信息
     * @param service 
     * @param <T> 
     */
    <T> void register(T service) throws RpcException;

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    Object getService(String serviceName) throws RpcException;
}
