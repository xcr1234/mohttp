package com.mo.mohttp;



import com.mo.mohttp.anno.NotNull;
import com.mo.mohttp.anno.NotThreadSafe;
import com.mo.mohttp.anno.NullAble;
import com.mo.mohttp.anno.ThreadSafe;
import com.mo.mohttp.async.HttpCallAble;
import com.mo.mohttp.async.HttpCallback;
import com.mo.mohttp.async.CallbackFutureTask;
import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.http.NameFilePair;
import com.mo.mohttp.http.NameValuePair;

import com.mo.mohttp.impl.HttpClientExecutor;
import com.mo.mohttp.impl.UrlConnectionExecutor;
import com.mo.mohttp.misc.Args;
import com.mo.mohttp.misc.Classes;
import static com.mo.mohttp.misc.Classes.*;


import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Request请求对象，封装了http请求。
 */
@NotThreadSafe
public class Request {

    static {
        Classes.checkVersion();
    }

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
    public Request(@NotNull String uri){
        this(null,uri);
    }
    /**
     * 以无client方式创建GET请求
     * @param uri 请求的uri.
     */
    public Request(@NotNull URI uri){
        this(null,uri);
    }

    /**
     * 创建GET请求
     * @param client 客户端对象
     * @param uri 请求的uri.
     */
    public Request(@NullAble Client client, @NotNull String uri){
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
    public Request(@NullAble Client client,@NotNull URI uri){
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

        agent = Agents.MOHTTP_DEFAULT;   // :-D  help us to popularize this project.
    }

    /**
     * 设置请求的uri
     * @param uri uri
     * @return request
     */
    public Request uri(@NotNull URI uri){
        Args.notNull(uri,"uri");
        this.uri = uri;
        return this;
    }
    /**
     * 设置请求的uri
     * @param uri uri
     * @return request
     */
    public Request uri(@NotNull String uri){
        this.uri = URI.create(uri);
        return this;
    }
    /**
     * 设置请求的方法
     * @param method DELETE, GET,POST,PUT,HEAD
     * @return request
     */
    public Request method(@NotNull Http.Method method){
        Args.notNull(method,"method");
        this.method = method;
        return this;
    }

    /**
     * 添加请求头
     * @param name 请求头名称，可以自定义值或取{@link Headers}中的常量
     * @param value 请求头的值
     * @return request
     */
    public Request header(@NotNull String name,@NullAble String value){
        Args.notNull(name,"head field name");
        headerList.add(new NameValuePair(name,value));
        return this;
    }

    /**
     * 文件上传功能，添加要上传的文件
     * @param name 文件的字段名
     * @param file 文件内容
     * @return request
     */
    public Request file(@NotNull String name,@NotNull  File file){
        Args.notNull(name,"parameter name");
        Args.notNull(file,"upload file");
        if(!file.exists()){
            throw new IllegalArgumentException("file not exists.");
        }
        if(file.isDirectory()){
            throw new IllegalArgumentException("file cannot be directory.");
        }
        fileList.add(new NameFilePair(name,file));
        return this;
    }

    /**
     * 添加请求参数
     * @param key 参数名
     * @param value 参数值
     * @return request
     */
    public Request param(@NotNull String key,@NullAble String value){
        Args.notNull(key,"parameter name");
        paramList.add(new NameValuePair(key,value));
        return this;
    }

    /**
     * <pre>
     * 以 “name=value”的形式添加请求参数
     * 例如
     * abc=10:name = "abc",value = 10 ;
     * abc= : name = "abc" value = "";
     * abc ; name = "abc" value = null;
     * abc==10: name="abc" value = "=10"
     * null/emptyString : throws {@link IllegalArgumentException}
     * </pre>
     * @param param name=value
     * @return request
     */
    public Request param(@NotNull String param){
        Args.notNull(param,"parameter");
        if(param.isEmpty()){
            throw new IllegalArgumentException("parameter may not be empty!");
        }
        String name = null,value = null;
        int n = param.indexOf('=');
        if(n==-1){
            name = param;
        }else{
            name = param.substring(0,n);
            value = param.substring(n+1);
        }
        paramList.add(new NameValuePair(name,value));
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
     * 设置request请求的字符串编码格式
     * @param charset 编码格式，如果为空则取{@link Charset#defaultCharset()}
     * @return request
     */
    public Request encoding(@NotNull Charset charset){
        Args.notNull(charset,"request encoding charset");
        this.charset = charset;
        return this;
    }
    /**
     * 设置request请求的字符串编码格式
     * @param encoding 编码格式，如果为空则取{@link Charset#defaultCharset()}
     * @return request
     */
    public Request encoding(@NotNull String encoding){
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
    public Request proxy(@NullAble Proxy proxy){
        this.proxy = proxy;
        return this;
    }

    /**
     * 设置user agent.
     * @param agent 可以自定值或者取{@link com.mo.mohttp.constant.Agents}中的常量。默认值为{@link Agents#MOHTTP_DEFAULT}
     * @return request
     */
    public Request agent(@NullAble String agent){
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
    public StringBuilder stringEntity(@NotNull String str){
        Args.notNull(str,"string");
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
    public Request xmlContent(@NotNull String xml){
        Args.notNull(xml,"xml content");
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
        Args.notNull(json,"json content");
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
    @ThreadSafe
    public Response execute() throws IOException, URISyntaxException {
        if(uri == null){
            throw new NullPointerException("null uri!");
        }
        //寻找执行器
        Executor executor = inClasspath(httpClient) ? HttpClientExecutor.getInstance() : UrlConnectionExecutor.getInstance();
        return executor.execute(this);
    }

    /**
     * 异步执行request请求
     * @param executorService 线程池
     * @param callback 执行的回调函数
     * @return 执行的future.
     */
    @ThreadSafe
    @SuppressWarnings("unchecked")
    public Future<Response> execute(@NotNull ExecutorService executorService,@NotNull  HttpCallback callback){
        Args.notNull(executorService,"Executor service");
        Args.notNull(callback,"async request callback");
        HttpCallAble callAble = new HttpCallAble(this);
        CallbackFutureTask futureTask = new CallbackFutureTask(callAble,callback);
        return (Future<Response>) executorService.submit(futureTask);
    }

    /**
     * 新建一个{@link Executors#newCachedThreadPool()}，用该线程池执行异步request请求。
     * 每次请求都使用同一个线程池。
     * @param callback 回调函数
     * @return 执行的future
     */
    @ThreadSafe
    public Future<Response> execute(@NotNull HttpCallback callback){
        return execute(Http.getExecutorService(),callback);
    }






}
