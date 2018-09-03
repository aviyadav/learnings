package com.sample.lmax.disruptor.github.support;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorTwoArg;

public final class StubEvent {

    private int value;
    private String testString;
    public static final EventTranslatorTwoArg<StubEvent, Integer, String> TRANSLATOR = (event, sequence, arg0, arg1) -> {
        event.setValue(arg0);
        event.setTestString(arg1);
    };
    public static final EventFactory<StubEvent> EVENT_FACTORY = () -> new StubEvent(-1);

    public StubEvent(int i) {
        this.value = i;
    }

    public void copy(StubEvent event) {
        value = event.value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StubEvent other = (StubEvent) obj;

        return value == other.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }
}
