package com.mohttp.scripts;


import com.mohttp.scripts.util.JsUtil;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

public class NativeProxy extends ScriptableObject{
    private static final long serialVersionUID = 2930446773674963502L;

    public NativeProxy(){}

    private String hostname;

    private int port;

    private int type = Proxy.Type.HTTP.ordinal();

    @JSGetter("hostname")
    public String getHostname() {
        return hostname;
    }

    @JSSetter("hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @JSGetter("port")
    public int getPort() {
        return port;
    }
    @JSSetter("port")
    public void setPort(int port) {
        this.port = port;
    }
    @JSGetter("type")
    public int getType() {
        return type;
    }
    @JSSetter("type")
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getClassName() {
        return "Proxy";
    }

    public static final int http = Proxy.Type.HTTP.ordinal();

    public static final int direct = Proxy.Type.DIRECT.ordinal();

    public static final int socks = Proxy.Type.SOCKS.ordinal();

    private Proxy proxy;

    public Proxy getProxy() {
        return proxy;
    }

    @JSConstructor
    public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
        NativeProxy nativeProxy = new NativeProxy();
        if(JsUtil.isValid(args)){
            if(args[0] instanceof Map){
                Map map = (Map)args[0];

                Object hostname = map.get("hostname");
                if(JsUtil.isValid(hostname)){
                    nativeProxy.hostname = JsUtil.toString(hostname);
                }

                Object port = map.get("port");
                if(JsUtil.isValid(port)){
                    nativeProxy.port = ScriptRuntime.toInt32(port);
                }

                Object type = map.get("type");
                if(JsUtil.isValid(type)){
                    nativeProxy.type = ScriptRuntime.toInt32(type);
                }

                nativeProxy.proxy = new Proxy(Proxy.Type.values()[nativeProxy.type],new InetSocketAddress(nativeProxy.hostname,nativeProxy.port));

            }
        }
        return nativeProxy;
    }
}
