package com.sample.lmax.disruptor.coalescing.ring.buffer;

public interface CoalescingRingBufferViewerMBean {

    int getSize();
    int getCapacity();
    int getRemainingCapacity();
    long getRejectionCount();
    long getProducerIndex();
    long getConsumerIndex();
}
