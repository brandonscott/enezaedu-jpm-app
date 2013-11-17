package com.enezaeducation.mwalimu.server;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.enezaeducation.mwalimu.Constants;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
//import android.util.Log;

class Server {
	
	/*
	 * CONSTNANTS
	 */
	
	/** tag name */
	//private static final String TAG = "Server";
	
	/*
	 * MEMBERS
	 */
	
	/** single instance of HttpClient */
    private static HttpClient httpClient;
	
	/** server task */
	private ArrayList<ServerTask> tasks = new ArrayList<ServerTask>();
	
	/** iterator */
	private int iterator = 0;
	
	/** is working */
	private boolean working = false;
	
	/** singleton instance */
	private static Server instance = null;
	
	/*
	 * INITIALISATION
	 */
	
	/** singleton constructor */
	private Server() {
		// nothing
	}
	
	/** get singleton instance */
	static Server getInstance() {
		if(instance == null) {
			instance = new Server();
		}
		return instance;
	}
	
	/*
	 * PACKAGE METHODS
	 */

	/** add task with callback */
	void addTask(ServerTask task) {
		tasks.add(task);
		nextTask();
	}
	
	/** when task is done */
	void taskDone() {
		working = false;
		iterator++;
		nextTask();
	}
	
	/*
	 * PRIVATE METHODS
	 */
	
	/** execute next task if not busy */
	@SuppressLint("NewApi")
	private void nextTask() {
		if(!working) {
			if(iterator >= tasks.size()) {
				tasks = new ArrayList<ServerTask>();
				iterator = 0;
			}
			else {
				ServerTask task = tasks.get(iterator);
				if(!task.isPossible()) return;
				if(Build.VERSION.SDK_INT < 11) {
					tasks.get(iterator).execute();
				} else {
					tasks.get(iterator).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		}
	}
	
	/*
	 * PUBLIC STATIC METHODS
	 */
	
	/** returns single HTTP client */
	public static HttpClient getHttpClient() {
        if(httpClient == null) {
        	// create HTTP client
        	httpClient = new DefaultHttpClient();
            final HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, Constants.HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, Constants.HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, Constants.HTTP_TIMEOUT);
        }
        return httpClient;
    }
}
