package com.enezaeducation.mwalimu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegistrationActivity extends BaseActivity {

	private Spinner spinnerType = null;
	private EditText inputName = null;
	private EditText inputPassword = null;
	private EditText inputPhone = null;
	private EditText inputEmail = null;
	private EditText inputSchool = null;
	private EditText inputCode = null;
	
	private final static String TAG = "registrationActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initialiseInterface(R.layout.activity_registration);
		initialiseActionBarHomeButton();
		
		initialiseMembers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	protected void initialiseMembers() {
		spinnerType = (Spinner)findViewById(R.id.spinnerType);
		inputName = (EditText)findViewById(R.id.inputName);
		inputPassword = (EditText)findViewById(R.id.inputPassword);
		inputPhone = (EditText)findViewById(R.id.inputPhone);
		inputEmail = (EditText)findViewById(R.id.inputEmail);
		inputSchool = (EditText)findViewById(R.id.inputSchool);
		inputCode = (EditText)findViewById(R.id.inputCode); 
		
		spinnerType.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		        if(position != 0) {
		        	Utils.makeOkAlert(RegistrationActivity.this, "Demo", "Not available in this demo");
		        	spinnerType.setSelection(0);
		        }
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(btnRegisterListener);
	}

	/** when button clicked */
    private OnClickListener btnRegisterListener = new OnClickListener() {
		@Override
        public void onClick(View v) {
			/*if(inputName.length() == 0 || inputPassword.length() == 0 || inputPhone.length() == 0 || inputEmail.length() == 0 || inputSchool.length() == 0 || inputCode.length() == 0) {
				Utils.makeOkAlert(RegistrationActivity.this, "Fields", "Fill up all the fields"); return;
			} else if(!inputCode.getText().equals("123")) {
				Utils.makeOkAlert(RegistrationActivity.this, "Code", "Code should be 123"); return;
			}*/
			
			String pattern = "\\+\\d{8,}";
		    Pattern r = Pattern.compile(pattern);
		    Matcher m = r.matcher(inputPhone.getText());
		    if (!m.find( )) {
		    	Utils.makeOkAlert(RegistrationActivity.this, "Phone", "No spaces! Country code!"); return;
		    }
		    
			
			ServerTask task = new ServerTask(RegistrationActivity.this, Constants.REGISTRATION_URL, new ServerCallback() {
				@Override
				public void run() {
					// hide progress dialogue
					
					if(status == ServerTask.REQUEST_SUCCESS) {
						// response available
						if(response != null) {
							boolean loggedIn;
							Log.i(TAG, response.toString());
							try {
								loggedIn = response.getBoolean("valid");
								if(loggedIn) {
									switchToMainActivity();
								} else {
									String desc = response.getString("description");
									Utils.makeOkAlert(RegistrationActivity.this, "Error", desc);
								}
								
								return; // these error (if any) are not 'server' errors
							} catch(JSONException e) {
								if(Constants.DEBUG) {
									Log.e(TAG, "Server error", e);
								}
							}
						}
					}
					Utils.makeOkAlert(RegistrationActivity.this, "Server Error", "Sorry, Technical issues");
				}
			});
			//
			//task.addParameter("username", user.getUsername());
			//task.addParameter("password", user.getPassword());
			// TODO: fields
			// TODO: preload school shit
			task.run();
        }
    };
}
