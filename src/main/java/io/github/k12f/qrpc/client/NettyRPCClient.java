package io.github.k12f.qrpc.client;

import com.alibaba.nacos.api.exception.NacosException;
import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.enity.RPCResponse;
import io.github.k12f.qrpc.exception.QRPCException;
import io.github.k12f.qrpc.registry.ServiceRegister;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyRPCClient implements QRPCClient {
    private static final EventLoopGroup GROUP = new NioEventLoopGroup();

    private static final Bootstrap BOOTSTRAP = new Bootstrap();

    static {
        BOOTSTRAP.group(GROUP)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class);
    }

    private final ServiceRegister serviceRegister;

    private final Serializer serializer;

    public NettyRPCClient(ServiceRegister serviceRegister, Serializer serializer) {
        this.serviceRegister = serviceRegister;
        this.serializer = serializer;
    }

    @Override
    public <T> RPCResponse send(RPCRequest request) {
        try {
            var address = serviceRegister.serviceDiscovery(request.getInterfaceName());
            var channelFuture = BOOTSTRAP
                    .handler(new NettyClientInitializer(this.serializer))
                    .connect(address).sync();
            var channel = channelFuture.channel();

            channel.writeAndFlush(request);
            channel.closeFuture().sync();


            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            var response = channel.attr(key).get();

            if (response != null) {
                log.info(response.toString());
                return response;
            }
            return new RPCResponse();
        } catch (NacosException | InterruptedException e) {
            log.error(e.getMessage());
            throw new QRPCException(e.getMessage());
        }
    }
}
