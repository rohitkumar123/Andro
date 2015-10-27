package com.example.okhttp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.format.Formatter;

public class ConstantsUtils {
	
	private ConstantsUtils(){
		
	}
	public static final OkHttpClient client = new OkHttpClient();
	public static final Gson gson = new Gson();
	public static final String URL                  = "http://zoovemama.com/betaaws/api/";
	public static final String CREATE_USER_URL      = URL + "createUser/";
	public static final String LOGIN_URL            = URL + "login/";
	public static final String FORGOT_PASSWORD_URL  = URL + "forgotPassword/";
	public static final String GET_USER_DETAILS_URL = URL + "getUserDetails/";
	public static final String GET_USER_INFO_URL    = URL + "getUserInfo/";
	public static final String FIRST_NAME            = "first_name";
	public static final String MIDDLE_NAME           = "middle_name";
	public static final String LAST_NAME             = "last_name";
	public static final String LOGIN_NAME            = "login_name";
	public static final String EMAIL                = "email";
	public static final String EMAIL_TYPE           = "email_type";
	public static final String PASSWORD             = "password";
	public static final String USERBIO              = "userbio";
	public static final String SCREEN_NAME           = "screen_name";
	public static final String ORGANIZATION         = "organization";
	public static final String IPADDRESS            = "ipaddress";
	public static final String DATE_OF_BIRTH        = "date_of_birth";
	public static final String SEX                  = "sex";
	public static final int TIME_OUT                = 15;
	public static final String MESSAGE              = "Please wait...";
	public static String getIPAddress() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> enumnet = NetworkInterface
					.getNetworkInterfaces();
			NetworkInterface netinterface = null;

			while (enumnet.hasMoreElements()) {
				netinterface = enumnet.nextElement();

				for (Enumeration<InetAddress> enumip = netinterface
						.getInetAddresses(); enumip.hasMoreElements();) {
					InetAddress inetAddress = enumip.nextElement();

					if (!inetAddress.isLoopbackAddress()) {
						// ipaddress = inetAddress.getHostAddress();
						ipaddress = Formatter.formatIpAddress(inetAddress
								.hashCode());
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipaddress;
	}
	
	public static ProgressDialog getprogessDialog(Activity activity){
		ProgressDialog dialog = new ProgressDialog(activity);
    	dialog.setMessage(ConstantsUtils.MESSAGE);
    	dialog.setIndeterminate(false);
    	dialog.setCancelable(true);
    	dialog.show();
    	return dialog;
	}
}
