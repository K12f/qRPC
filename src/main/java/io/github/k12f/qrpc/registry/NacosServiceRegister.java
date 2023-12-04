package io.github.k12f.qrpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.github.k12f.qrpc.loadbalance.LoadBalance;
import io.github.k12f.qrpc.loadbalance.RandomLoadBalance;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.List;


@Data
public class NacosServiceRegister implements ServiceRegister {
    private final NamingService namingService;

    private final LoadBalance loadBalance = new RandomLoadBalance();

    public NacosServiceRegister(String host, int port) throws NacosException {

        this.namingService = NamingFactory.createNamingService(host + ":" + port);

    }

    private List<Instance> getAllInstances(String serviceName) throws NacosException {
        return this.namingService.getAllInstances(serviceName);
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) throws NacosException {
        var instance = new Instance();
        instance.setIp(serverAddress.getHostName());
        instance.setPort(serverAddress.getPort());

        if (!this.getAllInstances(serviceName).contains(instance)) {
            this.namingService.registerInstance(serviceName, instance);
        } else {
            throw new NacosException(NacosException.CLIENT_INVALID_PARAM, "该实例已存在");
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) throws NacosException {
        // 获取所有健康实列
        var instances = this.namingService.selectInstances(serviceName, true);
        // 获取根据负载均衡获取实列
        var instance = this.loadBalance.balance(instances);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }
}
