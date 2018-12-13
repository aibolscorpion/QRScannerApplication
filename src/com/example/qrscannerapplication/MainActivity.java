package com.example.qrscannerapplication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	 Context context;
	 ProgressDialog pDialog;

		final String LOGIN_URL = "http://icome.kz/c-api/authenticate/";

	    private static final String TAG_MESSAGE = "message";

	    EditText loginET;
		EditText passwordET;
		Button enterButton ;
		
		SharedPreferences pref;

	 JSONParser jsonParser = new JSONParser();
		 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
			
		 context = this;
		 
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		

		String token =  pref.getString("authorized",null);
		if (token != null){
			Intent i = new Intent(context, SecondActivity.class);
			i.putExtra("token",token);
            finish();
            startActivity(i);
		}
			
		
		 loginET = (EditText)findViewById(R.id.login);
		 passwordET = (EditText)findViewById(R.id.password);
		 enterButton = (Button)findViewById(R.id.enter); 
		Typeface OSBold = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Bold.ttf");
	
		Typeface OSRegular = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
		
		loginET.setTypeface(OSRegular);
		passwordET.setTypeface(OSRegular);
		enterButton.setTypeface(OSBold);
	}
	
	  
	public void login(View v){
		if (isOnline()){
        new AttemptLogin().execute();
		}
	}
	
	
	 class AttemptLogin extends AsyncTask<Void, Void, Void> {
		 boolean failure = false;
		 @Override
		protected void onPreExecute() {

	            super.onPreExecute();
	            pDialog = new ProgressDialog(context);
	            pDialog.setMessage("Загрузка ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 

		@Override
		protected Void doInBackground(Void... params) {

			  int success;
			String login = loginET.getText().toString();
            String password = passwordET.getText().toString();
            try {
                // Building Parameters
                List params1 = new ArrayList();
                params1.add(new BasicNameValuePair("login", login));
                params1.add(new BasicNameValuePair("password", password));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params1);
                success = json.getInt("status");
                if (success == 0) {

                    JSONObject jsonData = json.getJSONObject("data");
                    String token = jsonData.getString("token");
                	
                	Editor editor = pref.edit();
                	editor.putString("authorized", token);
                	editor.commit();
                	
                	Intent i = new Intent(context, SecondActivity.class);
                	i.putExtra("token",token);
                    finish();
                    startActivity(i);
                }else{
                	ArrayList<String> loginPassword = new ArrayList<String>();
                	loginPassword.add(login);
                	loginPassword.add(password);
                	incorrectpassword(loginPassword);
                	 }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
			return null;
		}
		 
		@Override
		 protected void onPostExecute(Void result) {
	            // dismiss the dialog once product deleted
			 super.onPostExecute(result);
	            pDialog.dismiss();
	            
	 }
}
	 
	 @SuppressLint("NewApi")
	public void incorrectpassword(final ArrayList<String> loginPassword){
		 
		 ((Activity) context).runOnUiThread(new Runnable() {
			  @Override
			public void run() {
				  if (loginPassword.get(0).isEmpty() && loginPassword.get(1).isEmpty()){
				  Toast.makeText(context, "Введите логин и пароль", Toast.LENGTH_LONG).show();
				  }
				  else  if (loginPassword.get(0).isEmpty()){
					  Toast.makeText(context, "Введите логин", Toast.LENGTH_LONG).show();
				  }else if (loginPassword.get(1).isEmpty()){
					  Toast.makeText(context, "Введите пароль", Toast.LENGTH_LONG).show();
				  }else {
					  Toast.makeText(context, "Вы ввели неправильный логин или пароль", Toast.LENGTH_LONG).show();
				  }
			  }
			});
		 
	 }
	 public boolean isOnline() {
		    ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		    if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
		        Toast.makeText(context, "Нет соединения с интернетом !", Toast.LENGTH_LONG).show();
		        return false;
		    }
		return true; 
		}
}
