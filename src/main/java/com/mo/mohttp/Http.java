package com.mo.mohttp;


import java.net.URI;

/**
 * 进行http(https)请求的公共类。
 */
public class Http {

    public enum Method{
        DELETE, GET,POST,PUT,HEAD;
    }

    public static Client newClient(){
        return new Client();
    }


    public static Request GET(URI uri){
        return new Request(null).uri(uri);
    }

    public static Request GET(String uri){
        return new Request(null).uri(uri);
    }

    public static Request POST(String uri){
        return new Request(null).uri(uri).method(Method.POST);
    }

    public static Request POST(URI uri){
        return new Request(null).uri(uri).method(Method.POST);
    }

}
