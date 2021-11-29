package common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:48
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(20000,"调用成功！"),
    FAIL(20001,"调用失败！"),
    NOT_FOUND_METHOD(20002,"未找到指定方法"),
    NOT_FOUND_CLASS(20003,"未找到指定类");
    
    private final int code;
    private final String message;
}
