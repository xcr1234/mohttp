package com.mo.mohttp.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * <pre>
 * HTTP protocol Entity
 * https://www.w3.org/Protocols/rfc2616/rfc2616-sec7.html
 *
 * this interface is only used for {@link java.net.HttpURLConnection}
 * since that Apache HttpClient has its own impl :
 * @see org.apache.http.entity.AbstractHttpEntity
 *</pre>
 */
public interface Entity {

    long getContentLength();

    long writeLength();

    String getContentType();

    Charset getContentEncoding();


    void writeTo(DataOutputStream out) throws IOException;

}
