package com.mo.mohttp;


import com.mo.mohttp.http.NameValuePair;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

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
        return new Request(this).uri(uri);
    }

    public Request uri(String uri){
        return new Request(this).uri(uri);
    }
}
