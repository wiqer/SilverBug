package io.github.wiqer.bug;

import org.springframework.context.annotation.Import;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BugEnableCustomServices - 启用Bug能力自定义服务
 * 自动扫描和注册io.github.wiqer.bug.manage包下的所有Service和Component
 *
 * @author 李岚峰
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BugEnableCustomServices.BugCustomServicesConfiguration.class)
public @interface BugEnableCustomServices {
    
    /**
     * Bug自定义服务配置类
     */
    @org.springframework.context.annotation.Configuration
    @org.springframework.context.annotation.ComponentScan(
        basePackages = "io.github.wiqer.bug.manage",
        includeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
            type = org.springframework.context.annotation.FilterType.ANNOTATION, 
            classes = {org.springframework.stereotype.Service.class, org.springframework.stereotype.Component.class}
        )
    )
    class BugCustomServicesConfiguration {
        // 配置类，用于启用组件扫描
    }
}
