package common.exception;

import common.enumeration.RpcError;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 15:07
 */
public class RpcException extends RuntimeException{
    public RpcException(String msg){
        super(msg);
    }
    
    public RpcException(String msg, Throwable cause){
        super(msg,cause);
    }
    
    public RpcException(RpcError error){
        super(error.getMessage());
    }
    
    public RpcException(RpcError error, String detail){
        super(error.getMessage() + ":" + detail);
    }
}
