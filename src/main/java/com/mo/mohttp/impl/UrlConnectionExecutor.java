package com.mo.mohttp.impl;


import com.mo.mohttp.*;

import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.http.Entity;
import com.mo.mohttp.http.NameFilePair;
import com.mo.mohttp.http.NameValuePair;
import com.mo.mohttp.misc.TextUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class UrlConnectionExecutor  implements Executor{

    private UrlConnectionExecutor(){}

    public static UrlConnectionExecutor getInstance(){
        synchronized (UrlConnectionExecutor.class){
            if(instance == null){
                instance = new UrlConnectionExecutor();
            }
            return instance;
        }
    }

    private static UrlConnectionExecutor instance ;

    @Override
    public Response execute(Request request) throws IOException, URISyntaxException {
        boolean writeData = request.getMethod() != Http.Method.GET;
        if(!writeData&&!request.getFileList().isEmpty()){
            throw new IllegalStateException("GET method does not support file upload connection!");
        }
        Charset charset = request.getCharset();
        if(request.getCharset() == null){
            charset = Charset.defaultCharset();
        }
        List<NameValuePair> paramList = request.getParamList();
        List<NameFilePair> fileList = request.getFileList();

        URI u = writeData ? request.getUri(): TextUtils.buildURI(request.getUri(),charset,request.getParamList());
        URLConnection connection = null;
        try {
            URL url = u.toURL();
            connection = request.getProxy() == null?url.openConnection():url.openConnection(request.getProxy());
            connection.setDoInput(true);
            if(writeData){
                connection.setDoOutput(true);
                connection.setUseCaches(false);
            }
            if(connection instanceof HttpURLConnection){
                HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
                httpURLConnection.setRequestMethod(request.getMethod().name());
                httpURLConnection.setInstanceFollowRedirects(false);
            }
            if(request.getTimeout()!=null){
                connection.setConnectTimeout(request.getTimeout());
                connection.setReadTimeout(request.getTimeout());
            }
            if(request.getAgent()!=null){
                connection.setRequestProperty(Headers.agent,request.getAgent());
            }
            if(request.getClient()!=null){
                Client client = request.getClient();
                if(request.getAgent()==null&&client.getUserAgent()!=null){
                    connection.setRequestProperty(Headers.agent,client.getUserAgent());
                }
                for(NameValuePair pair:client.getHeaders()){
                    connection.setRequestProperty(pair.getName(),pair.getValue());
                }
                //设置cookie
                if(client.getCookieManager()!=null){
                    CookieManager cookieManager = client.getCookieManager();
                    Map<String, List<String>> map = cookieManager.get(u,connection.getRequestProperties());
                    for(Map.Entry<String,List<String>> entry:map.entrySet()){
                        String key = entry.getKey();
                        for(String value:entry.getValue()){
                            connection.addRequestProperty(key,value);
                        }
                    }
                }


            }
            for(NameValuePair pair:request.getHeaderList()){
                connection.setRequestProperty(pair.getName(),pair.getValue());
            }

            if(writeData){
                connection.setDoOutput(true);
                Entity entity = null;
                if(fileList.isEmpty()){
                    entity = new FormUrlencodedEntity(paramList,charset);
                }else{
                    entity = new FileMultipartEntity(paramList,fileList,charset);
                }
                connection.setRequestProperty(Headers.contentType,entity.getContentType());
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                entity.writeTo(outputStream);
            }
            if (request.getClient()!=null){
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                request.getClient().getCookieManager().put(u,headerFields);
            }
            return new UrlConnectionResponse(connection);
        }finally {
            if(connection!=null&&connection instanceof HttpURLConnection){

                ((HttpURLConnection) connection).disconnect();
            }
        }
    }




}
