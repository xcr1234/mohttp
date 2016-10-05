package com.mo.mohttp.impl;


import com.mo.mohttp.Http;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class MoHttpRequestBase extends HttpEntityEnclosingRequestBase {

    private Http.Method method;

    public MoHttpRequestBase(Http.Method method) {
        this.method = method;
    }


    public String getMethod() {
        return method.name();
    }

    public MoHttpRequestBase(){
        super();
    }

    public MoHttpRequestBase(String uri){
        super();
        setURI(URI.create(uri));
    }

    public MoHttpRequestBase(URI uri){
        super();
        setURI(uri);
    }


}
