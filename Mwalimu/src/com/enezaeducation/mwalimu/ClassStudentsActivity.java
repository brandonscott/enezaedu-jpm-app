package com.enezaeducation.mwalimu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ClassStudentsActivity extends BaseActivity {
	
	private ListView listView = null;
	
	private Button btnAddUser = null;

	private final static String TAG = "ClassStudentsActivity";
	
	private ArrayList<Integer> userIds = null;
	private ArrayList<String> userNames = null;
	
	private int classId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null) {
			// error
			finish();
		}
		classId = savedInstanceState.getInt("id");
		
		initialiseInterface(R.layout.activity_class_students);
		
		initialiseMembers();
	}
	
	protected void initialiseMembers() {
		// list ivew
		listView = (ListView)findViewById(R.id.classUsersListView);
		
		String[] content = { "No classes" };		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content);
		listView.setAdapter(adapter);
		
		loadClasses();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				User user = User.getInstance(ClassStudentsActivity.this);
				ServerTask task = new ServerTask(ClassStudentsActivity.this, Constants.BASE_TEMP_URL + "classes/" + classId + "/user/" + user.getId() + "/delete", new ServerCallback() {
					@Override
					public void run() {
						// hide progress dialogue
						
						if(status == ServerTask.REQUEST_SUCCESS) {
							// response available
							if(response != null) {
								Log.i(TAG, response.toString());
								try {
									JSONArray classes = response.getJSONArray("classes");
									userIds = new ArrayList<Integer>();
									userNames = new ArrayList<String>();
									for(int i = 0; i < classes.length(); ++i) {
										JSONObject row = classes.getJSONObject(i);
										int id = row.getInt("id");
										String name = row.getString("name");
										userIds.add(id);
										userNames.add(name);
									}
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClassStudentsActivity.this, android.R.layout.simple_list_item_1, userNames);
									listView.setAdapter(adapter);
									return; // these error (if any) are not 'server' errors
								} catch(JSONException e) {
									if(Constants.DEBUG) {
										Log.e(TAG, "Server error", e);
									}
								}
							}
						}
						Utils.makeOkAlert(ClassStudentsActivity.this, "Server Error", "Sorry, Technical issues");
					}
				});
				//
				task.run();
			}
		});
		
		// + button
		btnAddUser = (Button)findViewById(R.id.btnAddClass);
		btnAddUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(ClassStudentsActivity.this, AddClassStudentActivity.class);
				//ClassStudentsActivity.this.startActivity(intent);
			}
		});
	}
	
	private void loadClasses() {
		final User user = User.getInstance(this);
		Log.i(TAG, Constants.BASE_TEMP_URL + "users/" + user.getId() + "/classes");
		ServerTask task = new ServerTask(ClassStudentsActivity.this, Constants.BASE_TEMP_URL + "users/" + user.getId() + "/classes", new ServerCallback() {
			@Override
			public void run() {
				// hide progress dialogue
				
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i(TAG, response.toString());
						try {
							JSONArray classes = response.getJSONArray("classes");
							userIds = new ArrayList<Integer>();
							userNames = new ArrayList<String>();
							for(int i = 0; i < classes.length(); ++i) {
								JSONObject row = classes.getJSONObject(i);
								int id = row.getInt("id");
								String name = row.getString("name");
								userIds.add(id);
								userNames.add(name);
							}
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClassStudentsActivity.this, android.R.layout.simple_list_item_1, userNames);
							listView.setAdapter(adapter);
							return; // these error (if any) are not 'server' errors
						} catch(JSONException e) {
							if(Constants.DEBUG) {
								Log.e(TAG, "Server error", e);
							}
						}
					}
				}
				Utils.makeOkAlert(ClassStudentsActivity.this, "Server Error", "Sorry, Technical issues");
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