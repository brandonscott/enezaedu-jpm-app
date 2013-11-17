package com.enezaeducation.mwalimu;
/** A class for sending a message to a specific student*/
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import eu.squ1rr.uni.chatbox.ChatBoxActivity;
import eu.squ1rr.uni.chatbox.Message;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ChatTab extends Fragment {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	private static final String TAG = "ChatTab";
	 
	/*
	 * MEMBERS
	 */
	
	/** parent activity */
	private Activity activity = null;
	
	public static ArrayList<Message> messages = new ArrayList<Message>();
	
	public static SparseArray<String> names = new SparseArray<String>();
	
	private static long maxTs = 0;
	
	public static ArrayList<Integer> ids = new ArrayList<Integer>();
	
	private Handler handler = new Handler();
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(r != null) {
			handler.removeCallbacks(r);
		}
	}
	
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
    
    private long ts = 0;
    
    Runnable r = null;
    
    /** showing chat page, when tab is opened */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
    	final User user = User.getInstance(activity);
    	
    	final ListView listView = (ListView)view.findViewById(R.id.classesListView);
    	
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(activity, ChatBoxActivity.class);
				intent.putExtra("id", ids.get(arg2));
				activity.startActivity(intent);
			}
    	});
    	
    	ts = 0;
    	
    	messages = new ArrayList<Message>();
    	names = new SparseArray<String>();
    	
    	//Log.i(TAG, Constants.BASE_URL + "users/" + user.getId() + "/timestamp/" + ts);
    	r = new Runnable() {

			@Override
			public void run() {
				Log.d(TAG, "TASK");
				ServerTask task = new ServerTask(activity, Constants.BASE_URL + "users/" + user.getId() + "/messages/" + ts, new ServerCallback() {
					@Override
					public void run() {
						if(status == ServerTask.REQUEST_SUCCESS) {
							// response available
							if(response != null) {
								ArrayList<Integer> tempids = new ArrayList<Integer>();
								Log.i("", response.toString());
								try {
									SparseBooleanArray hm = new SparseBooleanArray();
									
									User user = User.getInstance(activity);
									
									JSONArray messages = response.getJSONArray("messages");
									for(int i = 0; i < messages.length(); ++i) {
										JSONObject msg = messages.getJSONObject(i);
										String body = msg.getString("body");
										int sender = msg.getInt("from_user");
										int rece = msg.getInt("to_user");
										long timestamp = msg.getLong("sent_time");
										Message message = new Message(body, timestamp, sender, rece);
										
										ts = Math.max(timestamp, ts);
										
										int id = sender;
										if(id == user.getId()) id = rece;
										
										if(!hm.get(id)) {
											hm.put(id, true);
											tempids.add(id);
										}
										
										ChatTab.messages.add(message);
										
										if(ChatBoxActivity.self != null) { // hack
											ChatBoxActivity.self.listAdapter.addMessage(message);
										}
									}
									JSONArray users = response.getJSONArray("users");
									for(int i = 0; i < users.length(); ++i) {
										JSONObject usr = users.getJSONObject(i);
										int id = usr.getInt("id");
										String name = usr.getString("name");
										if(names.get(id) == null) {
											names.put(id, name);
										}
									}
									
									ArrayList<String> list = new ArrayList<String>();
									
									for(int i : tempids) {
										list.add(names.get(i));
									}
									
									
									if(list.size() > 0) {
										if(activity == null) return;
										ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list);
										listView.setAdapter(adapter);
										
										ids = tempids;
										
									}
									
									handler.postDelayed(r, 6 * 1000);
									
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
    		
    	};
    	
    	r.run();
    }
    

}