package com.mo.mohttp;


import com.mo.mohttp.anno.NotNull;
import com.mo.mohttp.anno.ThreadSafe;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 * 进行http(https)请求的公共类。
 * Doc:
 * https://xcr1234.github.io/mohttp/
 * </pre>
 */
@ThreadSafe
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
        },OPTIONS,TRACE;

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

    /**
     * 创建一个新的Client对象。
     * @return client
     */
    public static Client newClient(){
        return new Client();
    }

    /**
     * the alias of {@link #newClient()}
     * @return new Client();
     */
    public static Client client(){
        return newClient();
    }


    public static Request GET(@NotNull URI uri){
        return new Request(uri);
    }

    public static Request GET(@NotNull String uri){
        return new Request(uri);
    }

    public static Request POST(@NotNull String uri){
        return new Request(uri).method(Method.POST);
    }

    public static Request POST(@NotNull URI uri){
        return new Request(uri).method(Method.POST);
    }

    /**
     * 如果在使用https时 出现如下错误：{@link javax.net.ssl.SSLProtocolException}: handshake alert:  unrecognized_name，则需要调用一次该方法解决。
     */
    public static void httpsHandshake(){
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    private static ExecutorService executorService = null;

    /**
     * 获取mohttp线程池，也就是默认的线程池。
     * @return 获取正在运行的异步http请求所使用的线程池（{@link Executors#newCachedThreadPool()}）
     */
    public static ExecutorService getExecutorService() {
        synchronized (Http.class){
            if(executorService == null){
                executorService = Executors.newCachedThreadPool();
            }
            return executorService;
        }
    }
}
