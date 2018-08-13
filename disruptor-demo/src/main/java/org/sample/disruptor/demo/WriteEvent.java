package org.sample.disruptor.demo;

public class WriteEvent {
    
    private String message;

    public void set(String message) {
        this.message = message;
    }

    public String get() {
        return this.message;
    }
}
