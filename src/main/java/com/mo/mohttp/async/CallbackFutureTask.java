package com.mo.mohttp.async;

import com.mo.mohttp.Response;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class CallbackFutureTask extends FutureTask<Response> {

    private HttpCallback asyncCallback;

    public CallbackFutureTask(Callable<Response> callable, HttpCallback asyncCallback) {
        super(callable);
        this.asyncCallback = asyncCallback;
    }

    @Override
    public void done() {
        if(isCancelled()){
            asyncCallback.cancelled();
            return;
        }
        try {
            Response response = get();
            asyncCallback.complete(response);
        }catch (InterruptedException|IOException e){
            asyncCallback.failed(e);
        }catch (ExecutionException ex){
            asyncCallback.failed(ex.getCause());
        }
    }
}
