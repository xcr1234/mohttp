package com.mo.mohttp.anno;

import java.lang.annotation.*;

/**
 * 表示该值可以为空
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface NullAble {
}
