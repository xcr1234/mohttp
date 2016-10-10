package com.mo.mohttp.impl;


import com.mo.mohttp.Request;
import com.mo.mohttp.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class UrlConnectionResponse extends ResponseImpl {

    private URLConnection urlConnection;

    private InputStream inputStream;

    public UrlConnectionResponse(URLConnection urlConnection, Request request) throws IOException {
        super(request);
        this.urlConnection = urlConnection;
        this.inputStream = IOUtils.buffer(urlConnection.getInputStream());
    }

    public int statusCode() throws IOException {
        if(urlConnection instanceof HttpURLConnection){
            return ((HttpURLConnection) urlConnection).getResponseCode();
        }
        throw new UnsupportedOperationException("Only HttpURLConnection can get status code.");
    }

    public String contentType() {
        return urlConnection.getContentType();
    }

    public InputStream stream() {
        return inputStream;
    }

    public String encoding() {
        return urlConnection.getContentEncoding();
    }

    public Map<String,List<String>> getHeaders(){
        return urlConnection.getHeaderFields();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlConnectionResponse that = (UrlConnectionResponse) o;

        return urlConnection != null ? urlConnection.equals(that.urlConnection) : that.urlConnection == null;

    }

    @Override
    public int hashCode() {
        return urlConnection != null ? urlConnection.hashCode() : 0;
    }
}
