package com.mo.mohttp;


import java.net.URI;

/**
 * 进行http(https)请求的公共类。
 */
public final class Http {

    private Http(){}

    public enum Method{
        DELETE, GET,POST,PUT,HEAD;
    }

    public static Client newClient(){
        return new Client();
    }


    public static Request GET(URI uri){
        return new Request(null,uri);
    }

    public static Request GET(String uri){
        return new Request(null,uri);
    }

    public static Request POST(String uri){
        return new Request(null,uri).method(Method.POST);
    }

    public static Request POST(URI uri){
        return new Request(null,uri).method(Method.POST);
    }

    /**
     * 如果在使用https时 出现如下错误：{@link javax.net.ssl.SSLProtocolException}: handshake alert:  unrecognized_name，则需要调用一次该方法解决。
     */
    public static void httpsHandshake(){
        System.setProperty("jsse.enableSNIExtension", "false");
    }

}
