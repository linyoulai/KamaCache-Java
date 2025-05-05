package com.youlai.lrucache;


import java.util.*;

/**
 * 实现lru缓存
 * 提供get、put方法
 *
 */
public class LRUCache<Key, Value> {

    private final int capacity; // 缓存容量
    private final Map<Key, Node> cache; // 缓存键值对

    // 链表维护顺序
    private final Node head; // 头节点，哨兵
    private final Node tail; // 尾节点，哨兵


    // 节点内部类，存储键值对
    public class Node {
        public Key key;
        public Value value;
        public Node prev;
        public Node next;

        public Node(Key key, Value value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    public LRUCache(int capacity) {
        // 初始化缓存容量
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node(null, null, null, null);
        tail = new Node(null, null, null, null);
        head.next = tail;
        tail.prev = head;
    }

    // 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
    public synchronized Value get(Key key) {
        // 寻找key所对应节点的指针，
        // 如果找到了，就把它移到链表头部，返回对应的value
        // 如果找不到，返回-1，
        Node node = cache.get(key);

        // 找到节点
        if (node != null) {
            // 删除节点
            removeNode(node);
            // 添加到链表头部
            addToHead(node);
            // 返回值
            return node.value;
        }

        // 找不到
        return null;
    }

    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
    }

    protected void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    protected void removeNode(Key key) {
        Node node = cache.get(key);
        if (node != null) {
            removeNode(node);
            cache.remove(key);
        }
    }

    // 如果关键字 key 已经存在，则变更其数据值 value ；
    // 如果不存在，则向缓存中插入该组 key-value 。
    // 如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
    public synchronized void put(Key key, Value value) {
        Node node = cache.get(key);

        // 如果关键字 key 已经存在，则变更其数据值 value ；
        if (node != null) {
            node.value = value;
            // 删除节点
            removeNode(node);
            // 添加到链表头部
            addToHead(node);
            return;
        }

        // 如果不存在，则向缓存中插入该组 key-node 。
        node = new Node(key, value, null, null);
        addToHead(node);
        cache.put(key, node);

        // 如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
        if (cache.size() > capacity) {
            Node oldNode = tail.prev;
            cache.remove(oldNode.key);
            removeNode(oldNode);
        }
    }
}
