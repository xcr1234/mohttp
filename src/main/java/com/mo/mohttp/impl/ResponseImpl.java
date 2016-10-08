package com.mo.mohttp.impl;

import com.mo.mohttp.Client;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.misc.ByteArrayOutputStream;
import com.mo.mohttp.misc.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public abstract class ResponseImpl implements Response {

    private Request request;

    protected ResponseImpl(Request request){
        this.request = request;
    }

    @Override
    public Client getClient() {
        return request.getClient();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    public String string() throws IOException {
        return IOUtils.toString(stream(), Charset.defaultCharset());
    }

    public String string(String encoding) throws IOException {
        return IOUtils.toString(stream(),encoding);
    }

    @Override
    public String string(Charset charset) throws IOException {
        return IOUtils.toString(stream(),charset);
    }

    public BufferedImage image() throws IOException {
        return ImageIO.read(stream());
    }

    public byte[] bytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(stream(),byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public List<String> getHeader(String key) {
        return getHeaders().get(key);
    }
}
