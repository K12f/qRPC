package io.github.k12f.qrpc.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据包类型
 */
@AllArgsConstructor
@Getter
public enum DataPackType {
    REQUEST_PACK(1),
    RESPONSE_PACK(2);

    private final int code;

}
