package com.enezaeducation.mwalimu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag */
    private static final String TAG = "LoginActivity";
	
	/*
	 * MEMBERS
	 */
	
	/** user */
	private User user = null;
	
	/*
	 * INTERFACE COMPONENTS
	 */
	
	/** input for username */
	private EditText fieldUsername = null;

	/** input for password */
	private EditText fieldPassword = null;
	
	private ProgressDialog progressDialog = null;
	
	
	/*
	 * INITIALISATION
	 */

	/** initialise members, interface and try to log in */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initialiseMembers();
        
        initialiseInterface(R.layout.activity_login);

    	// try to log in if it's not a log out
        Bundle extras = getIntent().getExtras();
    	if((extras == null || !extras.getBoolean("logout")) && Utils.isOnline(this) && user.verify()) {
    		// log in if : wasn't just logged out : device is online : password/username are not empty
    		login();
    	}
    }

	/** initialise valuable members */
	protected void initialiseMembers() {
		// initialise user
		user = User.getInstance(this);
	}
	
	/** initialise interface components */
	@Override
	protected void initialiseInterface(int layout) {
		super.initialiseInterface(layout);
		
		// initialise progress dialogue
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loggin in");
		progressDialog.setMessage("Please wait... Logging you in...");
		
		// initialise sign in button
		Button btnSignIn = (Button)findViewById(R.id.btnSignIn);
    	btnSignIn.setOnClickListener(btnListener);
    	
    	// initialise sign in button
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(btnRegisterListener);
    	
    	// username field
    	fieldUsername = (EditText)findViewById(R.id.inputUsername);
    	fieldUsername.setText(user.getUsername());
    	
    	// password field
    	fieldPassword = (EditText)findViewById(R.id.inputPassword);
    	fieldPassword.setText(user.getPassword());
	}
	
	/*
	 * PRIVATE METHODS
	 */
	
	/** try to log in, does not check Internet availability */
	private void login() {
		// show progress
		progressDialog.show();
		
		// LOGIN HERE
	}

	/** start MainActivity and closes LoginActivity */
	private void switchToMainActivity() {
		// TODO: Intent mainActivity = new Intent(this, MainActivity.class);
		// TODO: this.startActivity(mainActivity);
		finish();
	}
	
	/*
	 * PRIVATE CALLBACKS
	 */
	
	/** when log in button clicked */
	private OnClickListener btnListener = new OnClickListener() {
		@Override
        public void onClick(View v) {
			if(Utils.isOnline(LoginActivity.this)) {
				
				user.setUsername(fieldUsername.getText().toString());
				user.setPassword(fieldPassword.getText().toString());
	            
				// try to log in
				login();
			} else {
				Utils.makeOkAlert(LoginActivity.this, "Offline", "Your device is offline");
			}
        }
    };
    
    /** switch to login activity, closing the current one, logging out */
   	protected void switchToRegisterActivity() {
   		Intent intent = new Intent(this, RegistrationActivity.class);
   		this.startActivity(intent);
   		finish();
   	}
    
    /** when button clicked */
    private OnClickListener btnRegisterListener = new OnClickListener() {
		@Override
        public void onClick(View v) {
			switchToRegisterActivity();
        }
    };
}