//package com.youlai.LfuCache;
//
//public class KLruCache<Key, Value> {
//    private static class LruNode<Key, Value> {
//        Key key;
//        Value value;
//        int accessCount;
//        LruNode<Key, Value> prev, next;
//
//        LruNode(Key key, Value value) {
//            this.key = key;
//            this.value = value;
//            this.accessCount = 1;
//        }
//    }
//
//    private final int capacity;
//    private final Map<Key, LruNode<Key, Value>> nodeMap = new HashMap<>();
//    private final LruNode<Key, Value> dummyHead, dummyTail;
//
//    public KLruCache(int capacity) {
//        this.capacity = capacity;
//        dummyHead = new LruNode<>(null, null);
//        dummyTail = new LruNode<>(null, null);
//        dummyHead.next = dummyTail;
//        dummyTail.prev = dummyHead;
//    }
//
//    @Override
//    public synchronized void put(Key key, Value value) {
//        if (capacity <= 0) return;
//
//        LruNode<Key, Value> node = nodeMap.get(key);
//        if (node != null) {
//            node.value = value;
//            moveToMostRecent(node);
//            return;
//        }
//
//        if (nodeMap.size() >= capacity) evict();
//        LruNode<Key, Value> newNode = new LruNode<>(key, value);
//        nodeMap.put(key, newNode);
//        insertToTail(newNode);
//    }
//
//    @Override
//    public synchronized Value get(Key key) {
//        LruNode<Key, Value> node = nodeMap.get(key);
//        if (node == null) return null;
//        moveToMostRecent(node);
//        node.accessCount++;
//        return node.value;
//    }
//
//    private void moveToMostRecent(LruNode<Key, Value> node) {
//        removeNode(node);
//        insertToTail(node);
//    }
//
//    private void removeNode(LruNode<Key, Value> node) {
//        node.prev.next = node.next;
//        node.next.prev = node.prev;
//    }
//
//    private void insertToTail(LruNode<Key, Value> node) {
//        node.prev = dummyTail.prev;
//        node.next = dummyTail;
//        dummyTail.prev.next = node;
//        dummyTail.prev = node;
//    }
//
//    private void evict() {
//        LruNode<Key, Value> lru = dummyHead.next;
//        removeNode(lru);
//        nodeMap.remove(lru.key);
//    }
//}