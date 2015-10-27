package com.example.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MainActivity extends Activity {
	private static final MediaType MEDIA_TYPE_PNG = MediaType
			.parse("image/png");
	public static final String TAG = MainActivity.class.getSimpleName();
	private static int RESULT_LOAD_IMG = 1;
	String imgDecodableString;
	private ImageView img;
	public ProgressDialog dialog;
	public String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		img = (ImageView) findViewById(R.id.ImageView01);
		((Button) findViewById(R.id.Button01))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						Intent galleryIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
					}
				});
	}

	class OkHttpHandler extends AsyncTask<Void, Void, Void> {
		Activity activity;

		public OkHttpHandler(Activity activity) {
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
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addFormDataPart(ConstantsUtils.FIRST_NAME, "test321")
				.addFormDataPart(ConstantsUtils.MIDDLE_NAME, "kumar")
				.addFormDataPart(ConstantsUtils.LAST_NAME, "test321")
				.addFormDataPart(ConstantsUtils.LOGIN_NAME, "test321")
				.addFormDataPart(ConstantsUtils.EMAIL, "test321@gmail.com")
				.addFormDataPart(ConstantsUtils.EMAIL_TYPE, "E")
				.addFormDataPart(ConstantsUtils.PASSWORD, "test321")
				.addFormDataPart(ConstantsUtils.USERBIO, "test321")
				.addFormDataPart(ConstantsUtils.SCREEN_NAME, "test321")
				.addPart(
						RequestBody.create(MEDIA_TYPE_PNG, new File(
								imgDecodableString)))
				.addFormDataPart(ConstantsUtils.ORGANIZATION, "Zoowimama")
				.addFormDataPart(ConstantsUtils.IPADDRESS,
						ConstantsUtils.getIPAddress())
				.addFormDataPart(ConstantsUtils.DATE_OF_BIRTH, "1984-08-02")
				.addFormDataPart(ConstantsUtils.SEX, "M").build();

		ConstantsUtils.client.setConnectTimeout(ConstantsUtils.TIME_OUT,
				TimeUnit.SECONDS); // connect timeout
		ConstantsUtils.client.setWriteTimeout(ConstantsUtils.TIME_OUT, TimeUnit.SECONDS);
		ConstantsUtils.client.setReadTimeout(ConstantsUtils.TIME_OUT,
				TimeUnit.SECONDS); // socket timeout

		Request request = new Request.Builder()
				.url(ConstantsUtils.CREATE_USER_URL).post(requestBody).build();
		Response response = ConstantsUtils.client.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				imgDecodableString = cursor.getString(columnIndex);
				cursor.close();

				new OkHttpHandler(this).execute();
				// Set the Image in ImageView after decoding the String
				img.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}

	}
}
