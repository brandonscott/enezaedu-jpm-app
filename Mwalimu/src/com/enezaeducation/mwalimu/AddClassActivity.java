package com.enezaeducation.mwalimu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddClassActivity extends BaseActivity {
	
	private ListView listView = null;

	private final static String TAG = "ClassActivity";
	
	private ArrayList<Integer> classIds = null;
	private ArrayList<String> classNames = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseInterface(R.layout.activity_add_class);
		
		initialiseMembers();
	}
	
	protected void initialiseMembers() {
		// list ivew
		listView = (ListView)findViewById(R.id.classUsersListView);
		
		String[] content = { "Classes Loading" };		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content);
		listView.setAdapter(adapter);
		
		loadClasses();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				User user = User.getInstance(AddClassActivity.this);
				ServerTask task = new ServerTask(AddClassActivity.this, Constants.BASE_URL + "classes/" + classIds.get(arg2) + "/add/" + user.getId(), new ServerCallback() {
					@Override
					public void run() {
						// hide progress dialogue
						
						if(status == ServerTask.REQUEST_SUCCESS) {
							// response available
							if(response != null) {
								Log.i(TAG, response.toString());
								try {
									boolean valid = response.getBoolean("valid");
									if(valid) {
										finish();
									} else {
										Utils.makeOkAlert(AddClassActivity.this, "Server Error", "Class list cannot be added");
									}
									return;
								} catch(JSONException e) {
									if(Constants.DEBUG) {
										Log.e(TAG, "Server error", e);
									}
								}
							}
						}
						Utils.makeOkAlert(AddClassActivity.this, "Server Error", "Sorry, Technical issues");
					}
				});
				task.setMethod(ServerTask.POST);
				task.run();
			}
		});
	}
	
	private void loadClasses() {
		ServerTask task = new ServerTask(AddClassActivity.this, Constants.BASE_URL + "classes", new ServerCallback() {
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
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_list_item_1, classNames);
							listView.setAdapter(adapter);
							return; // these error (if any) are not 'server' errors
						} catch(JSONException e) {
							if(Constants.DEBUG) {
								Log.e(TAG, "Server error", e);
							}
						}
					}
				}
				Utils.makeOkAlert(AddClassActivity.this, "Server Error", "Sorry, Technical issues");
			}
		});
		//
		task.run();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.class_menu, menu);
		return true;
	}

}
