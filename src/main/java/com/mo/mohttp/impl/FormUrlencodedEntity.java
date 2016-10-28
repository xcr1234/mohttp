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

/**
 * <pre>
 * An entity composed of a list of url-encoded pairs. This is typically useful while sending an HTTP POST request.
 * For example:
 *
 * parameters:
 * a = 1
 * b = 2
 * c = 100
 *
 * FormUrlencodedEntity:
 * a=1&amp;b=2&amp;c=100
 * </pre>
 *
 */
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
            String name = pair.getName();
            String value = pair.getValue();
            if(i>0){
                out.writeBytes("&");
            }
            out.writeBytes(URLEncoder.encode(name,charset.displayName()));
            if(value!=null){
                out.writeBytes("=");
                out.writeBytes(URLEncoder.encode(pair.getValue(),charset.displayName()));
            }
        }
    }
}
