package core.registry;

import java.net.InetSocketAddress;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/12/23 20:18
 */
public interface ServiceRegistry {
    /**
     * 将服务名与地址注册到服务注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
