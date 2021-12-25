package common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:41
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;
    
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
