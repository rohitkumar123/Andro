package com.example.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class LoginActivity extends Activity {
	public static final String TAG = LoginActivity.class.getSimpleName();
	public String data = "";
	public ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new LoginHandler(this).execute();
	}

	class LoginHandler extends AsyncTask<Void, Void, Void> {
		Activity activity;

		public LoginHandler(Activity activity) {
			this.activity = activity;
			dialog = ConstantsUtils.getprogessDialog(this.activity);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				data = run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				ResponseData responseData = ConstantsUtils.gson.fromJson(data,
						ResponseData.class);
				if (responseData != null) {
					Log.i(TAG, "SUCCES DATA::=>>>" + responseData.getStatus()
							+ "\n" + responseData.getMsg());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public String run() throws Exception {
		RequestBody requestBody = new FormEncodingBuilder()
				.add(ConstantsUtils.LOGIN_NAME, "test123")
				.add(ConstantsUtils.PASSWORD, "test123").build();
		ConstantsUtils.client.setConnectTimeout(ConstantsUtils.TIME_OUT,
				TimeUnit.SECONDS); // connect timeout
		ConstantsUtils.client.setReadTimeout(ConstantsUtils.TIME_OUT,
				TimeUnit.SECONDS); // socket timeout
		Request request = new Request.Builder().url(ConstantsUtils.LOGIN_URL)
				.post(requestBody).build();
		Response response = ConstantsUtils.client.newCall(request).execute();
		if (response.isSuccessful()) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			return response.body().string();
		} else {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			throw new IOException("Unexpected code " + response);
		}

	}
}
