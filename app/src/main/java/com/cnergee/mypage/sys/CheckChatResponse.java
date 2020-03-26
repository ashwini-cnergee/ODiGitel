package com.cnergee.mypage.sys;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;


import com.cnergee.mypage.SOAP.GetChatResponseSOAP;
import com.cnergee.mypage.utils.Utils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;

public class CheckChatResponse  extends Service{
	private String sharedPreferences_name;
	 Utils utils = new Utils();
	 String Proile_Id="";
	 Context ctx;
	 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		 
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		Proile_Id=utils.getMemberId();
		ctx=this;
		
		new CheckChatAsyncTask().execute();
		stopSelf();
		return START_STICKY;
	}
	
	public class CheckChatAsyncTask extends AsyncTask<String, Void, Void>{
		GetChatResponseSOAP chatResponseSOAP;
		String checkResult="",checkResponse="";
		Boolean chat_status,chat_response,wait_status;
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Utils.log("Background Service","running");
			chatResponseSOAP=new GetChatResponseSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_SERVER_MESSAGE));
			checkResult=chatResponseSOAP.getServerMessage(Proile_Id, CheckChatResponse.this);
			checkResponse=chatResponseSOAP.getResponse();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = ctx.getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			SharedPreferences.Editor editor= sharedPreferences.edit();
			if(checkResult.length()>0){
				if(checkResult.equalsIgnoreCase("OK")){
					if(checkResponse!=null){
					if(checkResponse.length()>0){
						if(checkResponse.equalsIgnoreCase("1")){
						
							
							
							
							chat_status=chatResponseSOAP.getChat_status();
							chat_response=chatResponseSOAP.getChat_response();
							wait_status=chatResponseSOAP.getWait_status();
							
							if(chat_status){
								editor.putBoolean("chat_status", true);
								editor.putBoolean("chat_response", true);
								editor.putBoolean("wait_status", true);
								editor.commit();
							}
							else{
								if(wait_status){
									editor.putBoolean("chat_status", false);
									editor.putBoolean("chat_response", true);
									editor.putBoolean("wait_status", true);
									editor.commit();
								}
								else{
									editor.putBoolean("chat_status", false);
									editor.putBoolean("chat_response", false);
									editor.putBoolean("wait_status", false);
									editor.commit();
									Intent intent1 = new Intent("chat_end");					   
									LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent1);
									Intent intentSetRepeatAlarm = new Intent(CheckChatResponse.this,AlarmBroadcastReceiver1.class);
									
									PendingIntent.getBroadcast(CheckChatResponse.this, 0, intentSetRepeatAlarm, 0).cancel();
								}
							}
							
							/*if(TableConstants.CHECK_APP_OPEN){
								Intent intent = new Intent("chat_end");
								intent.putExtra("state", true);
								LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
							}
							*/
							
						}
						/*else{
							//AlarmManager alarmManager = (AlarmManager)(CheckChatResponse.this.getSystemService( Context.ALARM_SERVICE ));
							Intent intentSetRepeatAlarm = new Intent(CheckChatResponse.this,AlarmBroadcastReceiver1.class);
							
							PendingIntent.getBroadcast(CheckChatResponse.this, 0, intentSetRepeatAlarm, 0).cancel();
						
							String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
							SharedPreferences sharedPreferences = ctx.getApplicationContext()
									.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
							if(TableConstants.CHECK_APP_OPEN){
								Intent intent = new Intent("chat_end");
								intent.putExtra("state", false);
								LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
							}
							SharedPreferences.Editor editor= sharedPreferences.edit();
							if(sharedPreferences.getBoolean("wait_status", false)){
								
							}
							else{
								editor.putBoolean("chat_status", false);
								editor.putBoolean("wait_status", true);
							}
							
							editor.commit();
						}*/
					}
					}
					else{
						editor.putBoolean("chat_status", false);
						editor.putBoolean("chat_response", false);
						editor.putBoolean("wait_status", false);
						editor.commit();
						Intent intent1 = new Intent("chat_end");					   
						LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent1);
						Intent intentSetRepeatAlarm = new Intent(CheckChatResponse.this,AlarmBroadcastReceiver1.class);				
						PendingIntent.getBroadcast(CheckChatResponse.this, 0, intentSetRepeatAlarm, 0).cancel();
					}
					
				}
				else{
					editor.putBoolean("chat_status", false);
					editor.putBoolean("chat_response", false);
					editor.putBoolean("wait_status", false);
					editor.commit();
					Intent intent1 = new Intent("chat_end");					   
					LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent1);
					Intent intentSetRepeatAlarm = new Intent(CheckChatResponse.this,AlarmBroadcastReceiver1.class);				
					PendingIntent.getBroadcast(CheckChatResponse.this, 0, intentSetRepeatAlarm, 0).cancel();
				}
			}
			else{
				editor.putBoolean("chat_status", false);
				editor.putBoolean("chat_response", false);
				editor.putBoolean("wait_status", false);
				editor.commit();
				Intent intent1 = new Intent("chat_end");					   
				LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent1);
				Intent intentSetRepeatAlarm = new Intent(CheckChatResponse.this,AlarmBroadcastReceiver1.class);				
				PendingIntent.getBroadcast(CheckChatResponse.this, 0, intentSetRepeatAlarm, 0).cancel();
			}
		}
		
	}
}
