package eu.squ1rr.uni.chatbox;

/**
 * Class that hold single message from the server
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class Message {
	
	/**
	 * Message body
	 */
	private final String body;
	
	/**
	 * Message sender as string
	 */
	private final String sender;
	
	/**
	 * Message time-stamp
	 */
	private final long timeStamp;
	
	/**
	 * @param body message body
	 * @param sender message sender as string
	 * @param timeStamp message time-stamp
	 */
	public Message(String body, String sender, long timeStamp) {
		this.body = body;
		this.sender = sender;
		this.timeStamp = timeStamp;
	}
	
	/**
	 * @return message body
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * @return message sender as string
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * @return message time-stamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
}
