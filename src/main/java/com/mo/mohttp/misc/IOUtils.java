package com.mo.mohttp.misc;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class IOUtils {

    private IOUtils(){}

    public static final int EOF = -1;

    private static final int BUF = 1024 * 4;

    public static String toString(InputStream inputStream, String encoding) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream,byteArrayOutputStream);
        return byteArrayOutputStream.toString(encoding);
    }


    public static String toString(InputStream inputStream, Charset charset) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream,byteArrayOutputStream);
        return byteArrayOutputStream.toString(charset);
    }




    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte [] bytes = new byte[BUF];
        int len;
        while((len = inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
        }
    }

    public static InputStream buffer(InputStream inputStream) throws IOException{
        return ByteArrayOutputStream.toBufferedInputStream(inputStream);
    }




}
