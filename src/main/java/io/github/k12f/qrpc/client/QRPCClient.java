package io.github.k12f.qrpc.client;

import com.alibaba.nacos.api.exception.NacosException;
import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.enity.RPCResponse;

public interface QRPCClient {

   <T> RPCResponse send(RPCRequest request) throws NacosException, InterruptedException;
}
