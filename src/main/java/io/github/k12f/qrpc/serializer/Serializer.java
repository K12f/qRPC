package io.github.k12f.qrpc.serializer;

import io.github.k12f.qrpc.constants.SerializerCode;

public interface Serializer {

    byte[] serialize(Object o);

    Object deserialize(byte[] bytes, Class<?> clazz);

    // 获取当前序列化的code
    int getCode();
}
