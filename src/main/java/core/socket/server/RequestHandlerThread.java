package core.socket.server;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.exception.RpcException;
import core.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 15:21
 */
public class RequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);
    
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
            
            RpcRequest rpcRequest = (RpcRequest) in.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            out.writeObject(RpcResponse.success(RpcResponse.success(result)));
            out.flush();

        } catch (IOException | ClassNotFoundException | RpcException e) {
            e.printStackTrace();
        }
    }
}
