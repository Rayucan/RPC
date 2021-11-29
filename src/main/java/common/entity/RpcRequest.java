package common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:41
 */

@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {
    
    public RpcRequest(){};
    
    /**
     * 调用接口名
     */
    private String interfaceName;
    /**
     * 调用方法名
     */
    private String methodName;
    /**
     * 方法的参数列表
     */
    private Object[] parameters;
    /**
     * 方法的参数类型列表
     */
    private Class<?>[] parameterTypes;
}
