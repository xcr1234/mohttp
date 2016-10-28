package com.mo.mohttp.misc;

import static com.mo.mohttp.misc.Classes.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class IOUtils {

    private IOUtils(){}

    public static final int EOF = -1;

    private static final int BUF = 1024 * 4;

    public static String toString(InputStream inputStream, String encoding) throws IOException{
        if(inClasspath(commonsIO)){
            return org.apache.commons.io.IOUtils.toString(inputStream,encoding);
        }else{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            copy(inputStream,byteArrayOutputStream);
            return byteArrayOutputStream.toString(encoding);
        }
    }


    public static String toString(InputStream inputStream, Charset charset) throws IOException{
        if (inClasspath(commonsIO)) {
           return org.apache.commons.io.IOUtils.toString(inputStream,charset);
        }else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            copy(inputStream,byteArrayOutputStream);
            return byteArrayOutputStream.toString(charset);
        }
    }




    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException{
        if(inClasspath(commonsIO)){
            org.apache.commons.io.IOUtils.copy(inputStream,outputStream);
        }else{
            byte [] bytes = new byte[BUF];
            int len;
            while((len = inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
        }
    }

    public static InputStream buffer(InputStream inputStream) throws IOException{
        if(inClasspath(commonsIO)){
            return org.apache.commons.io.output.ByteArrayOutputStream.toBufferedInputStream(inputStream);
        }else{
            return ByteArrayOutputStream.toBufferedInputStream(inputStream);
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        if(inClasspath(commonsIO)){
            return org.apache.commons.io.IOUtils.toByteArray(input);
        }else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(input,byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }






}
