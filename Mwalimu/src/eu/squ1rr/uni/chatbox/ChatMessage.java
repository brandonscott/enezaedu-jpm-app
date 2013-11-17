package eu.squ1rr.uni.chatbox;

import java.util.ArrayList;

/**
 * Class represents a message or a group of messages that will be shown in one
 * single entry of a list view
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class ChatMessage {
	
	/*
	 * MEMBERS
	 */
	
	/**
	 * List of messages associated with the same sender within the interval of
	 * Constants.MESSAGE_JOIN_TIME milliseconds
	 */
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	/**
	 * Pointer to the first message
	 */
	private Message first = null;
	
	/*
	 * INITIALISATION
	 */
	
	/**
	 * Group should consist of at least one message
	 * @param first
	 */
	public ChatMessage(Message first) {
		this.first = first;
	}
	
	/*
	 * PUBLIC METHODS
	 */
	
	/**
	 * @return message body
	 */
	public String getBody() {
		StringBuilder builder = new StringBuilder();
		builder.append(first.getBody());
		for (Message message : messages) {
			builder.append("\n");
			builder.append(message.getBody());
		}
		return builder.toString();
	}
	
	/**
	 * @return sender as string
	 */
	public int getSender() {
		return first.getSenderId();
	}
	
	/**
	 * @return first message time-stamp
	 */
	public long getTimeStamp() {
		return first.getTimeStamp();
	}
	
	/**
	 * Adds message to the group
	 * 
	 * @param message
	 */
	public void addMessage(Message message) {
		messages.add(message);
	}
}
