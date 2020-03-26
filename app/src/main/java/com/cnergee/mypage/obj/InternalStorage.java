package com.cnergee.mypage.obj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cnergee.plan.calc.Days;

import android.content.Context;

public final class InternalStorage{
	 
	   private InternalStorage() {}
	 
	   public static void writeObject(Context context, String key, Object days) throws IOException {
	      FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
	      ObjectOutputStream oos = new ObjectOutputStream(fos);
	      oos.writeObject(days);
	      oos.close();
	      fos.close();
	   }
	 
	   public static Object readObject(Context context, String key) throws IOException,
	         ClassNotFoundException {
		 
	      FileInputStream fis = context.openFileInput(key);
	      ObjectInputStream ois = new ObjectInputStream(fis);
	      Object days = ois.readObject();
	      return days;
	   }
	}
