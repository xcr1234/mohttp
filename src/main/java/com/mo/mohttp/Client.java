package com.mo.mohttp;


import com.mo.mohttp.http.NameValuePair;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
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
 * org.apache.http.client.CookieStore cookieStore =
    HttpClientExecutor.getInstance().getCookieStore(client);
 * @see CookieManager
 * @see CookieStore
 * @see org.apache.http.client.CookieStore
 */
public class Client {

    private CookieManager cookieManager;

    private List<NameValuePair> headers;

    private String userAgent;

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

    public Client header(String key,String value){
        headers.add(new NameValuePair(key,value));
        return this;
    }

    public Client agent(String agent){
        this.userAgent = agent;
        return this;

    }

    public Request GET(URI uri){
        return uri(uri);
    }

    public Request GET(String uri){
        return uri(uri);
    }

    public Request POST(URI uri){
        return uri(uri).method(Http.Method.POST);
    }

    public Request POST(String uri){
        return uri(uri).method(Http.Method.POST);
    }

    public CookieStore getCookieStore(){
        return cookieManager.getCookieStore();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public Request uri(URI uri){
        return new Request(this,uri);
    }

    public Request uri(String uri){
        return new Request(this,uri);
    }
}
