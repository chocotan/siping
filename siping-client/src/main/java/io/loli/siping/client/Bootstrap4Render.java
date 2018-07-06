package io.loli.siping.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class Bootstrap4Render extends SimpleRender{


    public String getFileName(){
        return "bootstrap4";
    }
}
