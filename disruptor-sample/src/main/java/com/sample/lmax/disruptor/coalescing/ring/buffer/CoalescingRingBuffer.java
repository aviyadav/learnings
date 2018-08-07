package com.sample.lmax.disruptor.coalescing.ring.buffer;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class CoalescingRingBuffer<K, V> implements CoalescingBuffer<K, V> {

    private final AtomicLong nextWrite = new AtomicLong(0);
    private long lastCleaned = 0;

    private final AtomicLong rejectionCount = new AtomicLong(0);
    private final K[] keys;
    private final AtomicReferenceArray<V> values;

    private final K nonCollapsibleKey = (K) new Object();
    private final int mask;
    private final int capacity;

    private volatile long firstWrite = 1;
    private final AtomicLong lastRead = new AtomicLong(0);

    public CoalescingRingBuffer(int capacity) {
        this.capacity = nextPowerOfTwo(capacity);
        this.mask = this.capacity - 1;

        this.keys = (K[]) new Object[this.capacity];
        this.values = new AtomicReferenceArray<>(this.capacity);
    }

    private int nextPowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    @Override
    public int size() {
        while (true) {
            long lastReadBefore = lastRead.get();
            long currentNextWrite = this.nextWrite.get();
            long lastReadAfter = lastRead.get();

            if(lastReadBefore == lastReadAfter) {
                return (int) (currentNextWrite - lastReadBefore) - 1;
            }
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return firstWrite == nextWrite.get();
    }

    @Override
    public boolean isFull() {
        return size() == capacity;
    }

    @Override
    public boolean offer(K key, V value) {

        long nextWrite = this.nextWrite.get();
        for(long updatePosition =firstWrite; updatePosition < nextWrite; updatePosition++) {
            int index = mask(updatePosition);

            if(key.equals(keys[index])) {
                values.set(index, value);

                if(updatePosition >= firstWrite) {
                    return true;
                } else {
                    break;
                }
            }
        }

        return add(key, value);
    }

    private int mask(long value) {
        return ((int) value) & mask;
    }

    @Override
    public boolean offer(V value) {
        return add(nonCollapsibleKey, value);
    }

    private boolean add(K key, V value) {
        if(isFull()) {
            rejectionCount.lazySet(rejectionCount.get() + 1);
            return false;
        }

        cleanUp();
        store(key, value);
        return true;
    }

    private void store(K key, V value) {
        long nextWrite = this.nextWrite.get();
        int index = mask(nextWrite);

        keys[index] = key;
        values.set(index, value);

        this.nextWrite.lazySet(nextWrite + 1);
    }

    private void cleanUp() {
        long lastRead = this.lastRead.get();

        if(lastRead == lastCleaned) {
            return;
        }

        while (lastCleaned < lastRead) {
            int index = mask(++lastCleaned);
            keys[index] = null;
            values.lazySet(index, null);
        }
    }

    @Override
    public int poll(Collection<? super V> bucket) {
        return fill(bucket, nextWrite.get());
    }

    @Override
    public int poll(Collection<? super V> bucket, int maxItems) {
        long claimUpTo = Math.min(firstWrite + maxItems, nextWrite.get());
        return fill(bucket, claimUpTo);
    }

    private int fill(Collection<? super V> bucket, long claimUpTo) {
        firstWrite = claimUpTo;
        long lastRead = this.lastRead.get();

        for (long readIndex = lastRead + 1; readIndex < claimUpTo; readIndex++) {
            int index = mask(readIndex);
            bucket.add(values.get(index));
            values.set(index, null);
        }

        this.lastRead.lazySet(claimUpTo - 1);
        return (int) (claimUpTo - lastRead - 1);
    }

    public long getRejectionCount() {
        return rejectionCount.get();
    }

    public long nextWrite() {
        return nextWrite.get();
    }

    public long firstWrite() {
        return firstWrite;
    }

}
