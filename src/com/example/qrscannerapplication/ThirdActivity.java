package com.example.qrscannerapplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends Activity{
	ProgressDialog pDialog;
	Context context;
	final String LOGIN_URL = "http://icome.kz/c-api/log-user-visit/";

	 JSONParser jsonParser = new JSONParser();
	 EditText enterSumEditText;
	 String token,user_id;
	 TextView nameSurname;
	 ArrayList<Skidky> ALSkidky ;
	 String sum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_activity);

		Intent i = getIntent();
		context = this;
		
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Typeface OSBold = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Bold.ttf");
		Typeface OSLight = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Light.ttf");
		Typeface OSRegular = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
		Typeface OSSemibold = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Semibold.ttf");
		
		//TextView stockSumTextView = (TextView) findViewById(R.id.stockSumTextView);
		//Button stockButton = (Button) findViewById(R.id.stockButton);
		Button sendButton = (Button) findViewById(R.id.send);
		
		ImageView avatar = (ImageView)findViewById(R.id.avatar);
		 nameSurname = (TextView)findViewById(R.id.nameSurname);
		TextView email = (TextView)findViewById(R.id.email);
		TextView phone = (TextView)findViewById(R.id.phone);
		TextView discountTV = (TextView)findViewById(R.id.discountTextView);
		
		 enterSumEditText = (EditText)findViewById(R.id.enterSumEditText);
		enterSumEditText.setTypeface(OSRegular);
		
		String nameSurnameStr = i.getStringExtra("nameSurname");
		String emailStr = i.getStringExtra("email");
		String phoneStr = i.getStringExtra("phone");
		String avatarStr = i.getStringExtra("avatar");
		String stockSum =  i.getStringExtra("stockSum");
		String level1_rate = i.getStringExtra("level1_rate");
		
		if (Integer.parseInt(stockSum) >= 1){
		DataWrapper dw = (DataWrapper)i.getSerializableExtra("ALSkidky");
		ALSkidky = dw.getSkidky();
		}else{

		//	stockButton.setEnabled(false);
		}
		token = i.getStringExtra("token");
		user_id = i.getStringExtra("user_id");
		
		nameSurname.setText(nameSurnameStr);
		email.setText(emailStr);
		phone.setText(phoneStr);
	//	stockSumTextView.setText(stockSum);
		discountTV.setText(level1_rate);
		nameSurname.setTypeface(OSBold);
		email.setTypeface(OSLight);
		phone.setTypeface(OSLight);
	//	stockButton.setTypeface(OSSemibold);
		discountTV.setTypeface(OSSemibold);
		sendButton.setTypeface(OSBold);
	//	stockSumTextView.setTypeface(OSRegular);
		
	/*	stockButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),StockActivity.class);
				
				i.putExtra("ALSkidky",new DataWrapper(ALSkidky));
				startActivity(i);
			}
		});
		*/
		new DownloadImageTask((ImageView) findViewById(R.id.avatar))
        .execute(avatarStr);
		//avatar.setImageDrawable(getResources().getDrawable(R.id.logo));
		
		
	}
	public void send(View v){
		new LogUserVisit().execute();
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    @Override
		protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    @Override
		protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
	@SuppressLint("NewApi")
	class LogUserVisit extends AsyncTask<Void, Void, Void>{
		 @Override
		protected void onPreExecute() {

	            super.onPreExecute();
	            pDialog = new ProgressDialog(context);
	            pDialog.setCancelable(false);
	            pDialog.setMessage("Загрузка ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 
		@SuppressLint("NewApi")
		@Override
		protected Void doInBackground(Void... params) {
			sum = enterSumEditText.getText().toString();

			if (sum.isEmpty()){
				pDialog.dismiss();
			((Activity) context).runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(context,"Пожалуйста, введите сумму !", Toast.LENGTH_SHORT).show();
				  } 
				  });
			}else{
					
			final int  status;
			List params1 = new ArrayList();
            params1.add(new BasicNameValuePair("sum", sum));
            params1.add(new BasicNameValuePair("token", token));
            params1.add(new BasicNameValuePair("user_id", user_id));
            JSONObject json = jsonParser.makeHttpRequest(
                    LOGIN_URL, "POST", params1);
            try {
				status = json.getInt("status");
				if (status == 0){
				}else{
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			   super.onPostExecute(result);
         pDialog.dismiss();
         if (!sum.isEmpty()){
        	 Toast.makeText(context, "Готово",Toast.LENGTH_SHORT).show();
                    finish();
                
         }
	}
}
}
