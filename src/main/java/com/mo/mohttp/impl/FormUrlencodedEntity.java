package com.mo.mohttp.impl;


import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.http.Entity;
import com.mo.mohttp.http.NameValuePair;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

public class FormUrlencodedEntity implements Entity {

    private Charset charset;

    private List<NameValuePair> paramList;




    public FormUrlencodedEntity(List<NameValuePair> paramList, Charset encoding){
        this.charset = encoding;
        this.paramList = paramList;
    }


    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public long writeLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return ContentType.FORM_DEFAULT;
    }

    @Override
    public Charset getContentEncoding() {
        return charset;
    }


    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        for(int i=0;i<paramList.size();i++){
            NameValuePair pair = paramList.get(i);
            if(i>0){
                out.writeBytes("&");
            }
            out.writeBytes(URLEncoder.encode(pair.getName(),charset.displayName()));
            out.writeBytes("=");
            out.writeBytes(URLEncoder.encode(pair.getValue(),charset.displayName()));
        }
    }
}