package edu.ucsb.cs290.touch.to.chat.remote.messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import java.security.GeneralSecurityException;

public class ProtectedMessage implements Serializable {
	private SealedObject message;
	private SealedObject messageKey;

	public ProtectedMessage(Message message, PublicKey dest, KeyPair author) throws GeneralSecurityException, IOException {
		SignedMessage signedMessage = new SignedMessage(message, author);
		KeyGenerator kg = KeyGenerator.getInstance("AES", "SC");
		kg.init(128);
		Key aesKey = kg.generateKey();
		Cipher c = Cipher.getInstance("AES","SC");
		c.init(Cipher.ENCRYPT_MODE, aesKey);
		this.message = new SealedObject(signedMessage, c);
		Cipher d = Cipher.getInstance("ElGamal", "SC");
		d.init(Cipher.ENCRYPT_MODE, dest);
		messageKey = new SealedObject(aesKey, d);
	}
	
	public SignedMessage getMessage(PrivateKey recipient, PublicKey author) throws GeneralSecurityException, IOException, ClassNotFoundException {
		Key aesKey = (Key) messageKey.getObject(recipient,"SC");
		return (SignedMessage) message.getObject(aesKey, "SC");
	}
}
