package com.enezaeducation.mwalimu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity {
	
	private static final int PERFORMANCE = 0;
	private static final int CLASSES = 1;
	private static final int CHAT = 2;
	private static final int ASSIGN = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseInterface(R.layout.activity_main);
		
		ActionBar actionBar = getSupportActionBar();
    	actionBar.setDisplayHomeAsUpEnabled(false);
		
		initialiseMembers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void initialiseMembers() {
		Button btnPerformance = (Button)findViewById(R.id.btnPerformance);
		Button btnClasses = (Button)findViewById(R.id.btnClasses);
		Button btnChat = (Button)findViewById(R.id.btnChat);
		Button btnAssignWork = (Button)findViewById(R.id.btnAssignWork);
		
		btnPerformance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(PERFORMANCE);
			}
		});
		
		btnClasses.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(CLASSES);
			}
		});
		
		btnChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(CHAT);
			}
		});
		
		btnAssignWork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(ASSIGN);
			}
		});
	}
	
	private void goTo(int where) {
		Intent intent = null;
		switch(where) {
		case PERFORMANCE:
			intent = new Intent(this, PerformanceActivity.class);
			break;
		case CLASSES:
			intent = new Intent(this, ClassActivity.class);
			break;
		case CHAT:
			intent = new Intent(this, ChatActivity.class);
			break;
		case ASSIGN:
			intent = new Intent(this, QuizActivity.class);
			break;
		}
		this.startActivity(intent);
	}
	
	/** when menu item is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
    	switch (item.getItemId()) {
    	case R.id.action_logout:
    		switchToLoginActivity();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

}
