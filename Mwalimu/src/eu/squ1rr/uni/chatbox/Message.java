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
	
	private final int senderId;
	private final int receId;
	
	/**
	 * Message time-stamp
	 */
	private final long timeStamp;
	
	/**
	 * @param body message body
	 * @param sender message sender as string
	 * @param timeStamp message time-stamp
	 */
	public Message(String body, long timeStamp, int senderId, int receId) {
		this.body = body;
		this.senderId = senderId;
		this.receId = receId;
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
	public int getSenderId() {
		return senderId;
	}
	
	public int getReceId() {
		return receId;
	}
	
	/**
	 * @return message time-stamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
}
