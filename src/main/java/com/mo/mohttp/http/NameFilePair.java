package com.mo.mohttp.http;

import java.io.File;
import java.io.Serializable;


public class NameFilePair implements Serializable {
    private static final long serialVersionUID = 2334079070248763345L;

    private String name;

    private File value;

    public NameFilePair(String name, File value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getValue() {
        return value;
    }

    public void setValue(File value) {
        this.value = value;
    }
}
