package com.enezaeducation.mwalimu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	/** returns true if device is connected to Internet */
	public static boolean isOnline(Context context){
		if(context == null) return false;
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if(info != null) {
				for(int i = 0; i < info.length; ++i) {
					if(info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** make a normal OK alert */
	public static void makeOkAlert(Activity activity, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alertDialog.show();
	}
}