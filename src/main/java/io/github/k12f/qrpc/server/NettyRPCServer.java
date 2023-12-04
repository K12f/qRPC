package io.github.k12f.qrpc.server;

import io.github.k12f.qrpc.registry.ServiceProvider;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyRPCServer implements RPCServer {
    private final ServiceProvider serviceProvider;
    private final Serializer serializer;

    public NettyRPCServer(ServiceProvider serviceProvider , Serializer serializer) {
        this.serviceProvider = serviceProvider;
        this.serializer = serializer;
    }

    @Override
    public void start(int port) {
        try (
                var bossGroup = new NioEventLoopGroup();
                var workGroup = new NioEventLoopGroup()
        ) {
            log.info("netty service is running");
            var serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyServerInitializer(serviceProvider,serializer));

            var channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void stop() {

    }
}
