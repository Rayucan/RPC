package core.registry;

import com.alibaba.nacos.api.naming.pojo.Instance;
import core.loadbalancer.LoadBalancer;
import core.loadbalancer.RandomLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos 服务发现中心
 */
public class NacosServiceDiscovery implements ServiceDiscovery{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if (loadBalancer == null){
            this.loadBalancer = new RandomLoadBalancer();
        }else {
            this.loadBalancer = loadBalancer;
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        }catch (Exception e){
            logger.error("获取服务时发生错误:",e);
        }

        return null;
    }
    
}
