package com.sample.blockchain;

import com.google.gson.GsonBuilder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringUtil {

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(StringUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
    
    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
}
