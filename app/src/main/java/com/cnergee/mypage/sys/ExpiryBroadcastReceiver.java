package com.cnergee.mypage.sys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ExpiryBroadcastReceiver extends BroadcastReceiver {
Context ctx;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Utils.log("Broadcast ","called");
		 Intent myIntent = new Intent(context,CheckExpiryService.class);
		 context.startService(myIntent);
		 ctx=context;
		 
		 Intent intentService1 = new Intent(context,ExpiryBroadcastReceiver.class);
		
	//	SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

			PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0, intentService1, 0);
			AlarmManager alarm2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			
			int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
			Calendar calendar1 = new GregorianCalendar();
			calendar1.set(Calendar.HOUR_OF_DAY, 8);
			calendar1.set(Calendar.MINUTE, 05);
			calendar1.set(Calendar.SECOND, 0);
			calendar1.set(Calendar.MILLISECOND, 0);

		    long triggerMillis = calendar1.getTimeInMillis();

		   
		    if (calendar1.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
		        triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
		        //System.out.println("Alarm will go off next day");
		    }
		    
		    	alarm2.cancel(pintent2);
				
		  alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis, AlarmManager.INTERVAL_DAY, pintent2);
	}
}
