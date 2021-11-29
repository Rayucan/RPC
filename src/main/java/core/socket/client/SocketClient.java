package core.socket.client;

import core.RpcClient;
import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enumeration.ResponseCode;
import common.enumeration.RpcError;
import common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/24 23:58
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    
    private final String host;
    private final int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object sendRequest(RpcRequest rpcRequest){
        try (Socket socket = new Socket(host, port)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            out.writeObject(rpcRequest);
            out.flush();
            
//            return ((RpcResponse)in.readObject()).getData();

            RpcResponse rpcResponse = (RpcResponse) in.readObject();

            if (rpcResponse == null){
                logger.error("服务调用失败,service:{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,"service:" + rpcRequest.getInterfaceName());
            }

            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("调用服务失败,service:{},response:{}",rpcRequest.getInterfaceName(),rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,"service:" + rpcRequest.getInterfaceName());
            }

            return rpcResponse.getData();
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }
}
