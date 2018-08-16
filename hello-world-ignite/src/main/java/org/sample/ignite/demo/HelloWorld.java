package org.sample.ignite.demo;

import javax.cache.Cache;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class HelloWorld {
    
    public static void main(String[] args) {
        Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
        
        for (int i = 0; i < 100; i++) {
            cache.put(i, "Hello Message no. " + (i + 1));
        }
        
        ignite.compute().broadcast(() -> {
            for (Cache.Entry<Integer, String> entry : cache) {
                System.out.println("Key : " + entry.getKey() + ", Value : " + entry.getValue());
            }
        });
    }
}
