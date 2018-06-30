package io.loli.siping.client;

import com.alibaba.fastjson.JSON;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class QueryDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        } else {
            String bodyStr = Util.toString(body.asReader());
            ResponseDto responseDto = JSON.parseObject(bodyStr, ResponseDto.class);
            if (responseDto.getData() != null) {
                String data = responseDto.getData().toString();
                if (data != null && data.startsWith("[")) {
                    List<Comment> comments = JSON.parseArray(data, Comment.class);
                    responseDto.setData(comments);
                    return responseDto;
                }
            }
            return responseDto;
        }
    }
}
