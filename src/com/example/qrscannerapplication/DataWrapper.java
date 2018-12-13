package com.example.qrscannerapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

   private ArrayList<Skidky> skidky;

   public DataWrapper(ArrayList<Skidky> data) {
      this.skidky = data;
   }

   public ArrayList<Skidky> getSkidky() {
      return this.skidky;
   }

}