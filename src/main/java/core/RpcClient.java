package core;

import common.entity.RpcRequest;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/28 17:12
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
