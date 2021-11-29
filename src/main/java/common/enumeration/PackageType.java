package common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/29 17:22
 */
@AllArgsConstructor
@Getter
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);
    
    private final int code;
}
