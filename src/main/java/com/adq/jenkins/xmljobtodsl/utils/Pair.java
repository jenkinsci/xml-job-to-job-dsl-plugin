package com.adq.jenkins.xmljobtodsl.utils;

public class Pair<K, V> {

    private final V value;
    private final K key;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
