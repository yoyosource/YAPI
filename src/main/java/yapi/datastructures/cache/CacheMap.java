// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CacheMap<K, V> extends LinkedHashMap<K, V> {

    private int desiredCacheSize = -1;

    public CacheMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CacheMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CacheMap() {
        super();
    }

    public CacheMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public CacheMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public void setDesiredCacheSize(int desiredCacheSize) {
        if (desiredCacheSize == -1) {
            this.desiredCacheSize = desiredCacheSize;
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > desiredCacheSize;
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V replace(K key, V value) {
        throw new UnsupportedOperationException();
    }
}