package com.mo.mohttp.misc;


import com.mo.mohttp.apache.URIBuilder;
import com.mo.mohttp.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

/**
 * @since 4.3
 */
public final class TextUtils {

    private TextUtils(){}

    /**
     * @return Returns true if the parameter is null or of zero length
     */
    public static boolean isEmpty(final CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
    }

    /**
     * @return Returns true if the parameter is null or contains only whitespace
     */
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean containsBlanks(final CharSequence s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static String randomString(int length){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<length;i++){
            int number = random.nextInt(base.length());
            stringBuilder.append(base.charAt(number));
        }
        return stringBuilder.toString();
    }


    public static String fileExt(String name){
        int i = name.lastIndexOf('.');
        if(i==-1){
            return null;
        }
        return name.substring(i+1);
    }

    public static URI buildURI(URI uri, Charset charset, List<NameValuePair> paramList) throws URISyntaxException, UnsupportedEncodingException {
        URIBuilder uriBuilder = new URIBuilder(uri);
        if(charset!=null){
            uriBuilder.setCharset(charset);
        }
        for(NameValuePair pair:paramList){
            uriBuilder.addParameter(pair.getName(),pair.getValue());
        }
        return uriBuilder.build();
    }

}
