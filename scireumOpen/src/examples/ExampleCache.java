package examples;

import java.util.concurrent.TimeUnit;

import com.scireum.open.cache.Cache;
import com.scireum.open.cache.CacheManager;
import com.scireum.open.statistics.Watch;

public class ExampleCache {

    private static final Cache<Integer, Integer> fibCache = CacheManager.createCache("Fibonacci", 10, 2, TimeUnit.SECONDS);

    private static int fib(int n, boolean useCache) {
        if (n < 2) {
            return 1;
        }

        Integer result = useCache ? fibCache.get(n) : null;
        if (result != null) {
            return result;
        }

        result = fib(n - 2, useCache) + fib(n - 1, useCache);
        if (useCache) {
            fibCache.put(n, result);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Watch w = Watch.start();
        System.out.println("fib(42) without cache:");
        fib(42, false);
        System.out.println(w.durationReset());

        System.out.println("fib(42) with cache:");
        fib(42, true);
        System.out.println(w.durationReset());

        outputStatistics();

        System.out.println("Waiting 5 seconds to let our entries expire...");
        Thread.sleep(5000);

        System.out.println("Running cache-eviction...");
        CacheManager.runEviction();

        outputStatistics();
    }

    private static void outputStatistics() {
        System.out.println("Size: " + fibCache.getSize());
        System.out.println("Uses: " + fibCache.getUses());
        System.out.println("Hit-Rate: " + fibCache.getHitRate());
    }
}
