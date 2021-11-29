package common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/28 16:46
 */

@AllArgsConstructor
@Getter
public enum RpcError {
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_NOT_FOUND("找不到对应服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("未识别的协议包"),
    UNKNOWN_SERIALIZER("未识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("未识别的数据包类型");
    
    
    private final String message;
}
