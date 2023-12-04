package io.github.k12f.qrpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    @Override
    public Instance balance(List<Instance> addressList) {
        var random = new Random();
        int choose = random.nextInt(addressList.size());
        return addressList.get(choose);
    }
}
