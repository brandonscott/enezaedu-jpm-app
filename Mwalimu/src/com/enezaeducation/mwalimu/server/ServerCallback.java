package com.enezaeducation.mwalimu.server;

import org.json.JSONException;
import org.json.JSONObject;

import com.enezaeducation.mwalimu.Constants;

import android.net.ParseException;
import android.util.Log;

public abstract class ServerCallback implements Runnable {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	private static final String TAG = "ServerCallback";
	
	/*
	 * METHODS
	 */
	
	/** holds response, if null with status = REQUEST_SUCCESS, it's server error */
	protected JSONObject response = null;
	
	/** holds status of the response, @see ServerTask */
	protected int status;

	/*
	 * INITIALISATION
	 */
	
	/** set response */
	public void set(String response, int status) {
		this.status = status;
		if(response != null) {
			this.response = returnJSON(response);
		}
	}
	
	/*
	 * PRIVATE METHODS
	 */
	
	/** returns JSON of a server response */
    private static JSONObject returnJSON(String result) {
    	try {
	        JSONObject json = new JSONObject(result);
	        return json;
        } catch(JSONException e){
        	if(Constants.DEBUG) {
        		Log.e(TAG, "JSONException", e);
        	}
        } catch(ParseException e){
        	if(Constants.DEBUG) {
        		Log.e(TAG, "ParseException", e);
        	}
        }
        return null;
    }
}