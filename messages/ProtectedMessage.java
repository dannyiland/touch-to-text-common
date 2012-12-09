package edu.ucsb.cs290.touch.to.chat.remote.messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;


public class ProtectedMessage implements Serializable {
	/**
	 * This version has two sealedObjects, one with ElGamal/NONE/PKCS1PADDING and one with AES. 
	 */
	private static final long serialVersionUID = 1L;
	private final SealedObject message;
	private final SealedObject messageKey;
	private final SealedObject newToken;
	public ProtectedMessage(SignedMessage signedMessage, PublicKey dest, KeyPair author) throws GeneralSecurityException {
		this(signedMessage,dest,author,null);
	}
	
	public ProtectedMessage(SignedMessage signedMessage, PublicKey dest, KeyPair author, KeyPair authorToken) throws GeneralSecurityException {
		KeyGenerator kg = KeyGenerator.getInstance("AES", "SC");
		kg.init(128);
		Key aesKey = kg.generateKey();
		Cipher c = Cipher.getInstance("AES","SC");
		c.init(Cipher.ENCRYPT_MODE, aesKey);
		SealedObject tempToken = null;
		SealedObject tempMessageKey = null;
		SealedObject tempMessage = null;
		try {
			tempMessage = new SealedObject(signedMessage, c);
			Cipher d = Cipher.getInstance("ElGamal/NONE/PKCS1PADDING", "SC");
			d.init(Cipher.ENCRYPT_MODE, dest);
			tempMessageKey = new SealedObject(aesKey.getEncoded(), d);
			if (authorToken != null) {
				c = Cipher.getInstance("AES", "SC");
				c.init(Cipher.ENCRYPT_MODE, aesKey);
				tempToken = new SealedObject(new SignedObject(UUID.randomUUID(),
						authorToken.getPrivate(), Signature.getInstance("DSA",
								"SC")), c);
			}
		} catch (IOException e) {
			// Will never happen.
			e.printStackTrace();
		}
		this.messageKey = tempMessageKey;
		this.message = tempMessage;
		this.newToken = tempToken;
	}
	
	public SignedMessage getMessage(PrivateKey recipient) throws GeneralSecurityException, IOException, ClassNotFoundException {
		Cipher d = Cipher.getInstance("ElGamal/NONE/PKCS1PADDING", "SC");
		d.init(Cipher.DECRYPT_MODE, recipient);
		Key aesKey = new SecretKeySpec((byte[]) messageKey.getObject(d),"AES");
		Cipher c = Cipher.getInstance("AES","SC");
		c.init(Cipher.DECRYPT_MODE, aesKey);
		return (SignedMessage) message.getObject(c);
	}

	public SealedObject getNewToken() {
		return newToken;
	}
}
