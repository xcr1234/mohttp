package com.mo.mohttp.pair;


import java.io.Serializable;

public class Pair<K extends Serializable,V extends Serializable> implements Serializable{
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (name != null ? !name.equals(pair.name) : pair.name != null) return false;
        return value != null ? value.equals(pair.value) : pair.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(name)+"="+String.valueOf(value);
    }
}
