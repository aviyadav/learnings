package com.sample.lmax.disruptor.coalescing.ring.buffer;

import java.util.Collection;

public interface CoalescingBuffer<K, V> {
    int size();
    int capacity();
    boolean isEmpty();
    boolean isFull();
    boolean offer(K key, V value);
    boolean offer(V value);
    int poll(Collection<? super V> bucket);
    int poll(Collection<? super V> bucket, int maxItems);
}
