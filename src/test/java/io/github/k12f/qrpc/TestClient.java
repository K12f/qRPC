package io.github.k12f.qrpc;

import com.alibaba.nacos.api.exception.NacosException;
import io.github.k12f.qrpc.client.NettyRPCClient;
import io.github.k12f.qrpc.client.RPCClientProxy;
import io.github.k12f.qrpc.model.User;
import io.github.k12f.qrpc.registry.NacosServiceRegister;
import io.github.k12f.qrpc.serializer.JsonSerializer;
import io.github.k12f.qrpc.service.UserService;
import org.junit.Test;
import io.github.k12f.qrpc.service.BlogService;

public class TestClient {

    @Test
    public void run() throws NacosException {

        var nacosServiceProvider = new NacosServiceRegister("127.0.0.1", 8848);
        var nettyRPCClient = new NettyRPCClient(nacosServiceProvider,new JsonSerializer());
        // 把这个客户端传入代理客户端
        RPCClientProxy rpcClientProxy = new RPCClientProxy(nettyRPCClient);
        // 代理客户端根据不同的服务，获得一个代理类， 并且这个代理类的方法以或者增强（封装数据，发送请求）
        var userService = rpcClientProxy.getProxy(UserService.class);
        // 调用方法
        var userByUserId = userService.insertUserId(new User(1, "发的的阿道夫", true));

        System.out.println(userByUserId);

        // 查找
        var user = userService.getUserByUserId(84266);

        System.out.println(user);
        var blogService = rpcClientProxy.getProxy(BlogService.class);

        var blog = blogService.getBlogById(1);
        System.out.println(blog);
    }
}
