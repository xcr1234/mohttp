package com.mo.mohttp.async;

import com.mo.mohttp.Response;

import java.io.IOException;


/**
 * 表示异步Request请求的执行回调。
 */
public interface HttpCallback {
    /**
     * 当http过程完成后，触发该方法
     * @param response http请求执行的结果
     * @throws IOException 用于处理response对象中抛出的异常
     */
    void complete(Response response) throws IOException;

    /**
     * 当http过程出现异常后，触发该方法
     * @param e 出现的异常：{@link java.net.URISyntaxException} {@link IOException} {@link InterruptedException}
     */
    void failed(Throwable e);

    /**
     * 当http过程被取消后（显式调用cancel方法）后触发该方法，
     */
    void cancelled();
}
