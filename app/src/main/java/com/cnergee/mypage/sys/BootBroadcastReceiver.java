package com.cnergee.mypage.sys;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cnergee.mypage.utils.Utils;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Utils.log("Boot Broadcast ","called");
		Intent intentService1 = new Intent(context,AlarmBroadcastReceiver.class);
		PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0, intentService1, 0);
		Calendar cal = Calendar.getInstance();
		AlarmManager alarm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
		// Start every 1mon..
		alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60000*2, pintent1);
		
		
		Intent intentService2 = new Intent(context,ExpiryBroadcastReceiver.class);
		
		
		PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0, intentService2, 0);
		/*Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 05);
		calendar.set(Calendar.SECOND, 00);
	
		AlarmManager alarm2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent 24*60*60*1000);
		// Start every 1mon..
	//	alarm2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pintent2);
		alarm2.cancel(pintent2);
		alarm2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent2);*/
		
		AlarmManager alarm2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
		Calendar calendar1 = new GregorianCalendar();
		calendar1.set(Calendar.HOUR_OF_DAY, 8);
		calendar1.set(Calendar.MINUTE, 05);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

	    long triggerMillis = calendar1.getTimeInMillis();

	    if (calendar1.getTimeInMillis() < Calendar.getInstance()
	            .getTimeInMillis()) {
	        triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;

	        //System.out.println("Alarm will go off next day");
	    }

	    alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
	                    AlarmManager.INTERVAL_DAY, pintent2);
	}

}
