package eu.squ1rr.uni.chatbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Chat Box adapter for the list view
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class ChatAdapter extends BaseAdapter {
	
	/*
	 * MEMBERS
	 */
	
	/**
	 * Full Message list in order by time-stamp
	 */
	private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	/**
	 * Parent's context (for performance)
	 */
	private Context context = null;

	/**
	 * List view
	 */
	private ListView listView = null;
	
	/*
	 * INITIALISATION
	 */
	
	/**
	 * @param context parent's context
	 * @param listView list view
	 */
	public ChatAdapter(Context context, ListView listView) {
		this.context = context;
		this.listView = listView;
	}
	
	/*
	 * PUBLIC METHODS
	 */
	
	/**
	 * Adds message to the list (only the one with the date newer
	 * than for the last message)
	 * @param message
	 */
	public void addMessage(Message message) {
		// get current latest time-stamp
		long since = 0;
		if (this.messages.size() > 0) {
			since = this.messages.get(this.messages.size() - 1).getTimeStamp();
		}
		
		// add message if it is 'new'
		if(message.getTimeStamp() > since) {
			appendMessage(message);
			
			// reload the list view
			listView.invalidateViews();
		}
	}
	
	/**
	 * Adds multiple messages to the list (only the ones with the date newer
	 * than for the last message)
	 * @param messages
	 */
	public void addMessages(ArrayList<Message> messages) {
		// sort messages by time-stamp
		Collections.sort(messages, new Comparator<Message>() {
			@Override
			public int compare(Message lhs, Message rhs) {
				return (int)(lhs.getTimeStamp() - rhs.getTimeStamp());
			}

		});
		
		// get current latest time-stamp
		long since = 0;
		if (this.messages.size() > 0) {
			since = this.messages.get(this.messages.size() - 1).getTimeStamp();
		}
		
		// append 'new' messages
		for (Message message : messages) {
			if(message.getTimeStamp() > since) {
				appendMessage(message);
			}
		}
		
		// reload the list view
		listView.invalidateViews();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return messages.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		
		// check if the position is even, it's not if the highest bit is one
		boolean even = (position & 1) == 0;
		
		// if view is recycled check if it is compatible, if yes, holder can
		// be recycled
		if (view != null) {
			holder = (ViewHolder)view.getTag();
			if (holder.getEven() != even) {
				view = null;
			}
		}
		
		// create new view
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater)context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			// get right layout depending on the position
			if (even) {
				view = inflator.inflate(R.layout.list_item_chatbox, null);
			} else {
				view = inflator.inflate(R.layout.list_item_chatbox_right, null);
			}
			
			// assign it to holder
			holder = new ViewHolder(view, even);
			view.setTag(holder);
		}

		// take the right message from the list
		ChatMessage message = (ChatMessage)getItem(position);
		
		// ask holder to view it
		holder.setMessage(message);

		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.BaseAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() { 
		return false; // removes annoying highlight from the list elements
	} 

	/*
	 * (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) { 
		return false; // removes annoying highlight from the list elements
	}
	
	/*
	 * PRIVATE METHODS
	 */
	
	/**
	 * Adds message to the list (internally)
	 * @param message
	 */
	private void appendMessage(Message message) {
		if (messages.size() > 0) {
			// get the last message in the list
			ChatMessage last = messages.get(messages.size() - 1);
			
			boolean sameSender = message.getSender().equals(last.getSender());
			long timeDiff = message.getTimeStamp() - last.getTimeStamp();
			
			if(sameSender && timeDiff <= Constants.MESSAGE_JOIN_TIME) {
				// join messages from same sender within the time period of
				// Constants.MESSAGE_JOIN_TIME
				last.addMessage(message);
				return;
			}
		}
		ChatMessage chatMessage = new ChatMessage(message);
		messages.add(chatMessage);
	}
}
