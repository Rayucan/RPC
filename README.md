## My-RPC

![系统架构](https://github.com/CN-GuoZiyang/My-RPC-Framework/raw/master/images/architecture.png)

My-RPC 是一款基于 Nacos 实现的 RPC 框架，网络传输实现了基于 Socket 与 Netty 的版本，并且实现了 Kryo 序列化与多种负载均衡算法。

系统架构如上图，消费者调用提供者的方式取决于消费者的客户端选择，如选用原生 Socket 则该调用使用 BIO，如选用 Netty 方式则该调用使用 NIO。

### 已完成的功能

- [x] 实现了基于 Java 原生 Socket 传输与 Netty 传输两种网络传输方式

- [x] 实现了 Kryo 序列化算法

- [x] 实现了两种负载均衡算法：随机算法与轮转算法

- [x] 使用 Nacos 作为注册中心，管理服务提供者信息

- [x] 使用自定义的通信协议

### TODO

- [ ] 实现更多序列化算法，如 Hessian 算法与 Google Protobuf 算法等
- [ ] 实现更多负载均衡算法，如加权随机、哈希算法等
- [ ] 集成 Spring Boot 框架，并提供自动注册服务
- [ ] 其他注册中心支持，如 Zookeeper
- [ ] 支持 HTTP 协议通信