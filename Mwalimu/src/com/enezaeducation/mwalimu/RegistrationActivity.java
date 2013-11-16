package com.enezaeducation.mwalimu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationActivity extends BaseActivity {

	private Spinner spinnerType = null;
	private EditText inputName = null;
	private EditText inputPassword = null;
	private EditText inputPhone = null;
	private EditText inputEmail = null;
	private EditText inputSchool = null;
	private EditText inputCode = null;
	
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
		
		// TODO: spinner listener, DEMO
		
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(btnRegisterListener);
	}

	/** when button clicked */
    private OnClickListener btnRegisterListener = new OnClickListener() {
		@Override
        public void onClick(View v) {
			if(inputName.length() == 0 || inputPassword.length() == 0 || inputPhone.length() == 0 || inputEmail.length() == 0 || inputSchool.length() == 0 || inputCode.length() == 0) {
				Utils.makeOkAlert(RegistrationActivity.this, "Fields", "Fill up all the fields");
			} else if(inputCode.getText().equals("123")) {
				Utils.makeOkAlert(RegistrationActivity.this, "Code", "Code should be 123");
			}
			
			//TODO: register online
			
			switchToMainActivity();
        }
    };
}
