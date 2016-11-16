package com.mo.mohttp.executor;


import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.anno.ThreadSafe;

import java.io.IOException;
import java.net.URISyntaxException;
@ThreadSafe
public interface Executor{

    Response execute(Request request)  throws IOException, URISyntaxException;

}
