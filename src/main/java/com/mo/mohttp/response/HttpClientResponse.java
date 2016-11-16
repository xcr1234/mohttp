package com.mo.mohttp.response;


import com.mo.mohttp.Request;
import com.mo.mohttp.misc.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientResponse extends AbstractResponse {

    private HttpResponse httpResponse;

    private InputStream inputStream;

    public HttpClientResponse(HttpResponse httpResponse, Request request) throws IOException {
        super(request);
        this.httpResponse = httpResponse;
        inputStream = IOUtils.buffer(httpResponse.getEntity().getContent());
    }

    public int statusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public String contentType() {
        return httpResponse.getEntity().getContentType().getValue();
    }

    public InputStream stream() throws IOException {
        return inputStream;
    }

    public String encoding() {
        return httpResponse.getEntity().getContentEncoding().getValue();
    }
    public Map<String,List<String>> getHeaders(){
        Map<String,List<String>> map = new HashMap<String, List<String>>();
        Header[] headers = httpResponse.getAllHeaders();
        for(Header header:headers){
            String key = header.getName();
            List<String> list = new ArrayList<String>();
            for(HeaderElement headerElement:header.getElements()){
                list.add(headerElement.toString());
            }
            map.put(key,list);
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpClientResponse that = (HttpClientResponse) o;

        return httpResponse != null ? httpResponse.equals(that.httpResponse) : that.httpResponse == null;

    }

    @Override
    public int hashCode() {
        return httpResponse != null ? httpResponse.hashCode() : 0;
    }
}
