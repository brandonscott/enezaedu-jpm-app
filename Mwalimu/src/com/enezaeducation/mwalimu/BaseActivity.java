package com.enezaeducation.mwalimu;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ViewConfiguration;

abstract public class BaseActivity extends ActionBarActivity {

	/*
	 * CONSTANTS
	 */

	/** tag */
	private static final String TAG = "BaseActivity";

	/*
	 * INITIALISATION
	 */

	/** initialise the interface and set the content view layout */
	
	protected void initialiseInterface(int layout) {
		setContentView(layout);
		initialiseActionBar();
	}
	
	 /** initialises action bar */
	protected ActionBar initialiseActionBar() {
    	// actionBar gets initiated
    	ActionBar actionBar = getSupportActionBar();

    	if(Build.VERSION.SDK_INT >= 11) {
	    	// remove the title (API 11+)
    		actionBar.setDisplayShowTitleEnabled(false);
    		actionBar.setDisplayUseLogoEnabled(true);
    	}
    	
    	actionBar.setDisplayHomeAsUpEnabled(true);
    	
    	return actionBar;
    }
	
	/*
	 * 	PROTECTED METHODS
	 */

    /** switch to login activity, closing the current one, logging out */
	protected void switchToLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("logout", true);
		this.startActivity(intent);
		finish();
	}
	
	/** start MainActivity and closes LoginActivity */
	protected void switchToMainActivity() {
		Intent mainActivity = new Intent(this, MainActivity.class);
		this.startActivity(mainActivity);
		finish();
	}
	
	/** makes home button in the action bar clickable */
	protected void initialiseActionBarHomeButton() {
		try {
    		ViewConfiguration config = ViewConfiguration.get(this);
    		Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
    		if(menuKeyField != null) {
    			menuKeyField.setAccessible(true);
    			menuKeyField.setBoolean(config, false);
    		}
    	} catch (Exception e) {
    		if(Constants.DEBUG) {
    			Log.e(TAG, "sHasPermanentMenuKey error (old android)");
    		}
    	}
	}
}
