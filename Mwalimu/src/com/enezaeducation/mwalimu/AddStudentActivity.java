package com.enezaeducation.mwalimu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddStudentActivity extends BaseActivity {
	
	private ListView listView = null;

	private final static String TAG = "AddStudentActivity";
	
	private ArrayList<Integer> userIds = null;
	private ArrayList<String> userNames = null;
	
	private int classId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent().getExtras() == null) {
			// error
			finish();
		}
		classId = getIntent().getExtras().getInt("id");
		Log.i(TAG, ""+classId);
		
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
				ServerTask task = new ServerTask(AddStudentActivity.this, Constants.BASE_URL + "classes/" + classId + "/add/" + userIds.get(arg2), new ServerCallback() {
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
										ClassStudentsActivity.self.loadClasses();
										finish();
									} else {
										Utils.makeOkAlert(AddStudentActivity.this, "Server Error", "Class list cannot be added");
									}
									return;
								} catch(JSONException e) {
									if(Constants.DEBUG) {
										Log.e(TAG, "Server error", e);
									}
								}
							}
						}
						Utils.makeOkAlert(AddStudentActivity.this, "Server Error", "Sorry, Technical issues");
					}
				});
				task.run();
			}
		});
	}
	
	private void loadClasses() {
		ServerTask task = new ServerTask(AddStudentActivity.this, Constants.BASE_URL + "users/student", new ServerCallback() {
			@Override
			public void run() {
				// hide progress dialogue
				
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i(TAG, response.toString());
						try {
							JSONArray classes = response.getJSONArray("students");
							userIds = new ArrayList<Integer>();
							userNames = new ArrayList<String>();
							for(int i = 0; i < classes.length(); ++i) {
								JSONObject row = classes.getJSONObject(i);
								int id = row.getInt("id");
								String name = row.getString("name");
								userIds.add(id);
								userNames.add(name);
							}
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_list_item_1, userNames);
							listView.setAdapter(adapter);
							return; // these error (if any) are not 'server' errors
						} catch(JSONException e) {
							if(Constants.DEBUG) {
								Log.e(TAG, "Server error", e);
							}
						}
					}
				}
				Utils.makeOkAlert(AddStudentActivity.this, "Server Error", "Sorry, Technical issues");
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
