package io.github.k12f.qrpc.client;

import com.alibaba.nacos.api.exception.NacosException;
import io.github.k12f.qrpc.enity.RPCRequest;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Data
public class RPCClientProxy implements InvocationHandler {
    private final NettyRPCClient client;

    public RPCClientProxy(NettyRPCClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        var request = new RPCRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());

        return client.send(request).getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return clazz.cast(Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this));
    }
}
