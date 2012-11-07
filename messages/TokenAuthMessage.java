package edu.ucsb.cs290.touch.to.chat.remote.messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.util.UUID;

public class TokenAuthMessage implements Serializable {
	private final ProtectedMessage pm;
	private final PublicKey destination;
	private final SignedObject secretToken;
        private final long earliest;
        private final long latest;
	public TokenAuthMessage(ProtectedMessage pm, PublicKey destination, SignedObject secretToken, long earliest, long latest) {
		this.destination = destination;
		this.secretToken = secretToken;
		this.pm = pm;
                this.earliest = earliest;
                this.latest = latest;
	}
        
        public TokenAuthMessage(ProtectedMessage pm, PublicKey destination, SignedObject secretToken) {
            this(pm, destination, secretToken, 0l,0l);
        }        
	
	public PublicKey getDestination() {
		try {
			if (secretToken.verify(destination, Signature.getInstance("DSA", "SC"))) {
				return destination;
			}
		} catch (GeneralSecurityException e) {
			
		}
		return null;
	}
	
	public UUID getToken() throws IOException, ClassNotFoundException {
		return (UUID) secretToken.getObject();
	}
	
	public ProtectedMessage getMessage() {
		return pm;
	}
}
