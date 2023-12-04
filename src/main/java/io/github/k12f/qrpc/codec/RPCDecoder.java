package io.github.k12f.qrpc.codec;

import io.github.k12f.qrpc.constants.DataPackType;
import io.github.k12f.qrpc.constants.RPCConstants;
import io.github.k12f.qrpc.constants.SerializerCode;
import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.enity.RPCResponse;
import io.github.k12f.qrpc.exception.QRPCException;
import io.github.k12f.qrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RPCDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) {
//        - 首位表示数据序列魔法常量 00xCAFEBABE
//                - 第二位表示数据格式化方法 ,目前只支持1:json
//                - 1:表示request , 2:表示response
//                - 数据包长度
//                - 数据包
        var magic = in.readInt();
        if (magic != RPCConstants.MAGIC_NUMBER) {
            log.error(RPCConstants.RPC_MAGIC_NOT_SUPPORT + ": " + magic);
            throw new QRPCException(RPCConstants.RPC_MAGIC_NOT_SUPPORT);
        }

        // 序列化方式
        var serializerCode = in.readInt();
        if (!SerializerCode.check(serializerCode)) {
            log.error(RPCConstants.RPC_SERIALIZER_NOT_SUPPORT + ": " + serializerCode);
            throw new QRPCException(RPCConstants.RPC_SERIALIZER_NOT_SUPPORT);
        }

        // 包结构
        var dataPackType = in.readInt();
        Class<?> dataPackClazz;
        if (dataPackType == DataPackType.REQUEST_PACK.getCode()) {
            dataPackClazz = RPCRequest.class;
        } else if (dataPackType == DataPackType.RESPONSE_PACK.getCode()) {
            dataPackClazz = RPCResponse.class;
        } else {
            log.error(RPCConstants.RPC_DATA_PACK_NOT_SUPPORT);
            throw new QRPCException(RPCConstants.RPC_DATA_PACK_NOT_SUPPORT);
        }

        // 根据序列化code获取序列化工具
        var serializer = Serializer.getSerializer(serializerCode);
        if (serializer == null) {
            log.error(RPCConstants.RPC_SERIALIZER_NOT_SUPPORT);
            throw new QRPCException(RPCConstants.RPC_SERIALIZER_NOT_SUPPORT);
        }

        var dataPackLen = in.readInt();

        var dataPack = new byte[dataPackLen];
        in.readBytes(dataPack);
        var obj = serializer.deserialize(dataPack, dataPackClazz);
        out.add(obj);
    }
}
