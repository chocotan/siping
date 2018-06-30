package io.loli.siping.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SimpleRender implements ClientRender {

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    {
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
    }

    TemplateEngine templateEngine = new TemplateEngine();

    {
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Autowired
    private SipingProperties properties;

    @Override
    public String render(ResponseDto<List<Comment>> comments, Map<String, String> additionalContext) {
        Map<String, Object> contextV = new HashMap<>(additionalContext);
        List<Comment> data = comments.getData();
        if (data != null) {
            data.sort((o1, o2) -> (int) (o2.getCreateDate().getTime() - o1.getCreateDate().getTime()));
            contextV.put("comments", data);
            contextV.put("commentSize", data.size());
        } else {
            contextV.put("commentSize", 0);
        }

        contextV.put("config", properties);
        IContext context = new Context(Locale.getDefault(), contextV);
        return templateEngine.process("simple", context);
    }


}
