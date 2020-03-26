package com.cnergee.mypage.sys;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.utils.Utils;



public class DateChangeService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		DatabaseAdapter dbAdapter= new DatabaseAdapter(getApplicationContext());
		dbAdapter.open();
		String nowDate = "";
		Calendar now = Calendar.getInstance();
		/*SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM");
		String month_name = month_date.format(now.getTime());*/
		//nowDate=now.get(Calendar.DAY_OF_MONTH)+" "+month_name+" "+now.get(Calendar.YEAR);
		nowDate=now.get(Calendar.DAY_OF_MONTH)+"/"+(now.get(Calendar.MONTH)+1)+"/"+now.get(Calendar.YEAR);
		long i=dbAdapter.insertDateMessgae(nowDate, "true");
		if(i!=-1)
			Utils.log("date","insert success"+nowDate);
		else
			Utils.log("date","insert failure"+nowDate);
		dbAdapter.close();
		stopSelf();
		return START_STICKY;
	}

}
