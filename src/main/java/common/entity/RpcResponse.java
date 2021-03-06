package common.entity;

import common.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:42
 */

@Data
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;
    
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应补充信息
     */
    private String message;
    /**
     * 响应数据主体
     */
    private T data;
    
    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        
        return response;
    }
    
    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        
        return response;
    }
}
