package com.youlai.lrucache;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU-k算法有两个队列一个是缓存队列，一个是数据访问历史队列。
 * 当访问一个数据时，首先将其添加进入访问历史队列并进行累加访问次数，
 * 当该数据的访问次数超过k次后，才将数据缓存到缓存队列，从而避免缓存队列被冷数据所污染。
 * 同时访问历史队列中的数据也不是一直保留的，也是需要按照LRU的规则进行淘汰的。
 */
public class LRUKCache<K, V> extends LRUCache<K, V> {
    private int k; // 进入缓存队列的评判标准
    private final LRUCache<K, Integer> historyList; // 访问数据历史记录(value为访问次数)
    private final Map<K, V> historyValueMap;

    public LRUKCache(int capacity, int historyCapacity, int k) {
        super(capacity);
        this.k = k;
        this.historyList = new LRUCache<>(historyCapacity);
        this.historyValueMap = new HashMap<>();
    }

    @Override
    public synchronized V get(K key) {
        // 如果在缓存中，直接返回
        V value = super.get(key);
        if (value != null) {
            return value;
        }

        // 不在缓存中，判断是否在历史队列中，如果在，就+1；不在，就加入历史队列，设置访问次数为1。
        Integer count = historyList.get(key);
        if (count != null) {
            historyList.put(key, count + 1);
        } else {
            historyList.put(key, 1);
        }
        historyValueMap.put(key, null);

        // 如果访问次数超过k次，并且值不为null，就加入缓存队列，删除历史记录，返回值
        V historyValue = historyValueMap.get(key);
        if (historyList.get(key) >= k && historyValue != null) {
            super.put(key, historyValue);
            historyValueMap.remove(key);
            historyList.removeNode(key);
            return historyValue;
        }

        // 最后返回null
        return null;
    }

    @Override
    public synchronized void put(K key, V value) {
        // 如果在缓存中，直接返回
        if (super.get(key) != null) {
            return;
        }

        // 不在缓存中，判断是否在历史队列中，如果在，就+1；不在，就加入历史队列，设置访问次数为1。
        Integer count = historyList.get(key);
        if (count != null) {
            historyList.put(key, count + 1);
        } else {
            historyList.put(key, 1);
        }
        historyValueMap.put(key, value);

        // 如果访问次数超过k次，就加入主缓存
        if (historyList.get(key) >= k) {
            super.put(key, value);
            historyValueMap.remove(key);
            historyList.removeNode(key);
        }
    }
}
