package edu.ucsb.cs290.touch.to.chat.remote.messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class ProtectedMessage implements Serializable {
	private SealedObject message;
	private SealedObject messageKey;

	public ProtectedMessage(SignedMessage signedMessage, PublicKey dest, KeyPair author) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException, IOException, NoSuchPaddingException, IllegalBlockSizeException {
		KeyGenerator kg = KeyGenerator.getInstance("AES", "SC");
		kg.init(128);
		Key aesKey = kg.generateKey();
		Cipher c = Cipher.getInstance("AES","SC");
		c.init(Cipher.ENCRYPT_MODE, aesKey);
		this.message = new SealedObject(signedMessage, c);
		Cipher d = Cipher.getInstance("ElGamal/NONE/PKCS1PADDING", "SC");
		d.init(Cipher.ENCRYPT_MODE, dest);
		messageKey = new SealedObject(aesKey.getEncoded(), d);
	}
	
	public SignedMessage getMessage(PrivateKey recipient) throws GeneralSecurityException, IOException, ClassNotFoundException {
		Cipher d = Cipher.getInstance("ElGamal/NONE/PKCS1PADDING", "SC");
		d.init(Cipher.DECRYPT_MODE, recipient);
		Key aesKey = new SecretKeySpec((byte[]) messageKey.getObject(d),"AES");
		Cipher c = Cipher.getInstance("AES","SC");
		c.init(Cipher.DECRYPT_MODE, aesKey);
		return (SignedMessage) message.getObject(c);
	}
}
