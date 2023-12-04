package io.github.k12f.qrpc.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void register(String serviceName, InetSocketAddress serverAddress) throws NacosException;

    InetSocketAddress serviceDiscovery(String serviceName) throws NacosException;
}
