package com.enezaeducation.mwalimu;

public class Constants {
	public final static boolean DEBUG = true;
	
	public final static boolean SKIP_LOGIN = true;
	
	public static final int HTTP_TIMEOUT = 30 * 1000; // 30 seconds
	
	public static final String BASE_URL = "http://54.220.201.194/";
	
	public static final String LOGIN_URL = BASE_URL + "authenticate";
	
	public static final String REGISTRATION_URL = BASE_URL + "registration";
}
