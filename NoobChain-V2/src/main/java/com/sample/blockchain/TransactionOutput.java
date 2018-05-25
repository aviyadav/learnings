package com.sample.blockchain;

import java.security.PublicKey;

public class TransactionOutput {
    
    public String id;
    public PublicKey reciepient;
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey reciepent, float value, String parentTransactionId) {
        this.reciepient = reciepent;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepent) + Float.toString(value) + parentTransactionId);
    }
    
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }
}
