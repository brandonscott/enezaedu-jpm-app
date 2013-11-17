package com.enezaeducation.mwalimu;

import com.enezaeducation.mwalimu.R;
import com.enezaeducation.mwalimu.SchoolChart;
import com.enezaeducation.mwalimu.StudentChart;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
/**
 * View to hold fragments for the reports
 */
public class PerformanceActivity extends BaseActivity {

	
	/*
	 * CONSTANTS
	 */

	/** tag name */
    //private static final String TAG = "MainActivity";
	
	/*
	 * 	MEMBERS
	 */

    
    /** school performance tab fragment */
	private SchoolChart schoolChart = null;
	
	/** student performance tab fragment */
	private StudentChart studentChart = null;
	
	/** last opened tab */
	private int selectedTab = 0;

	/*
	 * 	INITIALISATION
	 */

    /** method called on activity creation */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	if(savedInstanceState != null) {
            // Restore the last tab position
            selectedTab = savedInstanceState.getInt("selectedTab");
        }
    	
    	initialiseInterface(R.layout.activity_performance);
    }
    
    
    /** initialises action bar */
    protected ActionBar initialiseActionBar() {
    	ActionBar actionBar = super.initialiseActionBar();
    	
    	initialiseActionBarHomeButton();
    	
    	// tell the actionBar we want to use tabs
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	
    	// initiating both tabs and set text to it
    	ActionBar.Tab schoolChartTab = actionBar.newTab().setText("Schools");
    	ActionBar.Tab studentChartTab = actionBar.newTab().setText("Students");

    	// create the two fragments we want to use for display content
    	schoolChart = new SchoolChart();
    	schoolChart.initialise(this);
    	
    	studentChart = new StudentChart();
    	studentChart.initialise(this);

    	// set the tab listener, now we can listen for clicks
    	schoolChartTab.setTabListener(new TabsListener(schoolChart));
    	studentChartTab.setTabListener(new TabsListener(studentChart));

    	// add the two tabs to the actionBar
    	actionBar.addTab(schoolChartTab);
    	actionBar.addTab(studentChartTab);

    	// Set the last tab position, or zero by default
        getSupportActionBar().setSelectedNavigationItem(selectedTab);
        
        return actionBar;
    }
    
    
    /** save the tab selection state */
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the current tab position
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTab", getSupportActionBar().getSelectedNavigationIndex());
    }
	
   
}
