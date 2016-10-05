package com.mo.mohttp.impl;

import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.http.Entity;
import com.mo.mohttp.http.NameFilePair;
import com.mo.mohttp.http.NameValuePair;
import com.mo.mohttp.misc.TextUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class FileMultipartEntity implements Entity {

    private String boundary;

    private List<NameFilePair> nameFilePairs;

    private List<NameValuePair> nameValuePairs;

    private Charset charset;

    private String nb;

    public FileMultipartEntity(List<NameValuePair> nameValuePairList, List<NameFilePair> nameFilePairs,Charset charset){
        boundary = "---------------------------" + TextUtils.randomString(13);
        nb = "--"+boundary;
        this.nameFilePairs = nameFilePairs;
        this.nameValuePairs = nameValuePairList;
        this.charset = charset;
    }

    @Override
    public long getContentLength() {
        long len = 0;
        for(NameFilePair pair:nameFilePairs){
            len += pair.getValue().length();
        }
        return len;
    }

    @Override
    public long writeLength() {
        return finished;
    }

    @Override
    public String getContentType() {
        return ContentType.FORM_FILE+"; boundary="+boundary;
    }

    @Override
    public Charset getContentEncoding() {
        return charset;
    }



    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        for(NameValuePair pair:nameValuePairs){
            writeName(out,pair.getName());
            writeValue(out,pair.getValue());
        }
        for(NameFilePair pair:nameFilePairs){
            writeName(out,pair.getName());
            writeValue(out,pair.getValue());
        }
    }


    private void writeName(DataOutputStream out,String name) throws IOException{
        out.writeBytes("\r\n");
        out.writeBytes(nb);
        out.writeBytes("\r\n");
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"");
    }

    private void writeValue(DataOutputStream outputStream,String value) throws IOException {
        outputStream.writeBytes("\r\n\r\n");
        outputStream.writeBytes(value);
    }

    private static final int BUF_SIZE = 1024*4;

    private long finished = 0;

    private void writeValue(DataOutputStream outputStream,File file) throws IOException{
        String contentType = ContentType.findMimeByExtension(TextUtils.fileExt(file.getName()));

        outputStream.writeBytes("; filename=\""+file.getName()+"\"\r\n");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("Content-Type:" + contentType + "\r\n\r\n");

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte bytes[] = new byte[BUF_SIZE];
            int len;
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                finished += len;
            }
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                }catch (IOException e){}
            }
        }


    }


}
