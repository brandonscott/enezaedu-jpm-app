package com.enezaeducation.mwalimu;

import android.app.Activity;
import android.content.SharedPreferences;

public class User {
	
	/*
	 * 	CONSTANTS
	 */
	
	/** tag */
	//private static final String TAG = "User";
	
	/** shared preferences key */
	public static final String SHARED_PREF = "cridentials";
	
	/** user name key for shared preferences */
	public static final String PREF_USERNAME = "username";
	
	/** password key for shared preferences */
	public static final String PREF_PASSWORD = "password";
	
	public static final String PREF_ID = "id";
	
	/*
	 *  MEMBERS
	 */
	
	/** user name */
	private String username = null;
	
	/** user password */
	private String password = null;
	
	/** shared preferences */
	SharedPreferences sharedPreferences = null;
	
	/** singleton instance */
	private static User instance = null;
	
	private int id = 11; // TODO: -1
	
	/*
	 *  INITIALISATION
	 */
	
	/** singleton empty constructor */
	private User(Activity activity) {
		sharedPreferences = activity.getSharedPreferences(SHARED_PREF, 0);
		
		username = sharedPreferences.getString(User.PREF_USERNAME, "");
		password = sharedPreferences.getString(User.PREF_PASSWORD, "");
		id = sharedPreferences.getInt(User.PREF_ID, 11); // TODO: -1
	}
	
	/**
	 * Singleton method: get instance
	 * @param activity
	 * @return instance
	 */
	public static User getInstance(Activity activity) {
		if(instance == null) {
			instance = new User(activity);
		}
		return instance;
	}

	/*
	 * 	GET SET METHODS
	 */
	
	/** returns username */
	public String getUsername() {
		return username;
	}

	/** sets new username, trimming it */
	public void setUsername(String username) {
		this.username = username.trim();
	}

	/** returns password */
	public String getPassword() {
		return password;
	}

	/** sets new password, trimming it */
	public void setPassword(String password) {
		this.password = password.trim();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	/*
	 * 	PUBLIC METHODS
	 */
	
	/** save user */
	public void save() {
		SharedPreferences.Editor edit = sharedPreferences.edit();
		
		edit.putString(PREF_USERNAME, username);
		edit.putString(PREF_PASSWORD, password);
		edit.putInt(PREF_ID, id);
		
        edit.commit();
    }
	
	/** remove user */
	public void remove() {
		SharedPreferences.Editor edit = sharedPreferences.edit();
		
		edit.remove(PREF_USERNAME);
		edit.remove(PREF_PASSWORD);
		edit.remove(PREF_ID);
		
        edit.commit();
		
		username = "";
		password = "";
    }
	
	/** verify username and password by checking their length */
	public boolean verify() {
		return username.length() > 0 && password.length() > 0;
	}
}