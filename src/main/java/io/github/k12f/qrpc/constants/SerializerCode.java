package io.github.k12f.qrpc.constants;

import io.github.k12f.qrpc.serializer.JsonSerializer;
import io.github.k12f.qrpc.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

/**
 * 序列化的方式
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {

    JSON(1);
    private final int code;


    /**
     * 检测序列化方式是否存在
     * @param code int
     * @return boolean
     */
    public static boolean check(int code) {
        return Arrays.stream(SerializerCode.values()).allMatch(t -> t.getCode() == code);
    }

    public static Serializer getSerializer(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
