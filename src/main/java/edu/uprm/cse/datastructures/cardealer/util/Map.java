package edu.uprm.cse.datastructures.cardealer.util;

import java.util.List;

public interface Map<K, V> {

    int size();

    boolean isEmpty();

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    boolean contains(K key);

    List<K> getKeys();

    List<V> getValues();



}