package com.mo.mohttp.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface Entity {

    long getContentLength();

    long writeLength();

    String getContentType();

    Charset getContentEncoding();


    void writeTo(DataOutputStream out) throws IOException;

}
