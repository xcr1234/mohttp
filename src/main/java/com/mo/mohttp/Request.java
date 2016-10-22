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


/**
 * Request请求对象，封装了http请求。
 */
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


    /**
     * 以无client方式创建请求
     * @param uri 请求的uri.
     */
    public Request(String uri){
        this(null,uri);
    }
    /**
     * 以无client方式创建GET请求
     * @param uri 请求的uri.
     */
    public Request(URI uri){
        this(null,uri);
    }

    /**
     * 创建GET请求
     * @param client 客户端对象
     * @param uri 请求的uri.
     */
    public Request(Client client,String uri){
        if(uri==null){
            throw new NullPointerException("null uri");
        }
        init();
        this.client = client;
        this.uri = URI.create(uri);
    }

    /**
     * 创建GET请求
     * @param client 客户端对象
     * @param uri 请求的uri.
     */
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

    /**
     * 设置请求的uri
     * @param uri uri
     * @return request
     */
    public Request uri(URI uri){
        this.uri = uri;
        return this;
    }
    /**
     * 设置请求的uri
     * @param uri uri
     * @return request
     */
    public Request uri(String uri){
        this.uri = URI.create(uri);
        return this;
    }
    /**
     * 设置请求的方法
     * @param method DELETE, GET,POST,PUT,HEAD
     * @return request
     */
    public Request method(Http.Method method){
        this.method = method;
        return this;
    }

    /**
     * 添加请求头
     * @param name 请求头名称，可以自定义值或取{@link Headers}中的常量
     * @param value 请求头的值
     * @return request
     */
    public Request header(String name,String value){
        headerList.add(new NameValuePair(name,value));
        return this;
    }

    /**
     * 文件上传功能，添加要上传的文件
     * @param name 文件的字段名
     * @param file 文件内容
     * @return request
     */
    public Request file(String name, File file){
        fileList.add(new NameFilePair(name,file));
        return this;
    }

    /**
     * 添加请求参数
     * @param key 参数名
     * @param value 参数值
     * @return request
     */
    public Request param(String key,String value){
        paramList.add(new NameValuePair(key,value));
        return this;
    }

    /**
     * 移除所有请求参数
     * @return request
     */
    public Request clearParams(){
        paramList.clear();
        return this;
    }

    /**
     * 移除文件上传中的文件
     * @return request
     */
    public Request clearFiles(){
        fileList.clear();
        return this;
    }

    /**
     * 清除所有内容
     * @return request
     */
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

    /**
     * 请求请求头
     * @return request
     */
    public Request clearHeaders(){
        headerList.clear();
        return this;
    }

    /**
     * 设置请求的字符串编码格式
     * @param charset 编码格式
     * @return request
     */
    public Request encoding(Charset charset){
        this.charset = charset;
        return this;
    }
    /**
     * 设置请求的字符串编码格式
     * @param encoding 编码格式
     * @return request
     */
    public Request encoding(String encoding){
        this.charset = Charset.forName(encoding);
        return this;
    }

    /**
     * 设置最大超时时间
     * @param timeout 超时时间，单位为毫秒。
     * @return request
     */
    public Request timeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    /**
     * 设置HTTP代理
     * @param proxy HTTP代理对象
     * @return request
     */
    public Request proxy(Proxy proxy){
        this.proxy = proxy;
        return this;
    }

    /**
     * 设置user agent.
     * @param agent 可以自定值或者取{@link com.mo.mohttp.constant.Agents}中的常量。
     * @return request
     */
    public Request agent(String agent){
        this.agent = agent;
        return this;
    }

    /**
     * 是否允许重定向
     * @param allowRedirect 是否允许重定向
     * @return request
     */
    public Request allowRedirect(boolean allowRedirect){
        this.allowRedirect = allowRedirect;
        return this;
    }

    @Deprecated
    public StringBuilder getStringEntity() {
        return stringEntity;
    }

    /**
     * 向请求中直接写入字符串实体内容，此时将不能再写入请求参数。
     * 设置后建议手动调用header({@link Headers#contentType},"content type")设置contentType
     * @param str 字符串实体
     * @return request
     */
    public StringBuilder stringEntity(String str){
        return stringEntity.append(str);
    }

    /**
     * 获得字符串实体{@link StringBuilder}
     * @return StringBuilder
     */
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

    /**
     * 向请求中直接写入xml实体内容。
     * 并设置content Type为{@link ContentType#XML}
     * @param xml xml内容
     * @return request
     */
    public Request xmlContent(String xml){
        stringEntity().append(xml);
        return header(Headers.contentType, ContentType.XML);
    }

    /**
     * 向请求中直接写入json实体内容。
     * 并设置content Type为{@link ContentType#JSON}
     * @param json json内容
     * @return request
     */
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

    /**
     * 以{@link URLConnection}的方式执行request请求
     * @return response 执行的结果
     * @throws IOException 执行过程中发生异常
     * @throws URISyntaxException url格式错误
     * @throws NullPointerException uri为空时抛出该异常
     */
    public Response execURL() throws IOException,URISyntaxException{
        if(uri == null){
            throw new NullPointerException("null uri!");
        }
        return UrlConnectionExecutor.getInstance().execute(this);
    }

    /**
     * 如果classpath中有{@link org.apache.http.client.HttpClient}，则以HttpClient方式执行；
     * 否则以{@link URLConnection}的方式执行request请求。
     * @return response 执行的结果
     * @throws IOException 执行过程中发生异常
     * @throws URISyntaxException url格式错误
     * @throws NullPointerException uri为空时抛出该异常
     */
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
