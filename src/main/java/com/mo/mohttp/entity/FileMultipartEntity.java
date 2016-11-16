package com.mo.mohttp.entity;

import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.pair.NameFilePair;
import com.mo.mohttp.pair.NameValuePair;
import com.mo.mohttp.misc.TextUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <pre>
 *  File upload support
 *
 *  HTTP protocol :
 *
 *  first ,generate a random string (boundary):
 *  nb = "--" + boundary;
 *
 *  such as
 *  boundary =  ---------------------------123u2abc2v1z1f6
 *  nb = -----------------------------123u2abc2v1z1f6;
 *
 * second,write content Type in header field:
 * {@link ContentType#FORM_FILE}+"; boundary="+boundary;
 * in this example,content type is :
 * multipart/form-data; boundary=---------------------------123u2abc2v1z1f6
 *
 * thirdly,write param fields:
 *
 * line break : \r\n
 * lines:
 * 1)
 * 2) nb
 * 3) Content-Disposition: form-data; name="name"   // form field name
 * 4)
 * 5) value bytes (form field value)
 *
 * fourthly, write file fields:
 *
 * line break : \r\n
 *  * lines:
 * 1)
 * 2) nb
 * 3) Content-Disposition: form-data; name="name";filename = "file name"   // name : form field name
 * 4) Content-Type: file content Type
 * 5) file bytes
 * </pre>
 */
public class FileMultipartEntity implements Entity {

    private String boundary;  //pair boundary

    private List<NameFilePair> nameFilePairs;

    private List<NameValuePair> nameValuePairs;

    private Charset charset;

    private String nb;

    public FileMultipartEntity(List<NameValuePair> nameValuePairList, List<NameFilePair> nameFilePairs,Charset charset){
        boundary = "---------------------------Boundary" + TextUtils.randomString(13);
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
        //Content-Type: multipart/form-data; boundary=${bound}

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
        String contentType = ContentType.findMimeByExtension(TextUtils.fileExtension(file.getName()));

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
