package io.github.k12f.qrpc.serializer;

import io.github.k12f.qrpc.constants.SerializerCode;

public interface Serializer {
    static Serializer getSerializer(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

    byte[] serialize(Object o);

    Object deserialize(byte[] bytes, Class<?> clazz);

    // 获取当前序列化的code
    int getCode();
}
