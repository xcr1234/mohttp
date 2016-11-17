package com.mohttp.scripts;


import com.mo.mohttp.Client;
import com.mohttp.scripts.util.JsUtil;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

import java.util.Map;




public class NativeClient extends ScriptableObject{
    private static final long serialVersionUID = 4010734242307004327L;

    public NativeClient(){}

    @Override
    public String getClassName() {
        return "Client";
    }

    private Client client;

    @JSConstructor
    public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
        NativeClient nativeClient = new NativeClient();
        nativeClient.client = new Client();
        if(JsUtil.isValid(args)){
             if(args[0] instanceof Map){
                 Map map = (Map)args[0];

                 Object headers = map.get("headers");
                 if(JsUtil.isValid(headers)&&headers instanceof Map){
                     Map headersMap = (Map)headers;
                     for (Object o : headersMap.entrySet()) {
                         Map.Entry entry = (Map.Entry) o;
                         String key = JsUtil.toString(entry.getKey());
                         String value = JsUtil.toString(entry.getValue());
                         nativeClient.client.header(key, value);
                     }
                 }

                 Object userAgent = map.get("userAgent");
                 if(JsUtil.isValid(userAgent)){
                     nativeClient.client.agent(JsUtil.toString(userAgent));
                 }

                 Object proxy = map.get("proxy");
                 if(JsUtil.isValid(proxy)&&proxy instanceof NativeProxy){
                     NativeProxy nativeProxy = (NativeProxy)proxy;
                     nativeClient.client.proxy(nativeProxy.getProxy());
                 }
             }
        }
        return nativeClient;
    }

    public Client getClient() {
        return client;
    }

    @JSFunction("userAgent")
    public NativeClient userAgent(String agent){
        this.client.agent(agent);
        return this;
    }

    @JSFunction("proxy")
    public NativeClient proxy(NativeProxy proxy){
        this.client.proxy(proxy.getProxy());
        return this;
    }

    @JSFunction("header")
    public NativeClient header(String name,String value){
        this.client.header(name,value);
        return this;
    }
}
