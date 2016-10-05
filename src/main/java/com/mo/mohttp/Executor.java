package com.mo.mohttp;


import java.io.IOException;
import java.net.URISyntaxException;

public interface Executor{

    Response execute(Request request)  throws IOException, URISyntaxException;

}
