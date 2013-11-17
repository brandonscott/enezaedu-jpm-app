package eu.squ1rr.uni.chatbox;

import java.util.ArrayList;
import java.util.Date;

import com.enezaeducation.mwalimu.ChatTab;
import com.enezaeducation.mwalimu.R;
import com.enezaeducation.mwalimu.User;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Main activity class for a Chat Box application
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class ChatBoxActivity extends Activity {

	/*
	 * MEMBERS
	 */
	
	/**
	 * An adapter for a Chat Box list view
	 */
	public ChatAdapter listAdapter = null;
	
	/*
	 * INTERFACE MEMBERS
	 */
	
	/**
	 * List View holder
	 */
	private ListView listView = null;
	
	/**
	 * User message input
	 */
	private EditText inputMessage = null;
	
	/**
	 * Send button
	 */
	private Button buttonSend = null;
	
	private int otherId = -1;
	
	public static ChatBoxActivity self = null;
	
	/*
	 * INITIALISATION
	 */
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(getIntent().getExtras() == null) {
        	return; // error
        }
        
        setContentView(R.layout.activity_chat_box);
        
        otherId = getIntent().getExtras().getInt("id");
        
        initialiseMembers();
        initialiseInterface();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	self = this;
    }
    
    @Override
    public void onPause() {
    	self = null;
    	super.onPause();
    }

    /*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.chat_box, menu);
        return true;
    }
    
    /**
     * Initialise class members
     */
    private void initialiseMembers() {
    	// interface parts
    	listView = (ListView)findViewById(R.id.chatListView);
    	inputMessage = (EditText)findViewById(R.id.inputMessage);
    	buttonSend = (Button)findViewById(R.id.buttonSend);
    	
    	// adapter for the list view
    	listAdapter = new ChatAdapter(this, listView);
    	
    	// fill adapter with fake data
    	ArrayList<Message> messages = new ArrayList<Message>();
    	
    	User user = User.getInstance(this);
    	
    	for(Message msg : ChatTab.messages) {
    		Log.i("123", msg.getReceId() + " " + msg.getSenderId() + " " + otherId + " " + 17 + " " + user.getId());
    		if(msg.getReceId() == otherId || msg.getSenderId() == otherId || msg.getReceId() == 17 || msg.getSenderId() == 17) {
    			messages.add(msg);
    		}
    	}
    	
    	Log.i("ASD", "" + messages.size());
    	
    	listAdapter.addMessages(messages);
    	//listAdapter.addMessages(FakeServer.genMessages(45, 6));
    }
    
    /**
     * Initialise interface of a Chat Box activity
     */
    private void initialiseInterface() {
    	// set list adapter
    	listView.setAdapter(listAdapter);
    	
    	// set on-click listener for a send button
    	buttonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// take message
				String message = inputMessage.getText().toString();
				
				// check if it has any data in it
				if(message.trim().length() == 0) {
					Toast.makeText(
						ChatBoxActivity.this,
						"Message should contain text",
						Toast.LENGTH_SHORT
					).show();
				} else {
					// clear message
					inputMessage.getText().clear();
					
					User user = User.getInstance(ChatBoxActivity.this);
					
					//TODO:
					int out = 2;
					
					// add it to adapter
					listAdapter.addMessage(
						new Message(
							message,
							new Date().getTime(),
							user.getId(),
							out
						)
					);
				}
			}
    	});
    }
}
