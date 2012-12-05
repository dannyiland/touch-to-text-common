package edu.ucsb.cs290.touch.to.chat.remote.register;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

/**
 * This object registers a device and key with the server to allow it to receive
 * messages.
 * 
 * @author charlesmunger
 */
public class RegisterUser implements Serializable {
	public static final String FIELD_NAME = "RM";
	private final SignedObject regId;
	private final PublicKey publicKey;
	private final int maxDelay;

	public RegisterUser(String regId, KeyPair myKeys, int maxDelay) throws IOException, GeneralSecurityException {
		this.regId = new SignedObject(regId, myKeys.getPrivate(),
				Signature.getInstance("DSA", "SC"));
		this.publicKey = myKeys.getPublic();
		this.maxDelay = maxDelay;
	}

	public String getRegId() throws IOException, ClassNotFoundException, GeneralSecurityException{
		if (regId.verify(publicKey, Signature.getInstance("DSA", "SC")))
			return (String) regId.getObject();
		throw new SignatureException();
	}

	public PublicKey getKey() {
		return publicKey;
	}

	public int getMaxDelay() {
		return maxDelay;
	}
}
