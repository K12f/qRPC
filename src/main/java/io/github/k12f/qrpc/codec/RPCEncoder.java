package io.github.k12f.qrpc.codec;

import io.github.k12f.qrpc.constants.DataPackType;
import io.github.k12f.qrpc.constants.RPCConstants;
import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.serializer.JsonSerializer;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class RPCEncoder extends MessageToByteEncoder<Object> {

    private final Serializer serializer;

    public RPCEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) {
        log.info("netty encode");
        log.info("data" + o);

        out.writeInt(RPCConstants.MAGIC_NUMBER);

        // 只支持json
        out.writeInt(serializer.getCode());

        // 表示是request还是response
        if (o instanceof RPCRequest) {
            out.writeInt(DataPackType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(DataPackType.RESPONSE_PACK.getCode());
        }

        var dataPack = serializer.serialize(o);

        log.info("pack:" + Arrays.toString(dataPack));
        out.writeInt(dataPack.length);
        out.writeBytes(dataPack);
    }
}
