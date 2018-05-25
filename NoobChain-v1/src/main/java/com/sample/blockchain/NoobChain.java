package com.sample.blockchain;

import java.util.ArrayList;
import java.util.List;

public class NoobChain {

    public static List<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        System.out.println("Trying to Mine block 1 ... ");
        addBlock(new Block("Hi I am the first block", "0"));

        System.out.println("Trying to Mine block 2 ... ");
        addBlock(new Block("Yo I am the second block", blockchain.get(blockchain.size() - 1).hash));

        System.out.println("Trying to Mine block 3... ");
        addBlock(new Block("Hey I am the third block", blockchain.get(blockchain.size() - 1).hash));

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static Boolean isChainValid() {

        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current hashes not equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }

        return true;
    }
}
