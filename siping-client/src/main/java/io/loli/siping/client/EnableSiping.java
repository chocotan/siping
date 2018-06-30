package io.loli.siping.client;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SipingConfigurationSelector.class)
public @interface EnableSiping {
    AdviceMode mode() default AdviceMode.PROXY;
}
