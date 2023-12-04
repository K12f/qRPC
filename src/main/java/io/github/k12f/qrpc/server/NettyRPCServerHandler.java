package io.github.k12f.qrpc.server;

import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.enity.RPCResponse;
import io.github.k12f.qrpc.registry.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private final ServiceProvider serviceProvider;

    public NettyRPCServerHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn(cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) {
        var response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    private RPCResponse getResponse(RPCRequest request) {
        // 得到服务名
        var interfaceName = request.getInterfaceName();
        // 得到服务端相应服务实现类
        var service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        try {
            log.info("service: " + service);
            var method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke = method.invoke(service, request.getParameters());

            log.info("request: " + request);
            log.info("method: " + method);
            log.info("invoke: " + invoke);
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            return RPCResponse.fail(500, e.getMessage());
        }
    }
}
