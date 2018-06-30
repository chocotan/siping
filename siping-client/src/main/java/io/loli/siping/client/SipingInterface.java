package io.loli.siping.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import javaslang.Tuple2;

import java.util.List;

public interface SipingInterface {
    @RequestLine("POST /api/comment/add")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseDto<Object> add(
            @Param("url") String url,
            @Param("secret") String secret,
            @Param("username") String username,
            @Param("email") String email,
            @Param("content") String content,
            @Param("siteId") Long siteId,
            @Param("parentId") Long parentId
    );

    @RequestLine("POST /api/comment/query")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseDto<List<Comment>> queryByUrl(@Param("url") String url,
                                          @Param("secret") String secret,
                                          @Param("siteId") Long siteId);

}
