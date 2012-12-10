package edu.ucsb.cs290.touch.to.text.remote.messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.util.UUID;

public class TokenAuthMessage implements Serializable {
	/**
	 * TokenAuthMessage takes a DSA public key, as well as a SignedObject
	 * containing a UUID, and a ProtectedMessage version 1L. It also has fields
	 * for earliest time to transmit and latest.
	 */
	private static final long serialVersionUID = 1L;
	public static final String FIELD_NAME = "TM";
	private final ProtectedMessage pm;
	private final PublicKey destination;
	private final SignedObject secretToken;
	private final long earliest;
	private final long latest;

	public TokenAuthMessage(ProtectedMessage pm, PublicKey destination,
			SignedObject secretToken, long earliest, long latest) {
		this.destination = destination;
		this.secretToken = secretToken;
		this.pm = pm;
		this.earliest = earliest;
		this.latest = latest;
	}

	public TokenAuthMessage(ProtectedMessage pm, PublicKey destination,
			SignedObject secretToken) {
		this(pm, destination, secretToken, 0l, 0l);
	}

	public PublicKey getDestination() {
		try {
			if (secretToken.verify(destination,
					Signature.getInstance("DSA", "SC"))) {
				return destination;
			}
		} catch (GeneralSecurityException e) {

		}
		return null;
	}

        public SignedObject getToken() throws IOException, ClassNotFoundException {
	        return secretToken;
	}


	public ProtectedMessage getMessage() {
		return pm;
	}

	public long getEarliest() {
		return earliest;
	}

	public long getLatest() {
		return latest;
	}

}
