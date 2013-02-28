package edu.ucsb.cs290.touch.to.text.remote.register;

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
 * @author dannyiland
 * @author charlesmunger
 */
public class RegisterUser implements Serializable {
	/**
	 * A signed string to register with GCM, the public key used to sign it, and an int max delay tolerated by this user.
	 */
	private static final long serialVersionUID = 1L;
	public static final String FIELD_NAME = "RM";
	private final SignedObject regId;
	private final PublicKey publicKey;
	private final int maxDelay;
    private final SignedObject fbToken;

    public RegisterUser(String regId, KeyPair myKeys, int maxDelay) throws IOException, GeneralSecurityException {
	this.regId = new SignedObject(regId, myKeys.getPrivate(),
				      Signature.getInstance("DSA", "SC"));
	this.publicKey = myKeys.getPublic();
	this.maxDelay = maxDelay;
	this.fbToken = null;
    }

    public RegisterUser(String regId, String token, KeyPair myKeys, int maxDelay) throws IOException, GeneralSecurityException {
	this.regId = new SignedObject(regId, myKeys.getPrivate(),
				      Signature.getInstance("DSA", "SC"));
	this.fbToken = new SignedObject(token, myKeys.getPrivate(),
					Signature.getInstance("DSA", "SC"));
	this.publicKey = myKeys.getPublic();
	this.maxDelay = maxDelay;
    }

    public String getFbToken() throws IOException, ClassNotFoundException, GeneralSecurityException{
	if (fbToken.verify(publicKey, Signature.getInstance("DSA", "SC")))
	    return (String) fbToken.getObject();
	throw new SignatureException();
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
