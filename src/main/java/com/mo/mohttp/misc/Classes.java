package com.mo.mohttp.misc;


public final class Classes {
    private Classes(){}
    public static final String httpClient = "org.apache.http.client.HttpClient";

    public static final String commonsIO = "org.apache.commons.io.IOUtils";

    public static boolean inClasspath(String className){
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean hasMethod(Class<?> c,String methodName,Class<?> ... argTypes){
        try {
            c.getMethod(methodName,argTypes);
            return true;
        } catch (NoSuchMethodException e) {
            try {
                c.getDeclaredMethod(methodName,argTypes);
                return true;
            } catch (NoSuchMethodException e1) {
                return false;
            }
        }
    }


    public static void checkVersion(){
        //TODO
    }
}
