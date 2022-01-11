package core.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 轮转(轮询)算法
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    private static int index = 0;
    
    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()){
            index %= instances.size();
        }
        
        //index 表示当前选到第几个服务器,然后自增+1
        return instances.get(index++);
    }
}
