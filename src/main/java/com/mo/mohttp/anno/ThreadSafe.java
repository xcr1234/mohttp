package com.mo.mohttp.anno;

import java.lang.annotation.*;

/**
 * 表示是线程安全的.
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface ThreadSafe {
}
