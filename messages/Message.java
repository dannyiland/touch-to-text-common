 
package edu.ucsb.cs290.touch.to.chat.remote.messages;

import java.io.Serializable;

public class Message implements Serializable {
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
