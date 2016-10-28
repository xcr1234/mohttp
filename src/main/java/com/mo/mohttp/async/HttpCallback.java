package com.mo.mohttp.async;

import com.mo.mohttp.Response;

import java.io.IOException;


/**
 * 表示异步Request的执行结果，整个执行过程在异步线程中执行，由回调函数触发。
 */
public interface HttpCallback {
    void complete(Response response) throws IOException;
    void failed(Throwable e);
    void cancelled();
}
