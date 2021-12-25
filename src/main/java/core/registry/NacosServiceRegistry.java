package core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import common.enumeration.RpcError;
import common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/12/23 20:19
 */
public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);
    
    private static final String SERVER_ADDRESS = "127.0.0.1:8848";
    private static final NamingService namingService;
    
    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDRESS);
        }catch (Exception e){
            logger.error("连接至Nacos时发生错误:",e);
            throw new RpcException(RpcError.FAILED_CONNECT_SERVICE_REGISTRY);
        }
    }
    
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务时发生错误:",e);
            throw new RpcException(RpcError.FAILED_REGISTER_SERVICE);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        }catch (Exception e){
            logger.error("获取服务时发生错误:",e);
        }
        
        return null;
    }
}
