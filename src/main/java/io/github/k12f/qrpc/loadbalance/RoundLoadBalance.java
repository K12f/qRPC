package io.github.k12f.qrpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance {
    private static int choose = -1;

    @Override
    public Instance balance(List<Instance> addressList) {
        choose++;
        choose = choose % addressList.size();
        return addressList.get(choose);
    }
}
