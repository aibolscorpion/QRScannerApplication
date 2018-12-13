package com.example.qrscannerapplication;

public class ListData {
	String title,discount,newPrice;
	CharSequence oldPrice;
	String img;
	public ListData(String img,String title,String discount,CharSequence oldPrice,String newPrice){
		this.img = img;
		this.title = title;
		this.discount = discount;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
	}
}
