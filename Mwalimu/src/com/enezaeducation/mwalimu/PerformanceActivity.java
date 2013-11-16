package com.enezaeducation.mwalimu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PerformanceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseInterface(R.layout.activity_performance);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.performance, menu);
		return true;
	}

}
