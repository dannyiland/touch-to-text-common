 
package edu.ucsb.cs290.touch.to.text.remote.messages;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * This UID contains a String message, and a long time sent. The time sent is ms since January 1, 1970.
	 */
	private static final long serialVersionUID = 1L;
	final String message;
	final long timeSent;
	
	public Message(String message) {
		this.message = message;
		this.timeSent = System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		return message + "\n Sent at " + timeSent;
	}
	
	public long getTimeSent() {
		return timeSent;
	}
	
	public String getBody() {
		return message;
	}
}
