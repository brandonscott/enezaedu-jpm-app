package com.enezaeducation.mwalimu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RegistrationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseInterface(R.layout.activity_registration);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

}
