package com.example.qrscannerapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StockActivity extends Activity {
	int img[] = {R.drawable.img_list_vew,R.drawable.img_list_vew,R.drawable.img_list_vew};
	
	Typeface OSExtraBold;
	ListView stocksListView ;
	ArrayList<ListData> AL = new ArrayList<ListData>();
	ArrayList<Skidky> ALSkidky;
	CatalogAdapter CA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock);
		
		 Intent intent = getIntent();
		 DataWrapper dw = (DataWrapper)intent.getSerializableExtra("ALSkidky");
		 ALSkidky = dw.getSkidky();
		 
		 OSExtraBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBold.ttf");
		 
		TextView stockTV = (TextView)findViewById(R.id.stockTextView2);
		Button backButton = (Button)findViewById(R.id.backButton);
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		stockTV.setTypeface(OSExtraBold);
		stocksListView = (ListView) findViewById(R.id.stocksListView);
		stocksListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		
		
		for (int i=0;i<ALSkidky.size();i++){
			AL.add(new ListData(ALSkidky.get(i).logo,ALSkidky.get(i).name,ALSkidky.get(i).description,ALSkidky.get(i).old_price,ALSkidky.get(i).price));
		}
		CA = new CatalogAdapter(this,AL);
		stocksListView.setAdapter(CA);
		
	}
	
	public void getCheckedItems(View v){

		String str = "";
		Intent intent = new Intent(this,NewActivity.class);
		for (int i=0;i<CA.getChItems().size();i++){
			int j = CA.getChItems().get(i);
		   str += ALSkidky.get(j).name +"\n";
		}
		intent.putExtra("title", str);
	    startActivity(intent);
	}
	
}
