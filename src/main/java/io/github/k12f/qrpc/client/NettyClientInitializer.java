package io.github.k12f.qrpc.client;

import io.github.k12f.qrpc.codec.RPCDecoder;
import io.github.k12f.qrpc.codec.RPCEncoder;
import io.github.k12f.qrpc.serializer.JsonSerializer;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private final Serializer serializer;

    @Override
    protected void initChannel(SocketChannel ch) {
        log.info("netty client initializer");
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new RPCDecoder());
        pipeline.addLast(new RPCEncoder(serializer));
        pipeline.addLast(new NettyClientHandler());
    }
}
