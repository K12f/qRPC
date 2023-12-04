package io.github.k12f.qrpc;

import com.alibaba.nacos.api.exception.NacosException;
import io.github.k12f.qrpc.registry.NacosServiceRegister;
import io.github.k12f.qrpc.registry.ServiceProvider;
import io.github.k12f.qrpc.serializer.JsonSerializer;
import io.github.k12f.qrpc.server.NettyRPCServer;
import io.github.k12f.qrpc.service.impl.BlogServiceImpl;
import io.github.k12f.qrpc.service.impl.UserServiceImpl;
import org.junit.Test;

public class TestServer2 {
    @Test
    public void run() throws NacosException {
        var userService = new UserServiceImpl();
        var blogService = new BlogServiceImpl();

        var host = "127.0.0.1";
        var port = 8899;

        // nacos
        var nacosServiceRegister = new NacosServiceRegister(host, 8848);
        var serviceProvider = new ServiceProvider(nacosServiceRegister);

        serviceProvider.provideServiceInterface(userService, host, port);
        serviceProvider.provideServiceInterface(blogService, host, port);

        var RPCServer = new NettyRPCServer(serviceProvider, new JsonSerializer());
        RPCServer.start(port);
    }
}
