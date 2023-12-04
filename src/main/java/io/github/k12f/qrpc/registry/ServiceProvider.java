package io.github.k12f.qrpc.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    /**
     * 一个实现类可能实现多个服务接口，
     */
    private final Map<String, Object> interfaceProvider;

    private final ServiceRegister serviceRegister;

    public ServiceProvider(ServiceRegister serviceRegister) throws NacosException {
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = serviceRegister;
    }

    public void provideServiceInterface(Object service, String host, int port) throws NacosException {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaces) {
            // 本机的映射表
            interfaceProvider.put(clazz.getName(), service);
            // 在注册中心注册服务
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host, port));
        }

    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}