package com.example.qrscannerapplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
public class CatalogAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<ListData> objects;
	Typeface OSSemibold,OSLight,OSExtraBold,OSBold;
	HashMap<Integer,Boolean> checkedItems;
	CatalogAdapter(Context context, ArrayList<ListData> mylist) {
        this.context = context;
      objects = mylist;
      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	  checkedItems = new HashMap<Integer,Boolean>();
	  
      for (int i = 0; i < objects.size(); i++) {
			checkedItems.put(i, false);
		}
      
		 OSSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
		 OSLight = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
		 OSExtraBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
		 OSBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

	}
	
	
	@Override
	public int getCount() {
		return objects.size();
	}
	public void toogleChecked(int position){
		
		if (checkedItems.get(position)){
			checkedItems.put(position, false);
		}else{
			checkedItems.put(position,true);
		}
	}
	public List<Integer> getChItems(){
		List<Integer> checkedItemPositions = new ArrayList<Integer>();
		for (int i =0;i<checkedItems.size();i++){
			if (checkedItems.get(i)){
				checkedItemPositions.add(i);
			}
		}
		return checkedItemPositions;
	}
	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null){
			view = inflater.inflate(R.layout.list_view_item_row,parent,false);
		}
		
		ListData p = ((ListData) getItem(position));

		
		 TextView titleTV = (TextView) view.findViewById(R.id.titleTextView);
		 TextView discountTV = (TextView) view.findViewById(R.id.discountTextView);
		 TextView oldPriceTV = (TextView) view.findViewById(R.id.oldPriceTextView);
		 TextView newPriceTV = (TextView) view.findViewById(R.id.newPriceTextView);
		 CheckBox CB = (CheckBox)view.findViewById(R.id.checkBox);
		 
		 CB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					     toogleChecked(position);
			}});
		 titleTV.setText(p.title);
		 discountTV.setText(p.discount);
		 oldPriceTV.setText(p.oldPrice);
		 newPriceTV.setText(p.newPrice);
		 
		 titleTV.setTypeface(OSBold);
		 discountTV.setTypeface(OSLight);
		 oldPriceTV.setTypeface(OSExtraBold);
		 newPriceTV.setTypeface(OSExtraBold);
		 
		 oldPriceTV.setPaintFlags(oldPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		 new DownloadImageTask((ImageView) view.findViewById(R.id.imgView01))
	        .execute(p.img);
		 
	        return view;
	        
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
	
}
