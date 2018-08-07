package com.sample.blockchain;

import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String previousHash, String data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
            previousHash +
            Long.toString(timeStamp) +
            Integer.toString(nonce) +
            data
        );
        
        return calculatedhash;
    }
    
    public void mineBlock(int difficulty) {
        String target = StringUtil.getDificultyString(difficulty);
        while(!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        
        System.out.println("Block Mined !!! : " + hash);
    }
}