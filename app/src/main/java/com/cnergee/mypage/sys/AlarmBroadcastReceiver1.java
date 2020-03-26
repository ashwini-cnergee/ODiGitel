package com.cnergee.mypage.sys;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cnergee.mypage.utils.Utils;



public class AlarmBroadcastReceiver1 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Utils.log("Broadcast ","called");
		if(Utils.isOnline(context)){
		 Intent myIntent = new Intent(context,CheckChatResponse.class);
		 context.startService(myIntent);
		}
		else{
			Utils.log("No internet","connection");
		}
	}

}
