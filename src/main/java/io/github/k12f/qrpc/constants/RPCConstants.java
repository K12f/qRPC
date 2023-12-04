package io.github.k12f.qrpc.constants;

public interface RPCConstants {

    // 首位表示数据序列魔法常量，
    int MAGIC_NUMBER = 0xCAFEBABE;

    String RPC_MAGIC_NOT_SUPPORT = "不支持的协议数据包";
    String RPC_SERIALIZER_NOT_SUPPORT = "不支持的序列化方式";

    String RPC_DATA_PACK_NOT_SUPPORT = "不支持的数据包结构";
    String RPC_JSON_SERIALIZER_FAILED = "json序列化失败";
}
