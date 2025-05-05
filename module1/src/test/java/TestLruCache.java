import com.youlai.lrucache.LRUCache;
import org.junit.Test;

public class TestLruCache {
    /**
     * 输入
     * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
     * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
     * 输出
     * [null, null, null, 1, null, -1, null, -1, 3, 4]
     */
    @Test
    public void testLruCache() {
        LRUCache lruCache = new LRUCache(2);
        System.out.println("null");
        lruCache.put(1, 1); // 缓存是 {1=1}
        System.out.println("null");
        lruCache.put(2, 2); // 缓存是 {2=2, 1=1}
        System.out.println(lruCache.get(1));    // 返回 1
        lruCache.put(3, 3); // 去除关键字 2，缓存是 {3=3, 1=1}
        System.out.println("null");
        System.out.println(lruCache.get(2));    // 返回 -1 (未找到)
        lruCache.put(4, 4); // 去除关键字 1，缓存是 {4=4, 3=3}
        System.out.println("null");
        System.out.println(lruCache.get(1));    // 返回 -1 (未找到)
        System.out.println(lruCache.get(3));    // 返回 3
        System.out.println(lruCache.get(4));    // 返回 4
    }

}
