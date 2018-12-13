package com.example.qrscannerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity);

		TextView TV = (TextView)findViewById(R.id.getChItemsTV);
		
		Intent i = getIntent();
		TV.setText(i.getStringExtra("title"));
		
	}
}
