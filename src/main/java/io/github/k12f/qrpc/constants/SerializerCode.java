package io.github.k12f.qrpc.constants;

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
}
