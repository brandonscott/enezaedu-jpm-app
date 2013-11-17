package com.enezaeducation.mwalimu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.server.ServerCallback;
import com.enezaeducation.mwalimu.server.ServerTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegistrationActivity extends BaseActivity {

	private Spinner spinnerType = null;
	private EditText inputName = null;
	private EditText inputSurname = null;
	private EditText inputPassword = null;
	private EditText inputPhone = null;
	private EditText inputEmail = null;
	private Spinner spinnerSchool = null;
	private EditText inputCode = null;
	private Spinner spinnerGender = null;
	
	private final static String TAG = "registrationActivity";
	
	private ArrayList<String> schoolNames = new ArrayList<String>();
	private ArrayList<Integer> schoolIds = new ArrayList<Integer>();
	
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
		spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
		inputName = (EditText)findViewById(R.id.inputName);
		inputSurname = (EditText)findViewById(R.id.inputSurname);
		inputPassword = (EditText)findViewById(R.id.inputPassword);
		inputPhone = (EditText)findViewById(R.id.inputPhone);
		inputEmail = (EditText)findViewById(R.id.inputEmail);
		spinnerSchool = (Spinner)findViewById(R.id.spinnerSchool);
		inputCode = (EditText)findViewById(R.id.inputCode);
		
		ServerTask task = new ServerTask(RegistrationActivity.this, Constants.SCHOOLS_URL, new ServerCallback() {
			@Override
			public void run() {
				// hide progress dialogue
				
				if(status == ServerTask.REQUEST_SUCCESS) {
					// response available
					if(response != null) {
						Log.i(TAG, response.toString());
						try {
							JSONArray schools = response.getJSONArray("schools");
							for(int i = 0; i < schools.length(); ++i) {
								JSONObject sc = schools.getJSONObject(i);
								int id = sc.getInt("id");
								String name = sc.getString("school_name");
								schoolIds.add(id);
								schoolNames.add(name);
							}
							
			                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, schoolNames);
			                spinnerSchool.setAdapter(categoriesAdapter);
							
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
		task.run();
		
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
			if(inputName.length() == 0 || inputPassword.length() == 0 || inputPhone.length() == 0 || inputEmail.length() == 0 || inputCode.length() == 0) {
				Utils.makeOkAlert(RegistrationActivity.this, "Fields", "Fill up all the fields"); return;
			} else if(!inputCode.getText().toString().equals("123")) {
				Utils.makeOkAlert(RegistrationActivity.this, "Code", "Code should be 123"); return;
			}
			
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
									JSONObject obj = response.getJSONObject("user");
									User user = User.getInstance(RegistrationActivity.this);
									user.setId(obj.getInt("id"));
									
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
			task.addParameter("first_name", inputName.getText().toString());
			task.addParameter("last_name", inputSurname.getText().toString());
			task.addParameter("password", inputPassword.getText().toString());
			task.addParameter("mobile_number", inputPhone.getText().toString());
			task.addParameter("email", inputEmail.getText().toString());
			task.addInt("user_type", 1);
			task.addInt("school", schoolIds.get(spinnerSchool.getSelectedItemPosition()));
			task.addParameter("gender", spinnerGender.getSelectedItem().toString());
			task.setMethod(ServerTask.POST);
			task.run();
        }
    };
}
