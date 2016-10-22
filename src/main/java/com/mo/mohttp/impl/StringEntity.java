package com.mo.mohttp.impl;


import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.http.Entity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * simple string entity
 */
public class StringEntity implements Entity{

    private String stringContent;


    public StringEntity(String stringContent) {
        this.stringContent = stringContent;
    }

    @Override
    public long getContentLength() {
        return stringContent.length();
    }

    @Override
    public long writeLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return ContentType.TXT;
    }

    @Override
    public Charset getContentEncoding() {
        return Charset.defaultCharset();
    }

    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        out.writeBytes(stringContent);
    }
}
