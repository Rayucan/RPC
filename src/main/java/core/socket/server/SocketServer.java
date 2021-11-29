package core.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.registry.ServiceRegistry;
import core.RequestHandler;
import core.RpcServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author Rayucan
 * @description
 * @date Created on 2021/11/25 0:05
 */
public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    
    private final ExecutorService threadPool;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;
    
    public SocketServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                workingQueue,
                threadFactory);
    }
    
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动...");
            Socket socket = null;
            
            while ((socket = serverSocket.accept()) != null){
                logger.info("客户端消费者连接：{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry));
            }
            
            threadPool.shutdown();
            
        } catch (IOException e) {
            logger.info("服务器启动出现错误:",e);
        }
    }
}
