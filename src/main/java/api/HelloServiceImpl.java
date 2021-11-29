package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:19
 */
public class HelloServiceImpl implements HelloService{
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject object) {
        logger.info("接收到:{}", object.getMessage());
        return "这是返回值，id=" + object.getId();
    }
}
