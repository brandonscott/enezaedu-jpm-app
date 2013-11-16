package com.enezaeducation.mwalimu;

import  com.enezaeducation.mwalimu.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


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
    	return inflater.inflate(R.layout.activity_school_chart, container, false);
    	
    }
    
    /** showing chart page, when tab is opened */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	WebView myWebView = (WebView) view.findViewById(R.id.webView1);
		myWebView.loadUrl("http://beta.html5test.com/");
    }
    

}