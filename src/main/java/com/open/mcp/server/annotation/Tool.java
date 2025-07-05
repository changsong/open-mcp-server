package com.open.mcp.server.annotation;

import java.lang.annotation.*;

/**
 * Tool method annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tool {
    /**
     * Tool name
     */
    String name();

    /**
     * Tool description
     */
    String description();
} 