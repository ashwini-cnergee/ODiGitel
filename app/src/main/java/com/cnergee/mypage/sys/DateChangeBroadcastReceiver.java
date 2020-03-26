package com.cnergee.mypage.sys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cnergee.mypage.utils.Utils;

public class DateChangeBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Utils.log("Broadcast","date");
		Intent startDateChangeService= new Intent(context,DateChangeService.class);
		context.startService(startDateChangeService);
	}

}
