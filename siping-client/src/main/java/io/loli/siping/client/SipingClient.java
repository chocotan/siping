package io.loli.siping.client;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class SipingClient {
    private ClientRender render;
    private SipingInterface sipingInterface;
    private SipingProperties properties;


    public ResponseDto<List<Comment>> getComment(String url) {
        ResponseDto<List<Comment>> listResponseDto = sipingInterface.queryByUrl(url, properties.getSecret(), properties.getSiteId());
        log.debug("Get comments url={}, responseJson={}", url, JSON.toJSONString(listResponseDto));
        return listResponseDto;
    }

    public String getCommentsHtml(String url, Map<String, String> context) {
        ResponseDto<List<Comment>> comment = sipingInterface.queryByUrl(url, properties.getSecret(), properties.getSiteId());
        if (comment.getData() != null) {
            comment.getData().forEach(c -> {
                c.setHeaderImgUrl(properties.getHeaderImgUrlTemplate().replace("${md5}", DigestUtils.md5DigestAsHex(c.getEmail().getBytes(Charset.forName("UTF-8")))));
            });
        }
        return render.render(comment, context);
    }

    public ResponseDto<Object> addComment(String url, String username, String email, String content) {
        ResponseDto<Object> add = sipingInterface.add(url, properties.getSecret(), username, email, content, properties.getSiteId(), null);
        log.debug("Add comment url={}, responseJson={}", url, JSON.toJSONString(add));
        return add;
    }
}
