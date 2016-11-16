package com.mo.mohttp.pair;


import java.io.Serializable;

public class NameValuePair extends Pair<String,String> implements Serializable{
    public NameValuePair(String name, String value) {
        super(name, value);
    }

    private static final long serialVersionUID = -5259770595256822605L;
}
