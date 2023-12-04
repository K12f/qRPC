package io.github.k12f.qrpc.serializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import io.github.k12f.qrpc.constants.RPCConstants;
import io.github.k12f.qrpc.constants.SerializerCode;
import io.github.k12f.qrpc.enity.RPCRequest;
import io.github.k12f.qrpc.enity.RPCResponse;
import io.github.k12f.qrpc.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object o) {
        try {
            return JSON.toJSONBytes(o);
        } catch (JSONException e) {
            log.warn(RPCConstants.RPC_JSON_SERIALIZER_FAILED + e.getMessage());
            throw new SerializeException(e.getMessage());
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        var obj = JSON.parseObject(bytes, clazz, JSONReader.Feature.SupportClassForName);
        // 将格式化的数据转为可以处理的基本类型
        if (obj instanceof RPCRequest) {
            obj = handleRequest(obj);
        } else {
            obj = handleResponse(obj);
        }
        return obj;

    }

    private Object handleResponse(Object obj) {
        var response = (RPCResponse) obj;
        log.info("response: " + response);
        Class<?> dataType = response.getDataType();

        Object data = null;
        if (null != dataType && !dataType.isAssignableFrom(response.getData().getClass())) {
            data = JSON.to(dataType,response.getData());
        }

        response.setData(data);
        return response;
    }

    private Object handleRequest(Object obj) {
        var request = (RPCRequest) obj;
        log.info("request: " + request);

        // 处理格式化的数据
        Object[] objects = new Object[request.getParameters().length];

        for (int i = 0; i < request.getParamTypes().length; i++) {
            var clazz = request.getParamTypes()[i];
            if (!clazz.isAssignableFrom(request.getParameters()[i].getClass())) {
                objects[i] = JSON.to(request.getParamTypes()[i], request.getParameters()[i]);
            } else {
                objects[i] = request.getParameters()[i];
            }
        }
        request.setParameters(objects);
        return request;
    }

    @Override
    public int getCode() {
        return SerializerCode.JSON.getCode();
    }
}
