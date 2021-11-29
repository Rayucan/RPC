package common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 17:16
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    JSON(1);
    
    private final int code;
}
