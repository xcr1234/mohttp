package com.mo.mohttp;


import java.net.URI;

/**
 * <pre>
 * 进行http(https)请求的公共类。
 * Doc:
 * https://xcr1234.github.io/mohttp/
 * </pre>
 */
public final class Http {

    private Http(){}

    /**
     * Http Method
     * The Method token indicates the method to be performed on the resource identified by the Request-URI. The method is case-sensitive.
     */
    public enum Method{
        DELETE, GET{
            @Override
            public boolean writeData() {
                return false;
            }
        },POST,PUT,HEAD{
            @Override
            public boolean writeData() {
                return false;
            }
        };

        /**
         * whether information (in the form of an entity) is written in the Request-Line
         * @return true : Request-Line ; false: identified by the Request-URI
         * see
         * https://www.w3.org/Protocols/HTTP/1.1/rfc2616bis/draft-lafon-rfc2616bis-latest.html#method
         * for more information.
         */
        public boolean writeData(){
            return true;
        }
    }

    public static Client newClient(){
        return new Client();
    }


    public static Request GET(URI uri){
        return new Request(uri);
    }

    public static Request GET(String uri){
        return new Request(uri);
    }

    public static Request POST(String uri){
        return new Request(uri).method(Method.POST);
    }

    public static Request POST(URI uri){
        return new Request(uri).method(Method.POST);
    }

    /**
     * 如果在使用https时 出现如下错误：{@link javax.net.ssl.SSLProtocolException}: handshake alert:  unrecognized_name，则需要调用一次该方法解决。
     */
    public static void httpsHandshake(){
        System.setProperty("jsse.enableSNIExtension", "false");
    }

}
