package com.enezaeducation.mwalimu;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import eu.squ1rr.uni.chatbox.Message;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class BroadcastTab extends Fragment {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	private static final String TAG = "BroadcastTab";
	 
	/*
	 * MEMBERS
	 */
	
	/** parent activity */
	private Activity activity = null;
	
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	public static SparseArray<String> names = new SparseArray<String>();
	
	private static long maxTs = 0;
	
	/*
	 * INITIALISATION
	 */
	
	/** initialise with parent activity */
	public void initialise(Activity activity) {
		this.activity = activity;
	}
	
	/** assigning layout */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.activity_chat_tab, container, false);
    	
    }
    
    /** showing chart page, when tab is opened */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
    	ListView listView = (ListView)view.findViewById(R.id.classesListView);
    	
    	int ts = 0;
    	User user = User.getInstance(activity);
    	
    	ServerTask task = new ServerTask(activity, Constants.BASE_URL + "users/" + user.getId() + "/timestamp/" + ts, new ServerCallback() {
			@Override
			public void run() {
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i("", response.toString());
						try {
							JSONArray messages = response.getJSONArray("messages");
							for(int i = 0; i < messages.length(); ++i) {
								JSONObject msg = messages.getJSONObject(i);
								String body = msg.getString("body");
								int sender = msg.getInt("send_from");
								int rece = msg.getInt("send_to");
								long timestamp = msg.getLong("timestamp");
								Message message = new Message(body, timestamp, sender, rece);
							}
							JSONArray users = response.getJSONArray("users");
							for(int i = 0; i < messages.length(); ++i) {
								JSONObject usr = users.getJSONObject(i);
								int id = usr.getInt("id");
								String name = usr.getString("name");
								if(names.get(id) == null) {
									names.put(id, name);
								}
							}
							return; // these error (if any) are not 'server' errors
						} catch(JSONException e) {
							if(Constants.DEBUG) {
								Log.e("", "Server error", e);
							}
						}
					}
				}
				
			}
    	});

    	task.run();
    }
    

}