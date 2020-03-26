package com.cnergee.mypage.sys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Utils.log("Broadcast ","called");
		 Intent myIntent = new Intent(context,MyPageService.class);
		 context.startService(myIntent);
	 }
 }
