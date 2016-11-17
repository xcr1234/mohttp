package com.mohttp.scripts;


import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.pair.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClient {
    private Scriptable scope;
    private Context cx;

    @Before
    public void init(){
        cx = Context.enter();
        scope = cx.initStandardObjects();
        try {
            ScriptableObject.defineClass(scope,NativeProxy.class);
            ScriptableObject.defineClass(scope,NativeClient.class);

        }catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        cx.evaluateString(scope,"var Agents=com.mo.mohttp.constant.Agents;var ContentType=com.mo.mohttp.constant.ContentType;" +
                "var Headers=com.mo.mohttp.constant.Headers;var StatusCode=com.mo.mohttp.constant.StatusCode","constant define",1,null);
    }

    @Test
    public void test1(){
        NativeClient nativeClient = (NativeClient) cx.evaluateString(scope,"new Client({" +
               "headers:{a:100,b:'hello'}," +
               "userAgent:Agents.Chrome," +
                "proxy:new Proxy({" +
                "type:Proxy.http," +
                "hostname:'127.0.0.1'," +
                "port:8080" +
                "})" +
               "}).header(Headers.host,'www.baidu.com');",null,1,null);
        Assert.assertEquals(nativeClient.getClient().getUserAgent(),Agents.Chrome);
        List<NameValuePair> nameValuePairList = nativeClient.getClient().getHeaders();
        Assert.assertEquals(nameValuePairList.get(0).getName(),"a");
        Assert.assertEquals(nameValuePairList.get(0).getValue(),"100");
        Assert.assertEquals(nameValuePairList.get(1).getName(),"b");
        Assert.assertEquals(nameValuePairList.get(1).getValue(),"hello");
        Assert.assertEquals(nameValuePairList.get(2).getName(), Headers.host);
        Assert.assertEquals(nameValuePairList.get(2).getValue(),"www.baidu.com");

        Proxy proxy = nativeClient.getClient().getProxy();
        Assert.assertEquals(proxy.type(), Proxy.Type.HTTP);
        Assert.assertTrue(proxy.address() instanceof InetSocketAddress);
        InetSocketAddress address = (InetSocketAddress) proxy.address();
        Assert.assertEquals(address.getHostName(),"127.0.0.1");
        Assert.assertEquals(address.getPort(),8080);
    }
}
