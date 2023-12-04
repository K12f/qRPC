package io.github.k12f.qrpc.enity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResponse implements Serializable {

    // 更新,这里我们需要加入这个，不然用其它序列化方式（除了java Serialize）得不到data的type
    Class<?> dataType;
    private int code;
    private String message;
    private Object data;


    public static RPCResponse success(Object data) {
        var response = new RPCResponse();

        Class<?> responseClazz = null;
        if (null != data) {
            responseClazz = data.getClass();
        }

        response.setCode(200);
        response.setData(data);
        response.setMessage("ok");
        response.setDataType(responseClazz);
        return response;
    }

    public static RPCResponse fail(int code, String msg) {
        var response = new RPCResponse();
        response.setCode(code);
        response.setMessage(msg);
        return response;
    }
}