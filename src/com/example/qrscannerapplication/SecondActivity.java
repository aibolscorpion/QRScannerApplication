package com.example.qrscannerapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import eu.livotov.zxscan.ZXScanHelper;

public class SecondActivity extends Activity{
	private ArrayList<DataWrapper> skidky;
	String URL = "http://icome.kz/c-api/user-info-by-qr/";
	ProgressDialog pDialog;
	String scannedCode ;
	Context context;
	String text;
	String email,birthdate,nameSurname,phone,avatar;
	Button scanQR;
	int i=0;
	String token;
	 JSONParser jsonParser = new JSONParser();
	 JSONObject json;
		ArrayList<Skidky> ALSkidky = new ArrayList<Skidky>();
	String logo,price,old_price,nameOfCom,description;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);

		context = this;
        pDialog = new ProgressDialog(context);


		Intent i = getIntent();
		token = i.getStringExtra("token");
		
		Typeface OSBold = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Semibold.ttf");
		
		 scanQR = (Button)findViewById(R.id.scanQR);
		ImageButton exit = (ImageButton)findViewById(R.id.exit);
		
		scanQR.setTypeface(OSBold);
	}
	
	
	public  void exit(View v){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Вы действительно хотите выйти ?").setCancelable(false).setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
            	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            	Editor editor = pref.edit();
            	editor.clear();
            	
            	editor.commit();
                Intent i = new Intent(context,MainActivity.class);
                finish();
                startActivity(i);
            }
        })
        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int id) {
        			dialog.cancel();
        	}
        });

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
}
	
	public void scanQR(View v){
		ZXScanHelper.scan(this,123456);
	}
	  
	  @Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
	 
			 if (resultCode == RESULT_OK && requestCode == 123456)
        {
		     if (isOnline()){
             scannedCode = ZXScanHelper.getScannedCode(data);
             
             new getUserInfo().execute();
		     }
        }
    } 
	  class getUserInfo extends AsyncTask<Void, Void, Void>{

		  @Override
		protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog.setMessage("Загрузка ...");
	            pDialog.setIndeterminate(false); 
	            pDialog.setCancelable(true);
	            pDialog.show();
}
	
		 


		@Override
		protected Void doInBackground(Void... params) {
			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("token",token));
			 
			try {
				if (proverkaQr(scannedCode)){
				json = jsonParser.makeHttpRequest(
				       URL+scannedCode, "GET", params1);
				
				
				
				if (json.getInt("status") == 0){

					JSONObject data = json.getJSONObject("data");
					 email = data.getString("email");
					 birthdate = data.getString("birthdate");
					 phone = data.getString("phone");
					String user_id = data.getString("id");
					String name = data.getString("firstname");
					String surname = data.getString("lastname");
					String avatar = data.getString("avatar");
					
					final JSONArray stocks = data.getJSONArray("stocks");
					JSONArray discounts = data.getJSONArray("discounts");
					 Intent intent = new Intent(context,ThirdActivity.class);
				if (stocks.length() >= 1){
					
					JSONObject object = stocks.getJSONObject(0);
					for (int i=0;i<stocks.length();i++){
						JSONObject object2 = stocks.getJSONObject(i);
						
						 logo = object2.getString("logo");
						 price = object2.getString("price");
						 old_price = object2.getString("old_price");
						JSONArray JS = object2.getJSONArray("locales");
						
						for (int j=0;j<JS.length();j++){
							JSONObject localesObj = JS.getJSONObject(j);
							 nameOfCom  = localesObj.getString("name");
							 description = localesObj.getString("description");
						}
						ALSkidky.add(new Skidky(logo,price,old_price,description,nameOfCom));

						intent.putExtra("ALSkidky",new DataWrapper(ALSkidky));
					}
						
					 
					}
				if (discounts.length() >= 1){
					JSONObject object = discounts.getJSONObject(0);
					String rate = object.getString("level1_rate");
					String level1rate="Клубная карта - "+String.format("%.0f",(Float.parseFloat(rate)*100))+"%";
					
					intent.putExtra("level1_rate",level1rate);
				}else {
					intent.putExtra("level1_rate","Клубной карты нету");
				}
					nameSurname = name+" "+surname;

						intent.putExtra("user_id",user_id );
						intent.putExtra("email",email);
						intent.putExtra("birthdate",birthdate); 
						intent.putExtra("phone",phone);
						intent.putExtra("nameSurname",nameSurname);
						intent.putExtra("avatar",avatar);
						intent.putExtra("token",token);
						intent.putExtra("stockSum", stocks.length()+"");
					
					startActivity(intent); 
				}
				
				else {
				
					
				}
				}else{

				((Activity) context).runOnUiThread(new Runnable() {
					  public void run() {
						  Toast.makeText(context,"User not found ", Toast.LENGTH_SHORT).show();;
					  } 
					  });
				}
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
						return null;
						
		}
				@Override
		 protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
			pDialog.dismiss();
      
 }
		
		 
	  }	 
	  public boolean proverkaQr(String qrCode){
		  
		  for (int i = 0;i<qrCode.length();i++){
			if (!Character.isLetterOrDigit(qrCode.charAt(i))){
				return false;
			}
		  }
		  return true;
		  
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
