package com.mo.mohttp;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface Response {

    int statusCode() throws IOException;

    String string() throws IOException;

    String string(String encoding) throws IOException;

    String string(Charset charset) throws IOException;

    BufferedImage image() throws IOException;

    String contentType();

    InputStream stream() throws IOException;

    byte[] bytes() throws IOException;

    String encoding();

    Map<String,List<String>> getHeaders();

    List<String> getHeader(String key);

    Client getClient();

    Request getRequest();
}