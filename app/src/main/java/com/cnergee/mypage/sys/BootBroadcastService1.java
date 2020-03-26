package com.cnergee.mypage.sys;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cnergee.mypage.utils.Utils;


public class BootBroadcastService1 extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Utils.log("Phone has","restarted");
		
		Intent intentSetRepeatAlarm = new Intent(context,AlarmBroadcastReceiver1.class);
		
		PendingIntent penIntentRepeatAlarm =PendingIntent.getBroadcast(context, 0, intentSetRepeatAlarm, 0);
		Calendar cal = Calendar.getInstance();
		AlarmManager Repeatalarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
		// Start every 1mon..
		Repeatalarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 3*1000, penIntentRepeatAlarm);
		
		int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
		Intent intentSetOneAlarm = new Intent(context,DateChangeBroadcastReceiver.class);
		
		PendingIntent penIntentOneAlarm = PendingIntent.getBroadcast(context, 0, intentSetOneAlarm, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 00);
		 calendar.set(Calendar.SECOND, 00);
		long triggerMillis = calendar.getTimeInMillis();
		
		   if (calendar.getTimeInMillis() < Calendar.getInstance()
		            .getTimeInMillis()) {
		        triggerMillis = calendar.getTimeInMillis() + INTERVAL_DAY1;
		       // System.out.println("Alarm will go off next day");
		    }
		
		AlarmManager oneAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);24*60*60*1000
		// Start every 1mon..
		
		oneAlarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, penIntentOneAlarm);
	}

}
