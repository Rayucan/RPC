package core;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 15:27
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    
    public Object handle(RpcRequest rpcRequest,Object service){
        Object result = null;
        
        try {
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("服务：{} 成功调用方法：{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (Exception e) {
            logger.error("调用时发生错误：",e);
        }
        
        return result;
    }
    
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;

        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, rpcRequest.getRequestId());
        }
        
        return method.invoke(service,rpcRequest.getParameters());
    }
}
