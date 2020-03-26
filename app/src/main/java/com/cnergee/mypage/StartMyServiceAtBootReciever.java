package com.cnergee.mypage;

import com.cnergee.mypage.sys.MyPageService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context,MyPageService.class);
	    context.startService(serviceIntent);
	    }
	 } 
