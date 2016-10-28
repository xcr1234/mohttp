package com.mo.mohttp;


import com.mo.mohttp.anno.NotNull;
import com.mo.mohttp.anno.NullAble;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 表示http请求执行的结果
 */
public interface Response {

    /**
     * 返回状态码
     * @return HTTP标准状态码，取{@link com.mo.mohttp.constant.StatusCode}中的常量值。
     * @throws IOException
     */
    int statusCode() throws IOException;

    /**
     * 将response中的流转以字符串格式输出。
     * @return  转换后的字符串
     * @throws IOException
     */
    String string() throws IOException;

    /**
     * 将response中的流转以字符串格式输出。
     * @param encoding 字符串编码格式
     * @return 转换后的字符串
     * @throws IOException
     */
    String string(@NullAble String encoding) throws IOException;

    /**
     * 将response中的流转以字符串格式输出。
     * @param charset 字符串编码格式
     * @return 转换后的字符串
     * @throws IOException
     */
    String string(@NullAble Charset charset) throws IOException;

    /**
     * 将response中的流转以图片格式输出。
     * @return 转换后的图片
     * @throws IOException
     */
    BufferedImage image() throws IOException;

    /**
     * http响应中的contentType.
     * @return contentType
     */
    String contentType();

    /**
     * http响应的数据流
     * @return 数据流，该流是一个{@link java.io.ByteArrayInputStream}，支持流复用，HTTP请求关闭后该流也可以正常使用。
     * @throws IOException
     */
    InputStream stream() throws IOException;

    /**
     * 将response中的流转以byte[]格式输出。
     * @return 转换后的字节数组
     * @throws IOException
     */
    byte[] bytes() throws IOException;

    /**
     * http响应中的编码
     * @return 编码格式
     */
    String encoding();

    /**
     * 获取所有的http响应头
     * @return http响应头
     */
    Map<String,List<String>> getHeaders();

    /**
     * 获取http响应头
     * @param key 头名称
     * @return 响应头的值
     */
    List<String> getHeader(String key);

    /**
     * 获取对应的client对象，可能为空
     * @return client对象
     */
    Client getClient();

    /**
     * 获取对应的请求Request对象
     * @return request对象。
     */
    Request getRequest();
}
