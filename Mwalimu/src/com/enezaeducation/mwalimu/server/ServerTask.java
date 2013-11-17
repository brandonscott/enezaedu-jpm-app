package com.enezaeducation.mwalimu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	public static final int GET = 0;
	public static final int POST = 1;
	public static final int DELETE = 2;
	public static final int PUT = 3;
	
	/*
	 * MEMBERS
	 */
	
	/** activity */
	private Activity activity = null;
	
	/** server page URL */
	private String url = null;
	
	/** post parameters */
	private JSONObject post = new JSONObject();
	
	/** callback */
	private ServerCallback callback = null;
	
	/** server response */
	private String response = null;
	
	private int method = GET;
	
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
	}
	
	/*
	 * 	PUBLIC MEMBERS
	 */
	
	/** adds new parameter */
	public boolean addParameter(String name, String value) {
		try {
			post.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
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
	
	public void setMethod(int method) {
		this.method = method;
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

			HttpRequestBase request = null;
			switch(method) {
			case GET: request = new HttpGet(url); break;
			case POST: request = new HttpPost(url); ((HttpPost)request).setEntity(new ByteArrayEntity(post.toString().getBytes("UTF8"))); break;
			case DELETE: request = new HttpDelete(url); break;
			case PUT: request = new HttpPut(url); ((HttpPut)request).setEntity(new ByteArrayEntity(post.toString().getBytes("UTF8"))); break;
			}
			request.setHeader("Content-Type", "application/json;charset=UTF-8");

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
}
