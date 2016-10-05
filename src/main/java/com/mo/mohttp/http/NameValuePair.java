package com.mo.mohttp.http;


import java.io.Serializable;

public class NameValuePair implements Serializable{

    private static final long serialVersionUID = -5259770595256822605L;

    private String name;

    private String value;

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
