package com.mo.mohttp.anno;

import java.lang.annotation.*;

/**
 * 表示该类或方法不是线程安全的
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface NotThreadSafe {
}
