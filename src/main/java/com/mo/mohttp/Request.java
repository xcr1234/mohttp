package com.mo.mohttp;



import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.http.NameFilePair;
import com.mo.mohttp.http.NameValuePair;

import com.mo.mohttp.impl.HttpClientExecutor;
import com.mo.mohttp.impl.UrlConnectionExecutor;


import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



public class Request {
    private URI uri;

    private Http.Method method;

    private List<NameValuePair> headerList;

    private List<NameValuePair> paramList;

    private List<NameFilePair> fileList;

    private Client client;

    private Proxy proxy;

    private Integer timeout;

    private Charset charset;

    private String agent;

    private Boolean allowRedirect;

    private StringBuilder stringEntity;



    public Request(String uri){
        this(null,uri);
    }

    public Request(URI uri){
        this(null,uri);
    }


    public Request(Client client,String uri){
        if(uri==null){
            throw new NullPointerException("null uri");
        }
        init();
        this.client = client;
        this.uri = URI.create(uri);
    }


    public Request(Client client,URI uri){
        if(uri==null){
            throw new NullPointerException("null uri");
        }
        init();
        this.client = client;
        this.uri = uri;
    }



    private void init(){
        method = Http.Method.GET;
        headerList = new ArrayList<>();
        paramList = new ArrayList<>();
        fileList = new ArrayList<>();
    }

    public Request uri(URI uri){
        this.uri = uri;
        return this;
    }

    public Request uri(String uri){
        this.uri = URI.create(uri);
        return this;
    }

    public Request method(Http.Method method){
        this.method = method;
        return this;
    }

    public Request header(String name,String value){
        headerList.add(new NameValuePair(name,value));
        return this;
    }

    public Request file(String name, File file){
        fileList.add(new NameFilePair(name,file));
        return this;
    }

    public Request param(String key,String value){
        paramList.add(new NameValuePair(key,value));
        return this;
    }

    public Request clearParams(){
        paramList.clear();
        return this;
    }

    public Request clearFiles(){
        fileList.clear();
        return this;
    }

    public Request clear(){
        headerList.clear();
        paramList.clear();
        fileList.clear();
        method = Http.Method.GET;
        proxy = null;
        timeout = null;
        charset = null;
        agent = null;
        allowRedirect = null;
        stringEntity = null;

        return this;
    }

    public Request clearHeaders(){
        headerList.clear();
        return this;
    }

    public Request encoding(Charset charset){
        this.charset = charset;
        return this;
    }

    public Request encoding(String encoding){
        this.charset = Charset.forName(encoding);
        return this;
    }

    public Request timeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    public Request proxy(Proxy proxy){
        this.proxy = proxy;
        return this;
    }

    public Request agent(String agent){
        this.agent = agent;
        return this;
    }

    public Request allowRedirect(boolean allowRedirect){
        this.allowRedirect = allowRedirect;
        return this;
    }

    @Deprecated
    public StringBuilder getStringEntity() {
        return stringEntity;
    }

    public StringBuilder stringEntity(String str){
        return stringEntity.append(str);
    }

    public StringBuilder stringEntity() {
        if(this.method == Http.Method.GET){
            throw new IllegalStateException("cannot write string entity to GET method.");
        }
        if(!fileList.isEmpty()||!paramList.isEmpty()){
            throw new IllegalStateException("cannot get string entity while file or param entity is not empty!");
        }
        if(stringEntity == null){
            stringEntity = new StringBuilder();
        }
        return stringEntity;
    }

    public Request xmlContent(String xml){
        stringEntity().append(xml);
        return header(Headers.contentType, ContentType.XML);
    }

    public Request jsonContent(String json){
        stringEntity.append(json);
        return header(Headers.contentType,ContentType.JSON);
    }

    public Client getClient() {
        return client;
    }


    public URI getUri() {
        return uri;
    }

    public Http.Method getMethod() {
        return method;
    }

    public List<NameValuePair> getHeaderList() {
        return headerList;
    }

    public List<NameValuePair> getParamList() {
        return paramList;
    }

    public List<NameFilePair> getFileList() {
        return fileList;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Boolean getAllowRedirect() {
        return allowRedirect;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getAgent() {
        return agent;
    }

    public Response execURL() throws IOException,URISyntaxException{
        if(uri == null){
            throw new NullPointerException("null uri!");
        }
        return UrlConnectionExecutor.getInstance().execute(this);
    }

    public Response execute() throws IOException, URISyntaxException {
        if(uri == null){
            throw new NullPointerException("null uri!");
        }
        //寻找执行器
        Executor executor = null;
        try {
            Class.forName("org.apache.http.client.HttpClient"); //如果classpath中有httpclient，则用httpclient执行器执行，如果没有则用java的urlConnection。
            executor = HttpClientExecutor.getInstance();
        }catch (ClassNotFoundException e){
            executor = UrlConnectionExecutor.getInstance();
        }
        return executor.execute(this);
    }






}
