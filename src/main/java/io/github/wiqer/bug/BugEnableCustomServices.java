package io.github.wiqer.bug;

/**
 * ：BaseEnableCustomServices
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 22 / 2024/1/22  14:15
 * @description：
 * @modified By：
 */
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(
        basePackages = "io.github.wiqer.bug",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Service.class,Component.class})
)
public @interface BugEnableCustomServices {
}
