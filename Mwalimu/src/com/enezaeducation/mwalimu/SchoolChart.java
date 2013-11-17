package com.enezaeducation.mwalimu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class SchoolChart extends Fragment {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	//private static final String TAG = "ContactList";
	 
	/*
	 * MEMBERS
	 */
	
	/** parent activity */
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
    	return inflater.inflate(R.layout.activity_school_chart, container, false);
    	
    }
    
    /** showing chart page, when tab is opened */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	final ArrayList<Bar> points = new ArrayList<Bar>();
    	
    	ServerTask.makeTask(activity, Constants.SCHOOLCHART_URL, new ServerCallback() {
			@Override
			public void run() {
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i("", response.toString());
						try {
							JSONArray schools = response.getJSONArray("schools");
							
							for(int i = 0; i < schools.length(); ++i) {
								JSONObject row = schools.getJSONObject(i);
								String name = row.getString("name");
								double score = row.getDouble("score");
							  	
								Bar bar = new Bar();
						    	bar.setColor(Color.parseColor("#99CC00"));
						    	bar.setName(name);
						    	bar.setValue(score);
						    	points.add(bar);
							}
							// ->
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
    	
    	BarGraph g = (BarGraph) view.findViewById(R.id.graph);
    	g.setBars(points);
    }
    

}