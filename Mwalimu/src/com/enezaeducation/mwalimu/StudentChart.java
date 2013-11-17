package com.enezaeducation.mwalimu;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import  com.enezaeducation.mwalimu.R;
import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class StudentChart extends Fragment {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	//private static final String TAG = "ContactList";
	 
	/*
	 * MEMBERS
	 */
	
	/** parent activity */
	@SuppressWarnings("unused")
	private Activity activity = null;
	
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
    	return inflater.inflate(R.layout.activity_student_chart, container, false);
    }
    
    /** showing list, when tab is opened */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	User user = User.getInstance(activity);
    	ServerTask task = new ServerTask(activity, Constants.STUDENTCHART_URL, new ServerCallback() {
			@Override
			public void run() {
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i("", response.toString());
						try {
							JSONArray students = response.getJSONArray("users");
							for(int i = 0; i < students.length(); ++i) {
								JSONObject row = students.getJSONObject(i);
								String name = row.getString("name");
								int score = row.getInt("average");
								
								TableRow tR = new TableRow(activity);
								TextView tV_txt1 = new TextView(activity);
								TextView tV_txt2 = new TextView(activity);
								tV_txt1.setText(name);
								tV_txt2.setText("" + score);
								tR.addView(tV_txt1);
						        tR.addView(tV_txt2);
						        
						        if(activity != null && activity.findViewById(R.id.tableLayout1) != null) {
						        	((TableLayout)activity.findViewById(R.id.tableLayout1)).addView(tR);
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