 
package edu.ucsb.cs290.touch.to.text.remote.messages;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	final String message;
	final Date timeSent;
	Date timeRecieved;
	
	public Message(String message) {
		this.message = message;
		this.timeSent = new Date(System.currentTimeMillis());
	}
}
