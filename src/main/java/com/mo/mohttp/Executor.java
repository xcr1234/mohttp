package com.mo.mohttp;


import com.mo.mohttp.anno.ThreadSafe;

import java.io.IOException;
import java.net.URISyntaxException;
@ThreadSafe
public interface Executor{

    Response execute(Request request)  throws IOException, URISyntaxException;

}
