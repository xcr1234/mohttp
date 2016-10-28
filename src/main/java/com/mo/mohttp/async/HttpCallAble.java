package com.mo.mohttp.async;

import com.mo.mohttp.Request;
import com.mo.mohttp.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;


public class HttpCallAble implements Callable<Response> {

    private Request request;

    public HttpCallAble(Request request) {
        this.request = request;
    }

    @Override
    public Response call() throws IOException, URISyntaxException {
        return request.execute();
    }
}
