package edu.ucsb.cs290.touch.to.text.remote.register;

import java.io.Serializable;
import java.security.PublicKey;
/**
 * This object registers a device and key with the server to allow it to receive
 * messages.
 * @author charlesmunger
 */
public class RegisterUser implements Serializable {
    private final String regId;
    private final PublicKey publicKey;
    private final int maxDelay;
    public RegisterUser(String regId, PublicKey publicKey, int maxDelay) {
        this.regId = regId;
        this.publicKey = publicKey;
        this.maxDelay = maxDelay;
    }    
    
    public String getRegId() {
        return regId;
    }
    
    public PublicKey getKey() {
        return publicKey;
    }
    
    public int getMaxDelay() {
        return maxDelay;
    }
}
