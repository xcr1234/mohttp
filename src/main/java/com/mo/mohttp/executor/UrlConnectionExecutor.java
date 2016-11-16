package com.mo.mohttp.executor;


import com.mo.mohttp.*;

import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.entity.Entity;
import com.mo.mohttp.entity.FileMultipartEntity;
import com.mo.mohttp.entity.FormUrlencodedEntity;
import com.mo.mohttp.entity.StringEntity;
import com.mo.mohttp.pair.NameFilePair;
import com.mo.mohttp.pair.NameValuePair;
import com.mo.mohttp.misc.TextUtils;
import com.mo.mohttp.response.UrlConnectionResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class UrlConnectionExecutor  implements Executor {

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
    @SuppressWarnings("deprecated")
    public Response execute(Request request) throws IOException, URISyntaxException {
        boolean writeData = request.getMethod().writeData();

        Client client = request.getClient();
        Charset charset = request.getCharset();
        if(request.getCharset() == null){
            charset = Charset.defaultCharset();
        }
        List<NameValuePair> paramList = request.getParamList();
        List<NameFilePair> fileList = request.getFileList();
        StringBuilder stringEntity = request.getStringEntity();
        if(!writeData&&!fileList.isEmpty()){
            throw new IllegalStateException("GET method does not support file upload request!");
        }
        if(stringEntity!=null&&(!fileList.isEmpty()||!paramList.isEmpty())){
            throw new IllegalStateException("cannot get string entity while file or param entity is not empty!");
        }

        URI u = null;
        if(writeData){
            u = request.getUri();
        }else{
            u = TextUtils.buildURI(request.getUri(),charset,request.getParamList());
        }
        URLConnection connection = null;
        try {
            URL url = u.toURL();

            Proxy proxy = null;
            if(client!=null&&client.getProxy()!=null){
                proxy = client.getProxy();
            }
            if(request.getProxy()!=null){
                proxy = request.getProxy();
            }
            connection = proxy == null?url.openConnection():url.openConnection(proxy); // open url connection
            connection.setDoInput(true);
            if(writeData){
                connection.setDoOutput(true);
                connection.setUseCaches(false);
            }
            if(connection instanceof HttpURLConnection){ // this will always happen.
                HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
                httpURLConnection.setRequestMethod(request.getMethod().name());
                if(request.getAllowRedirect()!=null){
                    httpURLConnection.setInstanceFollowRedirects(request.getAllowRedirect());
                }else {
                    httpURLConnection.setInstanceFollowRedirects(false);
                }
            }
            if(request.getTimeout()!=null){
                connection.setConnectTimeout(request.getTimeout());
                connection.setReadTimeout(request.getTimeout());
            }
            if(request.getAgent()!=null){
                connection.setRequestProperty(Headers.agent,request.getAgent());
            }
            if(request.getClient()!=null){
                if(request.getAgent()==null&&client.getUserAgent()!=null){
                    connection.setRequestProperty(Headers.agent,client.getUserAgent());
                }
                for(NameValuePair pair:client.getHeaders()){
                    connection.setRequestProperty(pair.getName(),pair.getValue());
                }
                //set cookies
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
            boolean flag = true; // whether contentType has been written in header fields;
            for(NameValuePair pair:request.getHeaderList()){
                connection.setRequestProperty(pair.getName(),pair.getValue());
                if(Headers.contentType.equalsIgnoreCase(pair.getName())){
                    flag = false;
                }
            }

            if(writeData){
                connection.setDoOutput(true);
                Entity entity = null; // data entity
                if(fileList.isEmpty()){
                    entity = new FormUrlencodedEntity(paramList,charset);
                }else{
                    entity = new FileMultipartEntity(paramList,fileList,charset);
                }

                if(stringEntity!=null){
                    entity = new StringEntity(stringEntity.toString());
                }

                if(flag) connection.setRequestProperty(Headers.contentType,entity.getContentType());
                    // if content type has not been written in header fields,write entity default content type ( always ContentType.FORM_DEFAULT).
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                entity.writeTo(outputStream);


            }
            if (request.getClient()!=null){
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                request.getClient().getCookieManager().put(u,headerFields); //automatically put cookies in response headers to cookie store.
            }

            return new UrlConnectionResponse(connection,request);
        }finally {
            if(connection!=null&&connection instanceof HttpURLConnection){

                ((HttpURLConnection) connection).disconnect();
            }
        }
    }




}
