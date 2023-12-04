package io.github.k12f.qrpc.server;

import io.github.k12f.qrpc.codec.RPCDecoder;
import io.github.k12f.qrpc.codec.RPCEncoder;
import io.github.k12f.qrpc.registry.ServiceProvider;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ServiceProvider serviceProvider;
    private final Serializer serializer;

    public NettyServerInitializer(ServiceProvider serviceProvider, Serializer serializer) {
        this.serviceProvider = serviceProvider;
        this.serializer = serializer;
    }


    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new RPCDecoder());
        pipeline.addLast(new RPCEncoder(serializer));
        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
