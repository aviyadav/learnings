package com.sample.lmax.disruptor.coalescing.ring.buffer;

public final class MarketSnapshot {
    
    private long instrumentId;
    private long bestBid;
    private long bestAsk;

    public static MarketSnapshot createMarketSnapshot(long instrumentId, long bestBid, long bestAsk) {
        MarketSnapshot snapshot = new MarketSnapshot();
        snapshot.setInstrumentId(instrumentId);
        snapshot.setBestBid(bestBid);
        snapshot.setBestAsk(bestAsk);
        return snapshot;
    } 

    public long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public long getBestBid() {
        return bestBid;
    }

    public void setBestBid(long bestBid) {
        this.bestBid = bestBid;
    }

    public long getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(long bestAsk) {
        this.bestAsk = bestAsk;
    }

    @Override
    public String toString() {
        return instrumentId + ": " + bestBid + "/" + bestAsk + '}';
    }
}