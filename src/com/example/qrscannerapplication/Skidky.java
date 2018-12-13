package com.example.qrscannerapplication;

import java.io.Serializable;

public class Skidky implements Serializable{
String logo,price,old_price,description,name;
public Skidky(String logo,String price,String old_price,String description,String name){
	this.logo = logo;
	this.price = price;
	this.old_price = old_price;
	this.description = description;
	this.name = name;
}
}
