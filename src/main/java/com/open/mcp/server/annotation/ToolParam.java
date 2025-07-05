package com.open.mcp.server.annotation;

import java.lang.annotation.*;

/**
 * 工具方法参数注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ToolParam {
    /**
     * 参数名称
     */
    String name();

    /**
     * 参数描述
     */
    String description();

    /**
     * 参数是否必须，默认为true
     */
    boolean required() default true;
} 