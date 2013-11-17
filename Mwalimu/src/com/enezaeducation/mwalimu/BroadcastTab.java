package com.enezaeducation.mwalimu;
/** A class for broadcasting a message to many students */
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
	
	ArrayList<Integer> classIds = null;
	ArrayList<String> classNames = null;
	
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
    	final ListView listView = (ListView)view.findViewById(R.id.classesListView);
    	
    	User user = User.getInstance(activity);
    	
    	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.makeOkAlert(activity, "Demo", "Not available yet");
			}
    		
    	});
    	
    	ServerTask task = new ServerTask(activity, Constants.BASE_URL + "users/" + user.getId() + "/classes", new ServerCallback() {
			@Override
			public void run() {
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i("", response.toString());
							ServerTask task = new ServerTask(activity, Constants.BASE_URL + "classes", new ServerCallback() {
								@Override
								public void run() {
									// hide progress dialogue
									
									if(status == ServerTask.REQUEST_SUCCESS) {
										// response available
										if(response != null) {
											Log.i(TAG, response.toString());
											try {
												JSONArray classes = response.getJSONArray("classes");
												classIds = new ArrayList<Integer>();
												classNames = new ArrayList<String>();
												for(int i = 0; i < classes.length(); ++i) {
													JSONObject row = classes.getJSONObject(i);
													int id = row.getInt("id");
													String name = row.getString("subject");
													classIds.add(id);
													classNames.add(name);
												}
												ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, classNames);
												listView.setAdapter(adapter);
												return; // these error (if any) are not 'server' errors
											} catch(JSONException e) {
												if(Constants.DEBUG) {
													Log.e(TAG, "Server error", e);
												}
											}
										}
									}
									//Utils.makeOkAlert(activity, "Server Error", "Sorry, Technical issues");
								}
							});
							//
							task.run();
							return; // these error (if any) are not 'server' errors
					}
				}
				
			}
    	});

    	task.run();
    }
    

}