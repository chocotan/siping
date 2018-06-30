package io.loli.siping.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T> {
    private Boolean status;
    private String msg;
    private T data;

    public static <P> ResponseDto<P> ok() {
        return new ResponseDto<>(true, "", null);
    }

    public static <P> ResponseDto<P> ok(P data) {
        return new ResponseDto<>(true, "", data);
    }

    public static <P> ResponseDto<P> error(String msg) {
        return new ResponseDto<>(false, msg, null);
    }

}
