package com.mo.mohttp.anno;

import java.lang.annotation.*;

/**
 * 表示该值不能为空
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface NotNull {
}
