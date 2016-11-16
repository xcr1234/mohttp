package com.mo.mohttp.pair;

import java.io.File;
import java.io.Serializable;


public class NameFilePair extends Pair<String,File> implements Serializable {
    public NameFilePair(String name, File value) {
        super(name, value);
    }

    private static final long serialVersionUID = -7146467401521799217L;
}
