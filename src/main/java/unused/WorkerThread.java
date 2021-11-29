package unused;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import lombok.AllArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 0:14
 */

@AllArgsConstructor
public class WorkerThread implements Runnable {
    private Socket socket;
    private Object service;

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){

            RpcRequest rpcRequest = (RpcRequest) in.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            
            out.writeObject(RpcResponse.success(returnObject));
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
