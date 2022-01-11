package core.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface LoadBalancer {
    /**
     * 选择某一负载均衡策略
     * 
     * 从一系列 Instance 选择一个
     * 两个比较经典的算法：随机 和 轮转(轮询)
     * 
     * @param instances
     * @return
     */
    Instance select(List<Instance> instances);
}
