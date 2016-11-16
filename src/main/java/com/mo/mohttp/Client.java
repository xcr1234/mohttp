package com.mo.mohttp;


import com.mo.mohttp.anno.NotNull;
import com.mo.mohttp.anno.NullAble;
import com.mo.mohttp.anno.ThreadSafe;
import com.mo.mohttp.pair.NameValuePair;
import com.mo.mohttp.misc.Args;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Http客户端连接，用于存储cookie和一些公共的配置信息。
 *
 * 实例方法：
 * Client client = new Client();
 * 或者
 * Client client = Http.newClient();
 *
 * 从client中得到存储的cookie：
 * java.net.CookieManager cookieManager = client.getCookieManager();
 * java.net.CookieStore cookieStore = client.getCookieStore();
 * 如果classpath中有httpclient，则应该是：
 * org.apache.pair.client.CookieStore cookieStore =
    HttpClientExecutor.getInstance().getCookieStore(client);
 * @see CookieManager
 * @see CookieStore
 * @see org.apache.http.client.CookieStore
 * </pre>
 */
@ThreadSafe
public class Client {

    private CookieManager cookieManager;

    private List<NameValuePair> headers;

    private String userAgent;

    private Proxy proxy;

    public Client(){
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        headers = new ArrayList<>();
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }

    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 向Client客户端中存入默认的header字段
     * @param key 字段名，可取{@link com.mo.mohttp.constant.Headers}中的值，或者自定义值
     * @param value 字段值
     * @return client
     */
    public Client header(@NotNull String key,@NullAble String value){
        Args.notNull("key","header field key");
        headers.add(new NameValuePair(key,value));
        return this;
    }

    /**
     * 设置client中默认的user agent
     * @param agent 可取{@link com.mo.mohttp.constant.Agents}中的常量值
     * @return client
     */
    public Client agent(@NullAble String agent){
        this.userAgent = agent;
        return this;

    }

    public Client proxy(@NullAble Proxy proxy){
        this.proxy = proxy;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Request GET(@NotNull URI uri){
        return uri(uri);
    }

    public Request GET(@NotNull String uri){
        return uri(uri);
    }

    public Request POST(@NotNull URI uri){
        return uri(uri).method(Http.Method.POST);
    }

    public Request POST(@NotNull String uri){
        return uri(uri).method(Http.Method.POST);
    }

    public CookieStore getCookieStore(){
        return cookieManager.getCookieStore();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public Request uri(@NotNull URI uri){
        return new Request(this,uri);
    }

    public Request uri(@NotNull String uri){
        return new Request(this,uri);
    }
}
