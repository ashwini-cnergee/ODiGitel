package com.cnergee.mypage.sys;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import all.interface_.IGetMessgeFromAsync;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;

import android.util.Log;

import com.cnergee.mypage.ChatActivity;
import com.cnergee.mypage.SOAP.GetServerMessageSOAP;
import com.cnergee.mypage.SOAP.UpdateNotifyIdSOAP;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.Chat;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ServerCommunicationService extends Service{
	Context ctx;
	IGetMessgeFromAsync iGetMessgeFromAsync;
	ChatActivity chatAct;
	GetServerMessageAsyncTask getServerMessageAsyncTask;
	public ArrayList<String> alNotifyId;
	public String profile_id,group_id="1";
	DatabaseAdapter dbAdapter;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		ctx=this;
		Utils.log("*****SERVICE STARTS********","here");
		Utils.log("service has started","here");
		
		/*if(getServerMessageAsyncTask!=null){
		if(getServerMessageAsyncTask.getStatus() == AsyncTask.Status.PENDING){
		    // My AsyncTask has not started yet
			Utils.log("pending ","yes");
			
		}

		else if(getServerMessageAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
		    // My AsyncTask is currently doing work in doInBackground()
			Utils.log("RUNNING ","yes");
		}

		else if(getServerMessageAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
		    // My AsyncTask is done and onPostExecute was called
			Utils.log("FINISHED ","yes");
			//getServerMessageAsyncTask.execute();
		}
		else{
			
		}
		}
		else{
			getServerMessageAsyncTask= new GetServerMessageAsyncTask();
			getServerMessageAsyncTask.execute();
			Utils.log("null ","yes");
		}*/
		if(TableConstants.START_SERVICE){
		//new GetServerMessageAsyncTask().execute();
			Utils.log("Asynctask start ","yes");
		}
		else{
			Utils.log("Asynctask start ","no");
		}
			
		stopSelf();
		return START_STICKY;
	}
	
	public class GetServerMessageAsyncTask extends AsyncTask<String, Void, Void>{
	  String rslt="";
	  int count=0;
		ArrayList<Chat> alChat;
		Utils utils;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utils.log("AsyncTask in Service ","onPreExecute GetServerMessageAsyncTask");
			//Utils.log(" onPrexecute","executed");
			
			String sharedPreferences_name = getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			utils= new Utils();
			utils.setSharedPreferences(sharedPreferences);
			dbAdapter= new DatabaseAdapter(getApplicationContext());
			dbAdapter.open();
			profile_id= (utils.getMemberId());
			String grp_id=dbAdapter.getGroupId();
			if(grp_id.length()>0){
				group_id=grp_id;
			}
			dbAdapter.close();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Utils.log("AsyncTask in Service ","doInBackground GetServerMessageAsyncTask STARTS");
			//Utils.log("doInBackground","started");
			TableConstants.START_SERVICE=false;
			GetServerMessageSOAP getServerMessageSOAP= new GetServerMessageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL),getString(R.string.METHOD_GET_SERVER_MESSAGE),ServerCommunicationService.this);
			try {
				/*
				 * getServerMessage("1",ctx);
				 * 
				 * set first parameter from profile table
				 * */
				
				/*
				 * for kishor set 2
				 * for me set 1
				*/
				rslt=getServerMessageSOAP.getServerMessage(profile_id,ctx);
				count=GetServerMessageSOAP.messageCount;
				alNotifyId=getServerMessageSOAP.getNotifyIdList();
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
			//	Utils.log("SocketTimeoutException","is: "+e);
				rslt="error";
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				//Utils.log("SocketException","is: "+e);
				rslt="error";
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//Utils.log("Exception","is: "+e);
				rslt="error";
				e.printStackTrace();
			}
			//Utils.log("AsyncTask in Service ","doInBackground GetServerMessageAsyncTask FINISHED");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Utils.log("AsyncTask in Service ","onPostExecute GetServerMessageAsyncTask FINISHED"+count);
			TableConstants.START_SERVICE=true;
			if(rslt.equalsIgnoreCase("ok")){
				//Utils.log("GetServerMessageAsyncTask onPostExecute ","result is: "+rslt);
				if(count>0){
				if(TableConstants.CHECK_APP_OPEN){
					//iGetMessgeFromAsync.getMessage(chatAct);
					//Utils.log("App","open"+count);
					 Intent intent = new Intent("speedExceeded");					   
					 LocalBroadcastManager.getInstance(ServerCommunicationService.this).sendBroadcast(intent);
					}
				
				else{
					//show Notififcation
					//Utils.log("App","close: "+count);
					String msg="";
					if(count>0){
						if(count>1)
						msg=" Messages from server";
						else
						msg=" Message from server";
						
						showNotification(count+ msg);
					}
				}
				}
				
				
				
				if(alNotifyId.size()>0){
					//Utils.log("Update NotifyId","is: "+alNotifyId.size());
					if(TableConstants.UPDATE_START){
						//Utils.log("Update Asynctask","HAS START ");
					new UpdateNotifyIdAsyncTask().execute();
					}
					else{						
						//Utils.log("Update Asynctask","NOT START ");
					}
				}
			}
			else{
				//Utils.log("GetServerMessageAsyncTask onPostExecute ","result is: "+rslt);
			}
			Utils.log("*****ENDS********","here");
			super.onPostExecute(result);
		}
		
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void showNotification(String Message) {
		int notifID = 1;

		Intent notificateionIntent = new Intent(ctx,
				ChatActivity.class);
		notificateionIntent.putExtra("callby", "notification");
		notificateionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	

		
		final Object systemService = ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationManager notificationManager = (NotificationManager) systemService;

		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 1,
				notificateionIntent, notifID);

		if (Build.VERSION.SDK_INT > 15) {

			// Log.i("--------------------NotifyId-----------------",
			// NotifyObj.getNotifyId());

			Notification notification = new Notification.Builder(ctx)
					.setContentTitle("ODiGitel Broadband Notification").setContentText(Message)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
					.setVibrate(new long[] { 0, 100, 200, 300 })
					.setContentIntent(contentIntent).build();
			notification.defaults |= Notification.DEFAULT_SOUND;
			// Hide the notification after its selected
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(notifID, notification);

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			// updateStatus(NotifyObj.getNotifyId());

		} else {

			// Log.i("--------------------NotifyId-----------------",
			// NotifyObj.getNotifyId());

			Notification notification = new Notification(R.drawable.ic_launcher,
					"ODiGitel Broadband Notification", System.currentTimeMillis());
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.vibrate = new long[] { 0, 100, 200, 300 };
			// Clear the status notification when the user selects it
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			//notification.setLatestEventInfo(ctx, "ODiGitel Broadband Notification ", Message,contentIntent);
			notificationManager.notify(notifID, notification);

		}
	}	
	
	
	public class UpdateNotifyIdAsyncTask extends AsyncTask<String, Void, Void>{
		  String rslt="";		
		  
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//Utils.log("UpdateNotifyIdAsyncTask onPrexecute","executed");
				
			}
			
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				//Utils.log("UpdateNotifyIdAsyncTask doInBackground","started");
				
				UpdateNotifyIdSOAP getServerMessageSOAP= new UpdateNotifyIdSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL),getString(R.string.METHOD_UPDATE_NOTIFY_ID));
				try {
					//Utils.log("UpdateNotifyIdAsyncTask NotifyId","is: "+alNotifyId.size());
					/*for kishor set 2
					 * for me set 1
					 */
					rslt=getServerMessageSOAP.updateNotifyId(profile_id,alNotifyId, ctx);
				
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					//Utils.log("SocketTimeoutException","is: "+e);
					rslt="error";
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					//Utils.log("SocketException","is: "+e);
					rslt="error";
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//Utils.log("Exception","is: "+e);
					rslt="error";
					e.printStackTrace();
				}
				//Utils.log("UpdateNotifyIdAsyncTask doInBackground","finished");
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				//Utils.log("onPostExecute","executed");
				if(rslt.equalsIgnoreCase("ok")){
				//	Utils.log("UpdateNotifyIdAsyncTask onPostExecute ","result is: "+rslt);															
				}
				else{
					//Utils.log("UpdateNotifyIdAsyncTask onPostExecute ","result is: "+rslt);
				}
				TableConstants.UPDATE_START=true;
				super.onPostExecute(result);
			}
			
		}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(dbAdapter!=null){
		if(dbAdapter.isOpen()){
			dbAdapter.close();
		}
		}
	}
}
