package com.mohttp.scripts.util;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;


public final class JsUtil {
    public   static  boolean isValid(Object o){
        return o!=null && o!= Context.getUndefinedValue();
    }

    public static String toString(Object val){
        return ScriptRuntime.toString(val);
    }

    public static boolean isValid(Object [] args){
        return args.length > 0 && args[0] != Context.getUndefinedValue();
    }
}
