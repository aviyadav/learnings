package com.sample.lmax.disruptor.github.examples;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class MultiProducerWithTranslator {

    private static class IMessage {
    }

    private static class ITransportable {
    }

    private static class ObjectBox {

        IMessage message;
        ITransportable transportable;
        String string;

        private static final EventFactory<ObjectBox> FACTORY = () -> new ObjectBox();

        public void setMessage(IMessage message) {
            this.message = message;
        }

        public void setTransportable(ITransportable transportable) {
            this.transportable = transportable;
        }

        public void setStreamName(String name) {
            this.string = name;
        }
    }

    public static class Publisher implements EventTranslatorThreeArg<ObjectBox, IMessage, ITransportable, String> {

        @Override
        public void translateTo(ObjectBox event, long sequence, IMessage arg0, ITransportable arg1, String arg2) {
            event.setMessage(arg0);
            event.setTransportable(arg1);
            event.setStreamName(arg2);
        }
    }

    public static class Consumer implements EventHandler<ObjectBox> {

        @Override
        public void onEvent(ObjectBox event, long sequence, boolean endOfBatch) throws Exception {
        }
    }

    static final int RING_SIZE = 1024;

    public static void main(String[] args) throws InterruptedException {

        Disruptor<ObjectBox> disruptor = new Disruptor<>(ObjectBox.FACTORY, RING_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new Consumer()).then(new Consumer());
        final RingBuffer<ObjectBox> ringBuffer = disruptor.getRingBuffer();
        Publisher p = new Publisher();
        IMessage message = new IMessage();
        ITransportable transportable = new ITransportable();
        String streamName = "com.lmax.wibble";
        System.out.println("publishing " + RING_SIZE + " messages");
        for (int i = 0; i < RING_SIZE; i++) {
            ringBuffer.publishEvent(p, message, transportable, streamName);
            Thread.sleep(10);
        }
        System.out.println("start disruptor");
        disruptor.start();
        System.out.println("continue publishing");
        while (true) {
            ringBuffer.publishEvent(p, message, transportable, streamName);
            Thread.sleep(10);
        }
    }

}
