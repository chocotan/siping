package io.loli.siping.client;

import feign.Feign;
import feign.form.FormEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(value = {SipingProperties.class})
public class SipingConfiguration {

    @Autowired
    SipingProperties sipingProperties;


    @Bean
    public SipingInterface dataSyncInterface() {
        return Feign.builder()
                .encoder(new FormEncoder())
                .decoder(new QueryDecoder())
                .target(SipingInterface.class, sipingProperties.getUrl());
    }

    @Bean
    public ClientRender render() {
        switch (sipingProperties.getRenderType()) {
            case "SIMPLE":
                return new SimpleRender();
            case "BOOTSTRAP4":
                return new Bootstrap4Render();
        }
        return new SimpleRender();
    }

    @Bean
    public SipingClient sipingClient() {
        return new SipingClient(render(), dataSyncInterface(), sipingProperties);
    }


    @Bean
    public FilterRegistrationBean sipingFilter() {
        FilterRegistrationBean<SipingServletFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new SipingServletFilter(sipingClient(), sipingProperties));
        List<String> filterPatterns = sipingProperties.getFilterPatterns();
        List<String> patterns = new ArrayList<>(filterPatterns);
        bean.setUrlPatterns(patterns);
        return bean;
    }
}
