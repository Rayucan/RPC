package core.hook;

import common.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.NacosUtil;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/12/26 1:02
 * 
 * 钩子，即在某些事件发生后自动调用的方法
 * 
 * 因为启动服务端后再进行关闭，并不会自动注销 Nacos 对应的服务信息
 * 可能导致再次向 Nacos请求服务时，获取到已经关闭的服务端信息，最终连接不到服务器而调用失败
 * 
 * 这个类可以帮助在服务端关闭之前，自动向 Nacos 注销服务
 * 
 * 另外，这个类还使用了单例模式
 */
public class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    
    private static final ShutdownHook shutdownHook = new ShutdownHook();
    
    public static ShutdownHook getShutdownHook(){
        return shutdownHook;
    }
    
    public void addClearAllHook(){
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactory.shutdownAll();
        }));
    }
}
