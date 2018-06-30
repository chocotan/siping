package io.loli.siping.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "siping")
public class SipingProperties {
    /**
     * 要接入私评的网站ID
     */
    private Long siteId;
    /**
     * 要介入私评的网站secret，请在私评系统中查看
     */
    private String secret;
    /**
     * 你所部署的私评系统，需要带http://前缀，末尾不能以/结尾
     */
    private String url;
    /**
     * 你的网站的地址，需要带http://前缀，末尾不能以/结尾
     */
    private String siteUrl;
    /**
     * 过滤器pattern
     */
    private List<String> filterPatterns = Arrays.asList("/*");
    /**
     * 自定义头像域名
     */
    private String headerImgUrlTemplate = "https://cn.gravatar.com/avatar/${md5}.jpg?size=50";
    /**
     * 评论转为html的转换器，目前只支持SIMPLE
     */
    private String renderType = "SIMPLE";
}
