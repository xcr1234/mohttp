package com.mo.mohttp.pair;


import java.io.Serializable;

public abstract class Pair<K extends Serializable,V extends Serializable> implements Serializable{
    private static final long serialVersionUID = 8140444398032758168L;
    private K name;
    private V value;

    protected Pair() {
    }

    public Pair(K name, V value) {
        this.name = name;
        this.value = value;
    }

    public K getName() {
        return name;
    }

    public void setName(K name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
