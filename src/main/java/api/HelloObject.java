package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rayucan
 * @description 实现 Serializable 接口是因为，它需要在调用过程中从客户端传递给服务端
 * @date Created on 2021/11/24 23:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
