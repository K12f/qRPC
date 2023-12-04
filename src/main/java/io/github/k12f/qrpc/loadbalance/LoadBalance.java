package io.github.k12f.qrpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface LoadBalance {
    Instance balance(List<Instance> addressList);
}
