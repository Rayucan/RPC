package core.registry;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    /**
     * 根据服务名查找服务实例
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
