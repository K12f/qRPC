package io.github.k12f.qrpc.server;

public interface RPCServer {
    void start(int port);

    void stop();
}
