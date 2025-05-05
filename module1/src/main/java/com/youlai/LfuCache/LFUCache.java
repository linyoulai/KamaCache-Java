package com.youlai.LfuCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 请你为 最不经常使用（LFU）缓存算法设计并实现数据结构。
 *
 * 实现 LFUCache 类：
 *
 * LFUCache(int capacity) - 用数据结构的容量 capacity 初始化对象
 * int get(int key) - 如果键 key 存在于缓存中，则获取键的值，否则返回 -1 。
 * void put(int key, int value) - 如果键 key 已存在，则变更其值；如果键不存在，请插入键值对。当缓存达到其容量 capacity 时，则应该在插入新项之前，移除最不经常使用的项。在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，应该去除 最久未使用 的键。
 * 为了确定最不常使用的键，可以为缓存中的每个键维护一个 使用计数器 。使用计数最小的键是最久未使用的键。
 *
 * 当一个键首次插入到缓存中时，它的使用计数器被设置为 1 (由于 put 操作)。对缓存中的键执行 get 或 put 操作，使用计数器的值将会递增。
 *
 * 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。
 */
public class LFUCache {

    private int minFrequency;
    private final int capacity;
    private final Map<Integer, Node> cache; // 一个哈希表维护key到Node的映射
    private final Map<Integer, NodeList> frequencyMap; // 一个哈希表维护frequency到NodeList的映射

    private class Node {
        public int frequency;
        public Integer key;
        public Integer value;
        public Node prev;
        public Node next;

        public Node(int frequency, Integer key, Integer value, Node prev, Node next) {
            this.frequency = frequency;
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private class NodeList {
        public final Node head;
        public final Node tail;
        public NodeList() {
            head = new Node(0, null, null, null, null);
            tail = new Node(0, null, null, null, null);
            head.next = tail;
            tail.prev = head;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    // 如果键 key 存在于缓存中，则获取键的值，否则返回 -1 。
    public int get(int key) {
        Node node = cache.get(key);

        if (node == null) {
            return -1;
        }

        Node head = frequencyMap.get(node.frequency).head;
        Node tail = frequencyMap.get(node.frequency).tail;
        // 频次 + 1
        node.frequency++;
        if (node.prev == head && node.next == tail && minFrequency == node.frequency - 1) {
            minFrequency = node.frequency;
        }
        // 从原链表删除
        removeNode(node);
        // 插入新链表的头结点
        insertNode(node);

        return node.value;
    }

    private void insertNode(Node node) {
        // 该频次不存在时创建链表
        if (!frequencyMap.containsKey(node.frequency)) {
            frequencyMap.put(node.frequency, new NodeList());
        }
        // 插入node
        Node head = frequencyMap.get(node.frequency).head;
        node.prev = head;
        node.next = head.next;
        head.next = node;
        node.next.prev = node;

    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }

    // 如果键 key 已存在，则变更其值；
    // 如果键不存在，请插入键值对。
    // 当缓存达到其容量 capacity 时，则应该在插入新项之前，移除最不经常使用的项。
    // 在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，
    // 应该去除 最久未使用 的键。
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            node.frequency++;
            if (node.prev == frequencyMap.get(node.frequency - 1).head &&
                    node.next == frequencyMap.get(node.frequency - 1).tail &&
                    minFrequency == node.frequency - 1) {
                minFrequency = node.frequency;
            }
            removeNode(node);
            insertNode(node);
            return;
        }

        // 缓存满了，开始删除
        if (cache.size() >= capacity) {
            Node oldNode = frequencyMap.get(minFrequency).tail.prev;
            removeNode(oldNode);
            cache.remove(oldNode.key);
            // 清理空链表
            if (frequencyMap.get(minFrequency).head.next == frequencyMap.get(minFrequency).tail
                    && minFrequency != 1) {
                frequencyMap.remove(minFrequency);
            }
        }
        // 新增节点
        node = new Node(1, key, value, null, null);
        cache.put(key, node);
        insertNode(node);

        minFrequency = 1;
    }
}
