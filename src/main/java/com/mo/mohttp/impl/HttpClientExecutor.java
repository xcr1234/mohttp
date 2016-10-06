package com.mo.mohttp.impl;


import com.mo.mohttp.*;
import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.http.NameFilePair;
import com.mo.mohttp.http.NameValuePair;
import com.mo.mohttp.misc.TextUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClientExecutor implements Executor{

    private HttpClientExecutor(){}

    public static HttpClientExecutor getInstance(){
        synchronized (HttpClientExecutor.class){
            if(instance == null){
                instance =  new HttpClientExecutor();
            }
            return instance;
        }
    }

    public CookieStore getCookieStore(Client client){
        return clientCookieStoreMap.get(client);
    }

    private static HttpClientExecutor instance;

    private Map<Client,CookieStore> clientCookieStoreMap = new ConcurrentHashMap<>();

    @Override
    public Response execute(Request request)  throws IOException, URISyntaxException {
        Client client = request.getClient();
        HttpClient httpClient = null;
        if(client!=null){
            CookieStore cookieStore = clientCookieStoreMap.get(client);
            if(cookieStore==null){
                clientCookieStoreMap.put(client,new BasicCookieStore());
            }
            HttpClientBuilder builder = HttpClients.custom().setDefaultCookieStore(cookieStore);
            if(client.getHeaders()!=null){
                List<Header> headerList = new ArrayList<>();
                for(NameValuePair nameValuePair:client.getHeaders()){
                    headerList.add(new BasicHeader(nameValuePair.getName(),nameValuePair.getValue()));
                }
                builder.setDefaultHeaders(headerList);
            }
            if(client.getUserAgent()!=null){
                builder.setUserAgent(client.getUserAgent());
            }
            httpClient = builder.build();
        }else{
            httpClient = HttpClients.createDefault();
        }

        Charset charset = request.getCharset();
        if(request.getCharset() == null){
            charset = Charset.defaultCharset();
        }
        Http.Method method = request.getMethod();
        URI uri = request.getUri();
        List<NameValuePair> paramList = request.getParamList();
        List<NameFilePair> fileList = request.getFileList();
        Integer time = request.getTimeout();

        HttpRequestBase httpRequestBase = null;
        boolean writeData = false;
        if(method== Http.Method.GET){
            httpRequestBase = new HttpGet(TextUtils.buildURI(uri,charset,paramList));
        }else{
            httpRequestBase = new MoHttpRequestBase(method);
            httpRequestBase.setURI(uri);
            writeData = true;
        }
        if(!fileList.isEmpty()&&!writeData){
            throw new IllegalStateException("GET method does not support file upload connection!");
        }

        for(NameValuePair pair:request.getHeaderList()){
            httpRequestBase.addHeader(pair.getName(),pair.getValue());
        }

        RequestConfig.Builder builder = RequestConfig.custom();
        //设置 timeout
        if(time!=null){
            builder.setConnectTimeout(time).setSocketTimeout(time).setConnectionRequestTimeout(time);
        }
        if(request.getProxy()!=null){
            Proxy proxy = request.getProxy();
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            HttpHost httpHost = new HttpHost(address.getAddress());
            builder.setProxy(httpHost);
        }
        RequestConfig requestConfig = builder.build();
        httpRequestBase.setConfig(requestConfig);
        if(writeData){
            MoHttpRequestBase mo = (MoHttpRequestBase)httpRequestBase;
            HttpEntity entity = null;
            if(fileList.isEmpty()){
                List<org.apache.http.NameValuePair> nvps = new ArrayList<org.apache.http.NameValuePair>();
                for(NameValuePair pair:paramList){
                    nvps.add(new BasicNameValuePair(pair.getName(),pair.getValue()));
                }
                entity = new UrlEncodedFormEntity(nvps,charset);
            }else{
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                for(NameFilePair pair:fileList){
                    String filename = pair.getValue().getName();
                    String content = ContentType.findMimeByExtension(TextUtils.fileExt(filename));
                    entityBuilder.addBinaryBody(pair.getName(),pair.getValue(), org.apache.http.entity.ContentType.create(content),filename);
                }
                for(NameValuePair pair:paramList){
                    entityBuilder.addTextBody(pair.getName(),pair.getValue());
                }
                entity = entityBuilder.build();
            }
            mo.setEntity(entity);
        }
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequestBase);
            return new HttpClientResponse(httpResponse);
        }finally {
            httpRequestBase.abort();
        }
    }


}
