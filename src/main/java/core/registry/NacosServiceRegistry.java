package core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import common.enumeration.RpcError;
import common.exception.RpcException;
import core.loadbalancer.LoadBalancer;
import core.loadbalancer.RandomLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/12/23 20:19
 * 
 * Nacos 服务注册中心
 */
public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);
    

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时发生错误:",e);
            throw new RpcException(RpcError.FAILED_REGISTER_SERVICE);
        }
    }


}
