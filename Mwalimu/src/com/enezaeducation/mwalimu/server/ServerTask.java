package com.enezaeducation.mwalimu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.enezaeducation.mwalimu.Constants;
import com.enezaeducation.mwalimu.User;
import com.enezaeducation.mwalimu.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class ServerTask extends AsyncTask<Void, Void, Integer> {
	
	/*
	 * CONSTANTS
	 */
	
	/** tag name */
	private static final String TAG = "ServerTask";
	
	// return codes
	public static final int REQUEST_SUCCESS = 0;
	public static final int CONNECTION_ERROR = -1;
	public static final int REQUEST_FAIL = 1;
	
	/*
	 * MEMBERS
	 */
	
	/** activity */
	private Activity activity = null;
	
	/** server page URL */
	private String url = null;
	
	/** post parameters */
	private ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
	
	/** callback */
	private ServerCallback callback = null;
	
	/** server response */
	private String response = null;
	
	/*
	 * CONSTRUCTOR
	 */
	
	/** default constructor will append username and password to the post */
	public ServerTask(Activity activity, String url, ServerCallback callback) {
		this.activity = activity;
		this.url = url;
		this.callback = callback;
		
		// append username and password
		User user = User.getInstance(activity);
		addParameter("username", user.getUsername());
		addParameter("password", user.getPassword());
	}
	
	/*
	 * 	PUBLIC MEMBERS
	 */
	
	/** adds new parameter */
	public boolean addParameter(String name, String value) {
		return post.add(new BasicNameValuePair(name, value));
	}
	
	/** run the task */
	public void run() {
		Server.getInstance().addTask(this);
	}
	
	/*
	 * PACKAGE METHODS
	 */
	
	/** tell server that everything is ready to perform the task */
	boolean isPossible() {
		return Utils.isOnline(activity);
	}
    
    /*
	 * TASK METHODS
	 */

    
	/** execute HTTP request */
	@Override
	protected Integer doInBackground(Void... params) {
		BufferedReader in = null;

		try {
			HttpClient client = Server.getHttpClient();
			
			// all requests are post (username and password are always there)
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(post));

			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();

			this.response = sb.toString();
			return REQUEST_SUCCESS;
		} catch (UnsupportedEncodingException e) {
			// wrong post parameters
			if(Constants.DEBUG) {
				Log.e(TAG, "Unsupported encoding in POST", e);
			}
		} catch (IllegalStateException e) {
			// response handling error
			if(Constants.DEBUG) {
				Log.e(TAG, "Cannot read response", e);
			}
		} catch (IOException e) {
			if(Constants.DEBUG) {
				Log.e(TAG, "Connection error", e);
			}
			return CONNECTION_ERROR;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					if(Constants.DEBUG) {
						Log.e(TAG, "BufferedReader cannot be released", e);
					}
				}
			}
		}
		return REQUEST_FAIL;
	}
    
    /** callback the response */
    @Override
    protected void onPostExecute(Integer status) {
    	Server.getInstance().taskDone();
    	
    	// callback
    	if(callback != null) {
    		callback.set(response, status);
    		callback.run();
    	} else {
    		if(Constants.DEBUG) {
    			Log.d(TAG, "No callback called");
    		}
    	}
    }
    
    /*
     * PUBLIC STATIC METHODS
     */
    
    /** make task and execute */
    public static void makeTask(Activity activity, String url, ServerCallback callback) {
    	ServerTask task = new ServerTask(activity, url, callback);
    	task.run();
    }
}
