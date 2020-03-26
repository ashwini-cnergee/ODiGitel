package com.cnergee.mypage.sys;



import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;

import android.widget.Toast;

import com.cnergee.mypage.ChatActivity;
import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.SOAP.GetServerMessageSOAP;
import com.cnergee.mypage.SOAP.UpdateNotificationWS;
import com.cnergee.mypage.SOAP.UpdateNotifyIdSOAP;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;


public class GcmMessageHandler extends IntentService  {

     String mes;
     private Handler handler;
     DatabaseAdapter dbAdapter;
     ArrayList<String> alNotifyId;
     String Profile_Id="";
 	public static int messageCount=0;
 	SharedPreferences sharedPreferences_;
 	Utils utils;
 	String NotificationId;
 	 public boolean is_show=false;
 	ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
 	Random rand = new Random();
	public GcmMessageHandler() {
		super("GcmMessageHandler");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}
	@Override
	protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String sharedPreferences_name = getString(R.string.shared_preferences_name);
		sharedPreferences_ = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils= new Utils();
		utils.setSharedPreferences(sharedPreferences_);
        dbAdapter= new DatabaseAdapter(this);
        Profile_Id=(utils.getMemberId());
		alNotifyId= new ArrayList<String>();
		/*Profile_Id means ClientId */
		 messageCount=0;
       /* GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);*/

       mes = extras.getString("message");
     //  showToast();
       Utils.log("GCM", "Received :"+mes);

        GcmBroadcastReceiver.completeWakefulIntent(intent);
       if(mes!=null){
    	   parseMessageJson(mes);
       }

	}
	
	public void showToast(){
		handler.post(new Runnable() {
		    public void run() {
		    	((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(800);
		        Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
		    }
		 });

	}
	/*
	 * @author Sandip
	 * Method to parse json Response from push Message
	 * 
	 */
	public void parseMessageJson(String json){
		JSONObject mainJson;
		try {
			mainJson = new JSONObject(json);
			JSONObject newDatasetJson = mainJson.getJSONObject("NewDataSet");
			if (newDatasetJson.has("Table")) {
				dbAdapter.open();
				if (newDatasetJson.get("Table") instanceof JSONObject) {
					JSONObject TableJson = newDatasetJson.getJSONObject("Table");
					
					if(TableJson.optString("Push_Type").equalsIgnoreCase("Notification")){
						push_notification(this, newDatasetJson);
					}
					else{
					if(TableJson.optBoolean("ChatResponse", false)){

						Intent intentBroadcast= new Intent("chat_online");
						LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
						
						String sharedPreferences_name = this.getString(R.string.shared_preferences_name);
						SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
						
						SharedPreferences.Editor editor= sharedPreferences.edit();
						editor.putBoolean("chat_status", true);
						editor.putBoolean("chat_response", true);
						editor.putBoolean("wait_status", true);
						editor.commit();
						
						
						String message=TableJson.optString("Message", "please wait..");
						
						String type=	"";//tableObj.getPropertyAsString("MessageType").toString();
						if(TableJson.optString("MessageType", "0").equalsIgnoreCase("0")){
							type=	"Text";
						}
						if(TableJson.optString("MessageType", "0").equalsIgnoreCase("1")){
							type=	"Image";
						}						
						String date=TableJson.optString("CreatedDate", "00-00-00");
						String time=	GetServerMessageSOAP.Convert24to12(TableJson.optString("CreatedTime", "00-00 AM"));
						String source=	"server";
						String path=	"no path"; //tableObj.getPropertyAsString("MessagePath").toString();
						String status=	"download";
						String date_change=	"false";
						String source_id="1";	//tableObj.getPropertyAsString("CreatedBy").toString();
						String sync_status=	"no";
						String url= "server";  //	tableObj.getPropertyAsString("MessageSource").toString();
						String notifyId=TableJson.optString("MessageId", "0") ;
						String LastChatWith=TableJson.optString("LastChatWith", "Admin") ;
						/*if(type.equalsIgnoreCase("Group")){
							//dbAdapter.open();
							dbAdapter.updateOrInsertGroup(url, message);
							//dbAdapter.open();
							TableConstants.MESSAGE_COUNT=0;
							UpdateGroupNotifyIdSOAP updateGroup= new UpdateGroupNotifyIdSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, "UpdateNotification");
							String updateGroup1=updateGroup.updateGroupNotifyId(Profile_Id, notifyId);
							Utils.log("Response for Group Update",":"+updateGroup1);
						}*/

						/* When a user is assigned to a group ,Group Id is updated ***ends here***** */
						
						Utils.log("Admin Name",":"+LastChatWith);
						alNotifyId.add(TableJson.optString("MessageId", "0"));
						
						if(!type.equalsIgnoreCase("Group")){
						if(dbAdapter.getNotifyCount(notifyId)==0){
							dbAdapter.insertMessgae(message, type, date, time, source, path, status, date_change, source_id, sync_status, url,notifyId,"uploaded",LastChatWith);
							messageCount=messageCount+1;
						}
					}
						
						if(messageCount>0){
							if(TableConstants.CHECK_APP_OPEN){
								//iGetMessgeFromAsync.getMessage(chatAct);
								Utils.log("App","open"+messageCount);
								 Intent intent = new Intent("speedExceeded");					   
								 LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(intent);
								 
								 Intent intent1 = new Intent("chat_online");					   
								 LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(intent1);
								 
								/* Intent intent2 = new Intent("speedExceeded");					   
								 LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(intent2);*/
								
										/*Intent i1 = new Intent("chat_end");
										intent.putExtra("state", true);
										LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(i1);*/
									
								}
							
							else{
								//show Notififcation
								Utils.log("App","close: "+messageCount);
								String msg="";
								if(messageCount>0){
									if(messageCount>1)
									msg=" Messages from server";
									else
									msg=" Message from server";
									
									
									showNotification(messageCount+ msg);
									
									myHandler.dispatchMessage(new Message());
									 

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
					}
				}
				

				else if (newDatasetJson.get("Table") instanceof JSONArray) {
					JSONArray TableJsonArray = newDatasetJson.getJSONArray("Table");
					for (int i = 0; i < TableJsonArray.length(); i++) {
						JSONObject PushJsonObject = TableJsonArray.getJSONObject(i);
						
						if(PushJsonObject.optString("Push_Type").equalsIgnoreCase("notification")){
							push_notification(this, newDatasetJson);
						}
						else{
						if(PushJsonObject.optBoolean("ChatResponse", false)){
							
							Intent intentBroadcast= new Intent("chat_online");
							LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
							
							String sharedPreferences_name = this.getString(R.string.shared_preferences_name);
							SharedPreferences sharedPreferences = this.getApplicationContext()
									.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
							
							SharedPreferences.Editor editor= sharedPreferences.edit();
							editor.putBoolean("chat_status", true);
							editor.putBoolean("chat_response", true);
							editor.putBoolean("wait_status", true);
							editor.commit();
							
							String message=PushJsonObject.optString("Message", "please wait..");
							
							String type=	"";//tableObj.getPropertyAsString("MessageType").toString();
							if(PushJsonObject.optString("MessageType", "0").equalsIgnoreCase("0")){
								type=	"Text";
							}
							if(PushJsonObject.optString("MessageType", "0").equalsIgnoreCase("")){
								type=	"Image";
							}						
							String date=PushJsonObject.optString("CreatedDate", "00-00-00");
							String time=	GetServerMessageSOAP.Convert24to12(PushJsonObject.optString("CreatedTime", "00-00 AM"));
							String source=	"server";
							String path=	"no path"; //tableObj.getPropertyAsString("MessagePath").toString();
							String status=	"download";
							String date_change=	"false";
							String source_id="1";	//tableObj.getPropertyAsString("CreatedBy").toString();
							String sync_status=	"no";
							String url= "server";  //	tableObj.getPropertyAsString("MessageSource").toString();
							String notifyId=PushJsonObject.optString("MessageId", "0") ;
							String LastChatWith=PushJsonObject.optString("LastChatWith", "Admin") ;
							/*if(type.equalsIgnoreCase("Group")){
							//dbAdapter.open();
							dbAdapter.updateOrInsertGroup(url, message);
							//dbAdapter.open();
							TableConstants.MESSAGE_COUNT=0;
							UpdateGroupNotifyIdSOAP updateGroup= new UpdateGroupNotifyIdSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, "UpdateNotification");
							String updateGroup1=updateGroup.updateGroupNotifyId(Profile_Id, notifyId);
							Utils.log("Response for Group Update",":"+updateGroup1);
						}*/
						
						/* When a user is assigned to a group ,Group Id is updated ***ends here***** */
						
							Utils.log("Admin Name",":"+LastChatWith);
						alNotifyId.add(PushJsonObject.optString("MessageId", "0"));
						
						if(!type.equalsIgnoreCase("Group")){
						if(dbAdapter.getNotifyCount(notifyId)==0){
							dbAdapter.insertMessgae(message, type, date, time, source, path, status, date_change, source_id, sync_status, url,notifyId,"uploaded",LastChatWith);
							messageCount=messageCount+1;
						}
						
						if(messageCount>0){
							if(TableConstants.CHECK_APP_OPEN){
								//iGetMessgeFromAsync.getMessage(chatAct);
								Utils.log("App","open"+messageCount);
								 Intent intent = new Intent("speedExceeded");					   
								 LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(intent);
								 
								
										/*Intent i1 = new Intent("chat_end");
										intent.putExtra("state", true);
										LocalBroadcastManager.getInstance(GcmMessageHandler.this).sendBroadcast(i1);*/
									
								}
							
							else{
								//show Notififcation
								Utils.log("App","close: "+messageCount);
								String msg="";
								if(messageCount>0){
									if(messageCount>1)
									msg=" Messages from server";
									else
									msg=" Message from server";
									
									showNotification(messageCount+ msg);
									myHandler.dispatchMessage(new Message());
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
						}
					}
					}
				}
				dbAdapter.close();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(dbAdapter!=null)
			dbAdapter.close();
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
					rslt=getServerMessageSOAP.updateNotifyId(Profile_Id,alNotifyId, GcmMessageHandler.this);
				
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
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void showNotification(String Message) {
		int notifID = 1;

		Intent notificateionIntent = new Intent(GcmMessageHandler.this,
				ChatActivity.class);
		notificateionIntent.putExtra("callby", "notification");
		notificateionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	

		
		final Object systemService = GcmMessageHandler.this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationManager notificationManager = (NotificationManager) systemService;

		PendingIntent contentIntent = PendingIntent.getActivity(GcmMessageHandler.this, 1,
				notificateionIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		if (Build.VERSION.SDK_INT > 15) {

			// Log.i("--------------------NotifyId-----------------",
			// NotifyObj.getNotifyId());

			Notification notification = new Notification.Builder(GcmMessageHandler.this)
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

			//notification.setLatestEventInfo(GcmMessageHandler.this, "ODiGitel Broadband Notification ", Message,
			//		contentIntent);
			notificationManager.notify(notifID, notification);

		}
	}


	
	private final Handler myHandler = new Handler() {
	    public void handleMessage(Message msg) {
	    	
	    	/*ImageView iconView = new ImageView(GcmMessageHandler.this);
		       iconView.setImageResource(R.drawable.ic_launcher);
			
			magnet = new Magnet.Builder(GcmMessageHandler.this)
	        .setIconView(iconView) // required
	        .setIconCallback(GcmMessageHandler.this)
	        .setRemoveIconResId(R.drawable.trash)
	        .setRemoveIconShadow(R.drawable.bottom_shadow)
	        .setShouldFlingAway(true)
	        .setShouldStickToWall(true)
	        .setRemoveIconShouldBeResponsive(true)
	        .build();
			magnet.show();*/
	      
	        }
	    };
	

	    public void push_notification(Context ctx, JSONObject newDataSet){
	    	try{
	    	  if(newDataSet.get("Table") instanceof JSONObject){
		        	Notificationobj NotifyObj = new Notificationobj();
		        	JSONObject jsonMainNode1 = newDataSet.getJSONObject("Table");
		        	
		        	String isNotify   = jsonMainNode1.optString("IsNotify").toString();
					String menberId = jsonMainNode1.optString("MemberId").toString();
					
					String notifyId = jsonMainNode1.optString("NotifyId").toString();
					String notificationMessage = jsonMainNode1.optString("NotificationMessage").toString();
					String dataFrom=jsonMainNode1.optString("DataFrom").toString();
					String icard_id=jsonMainNode1.optString("CreatedBy").toString();
				
					if(dataFrom.equalsIgnoreCase("Profile Update")){
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_profile), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("profile", true);
						editor.commit();
						is_show=true;
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("inonhome","yes");
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_renewal), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("renewal", true);
						editor.putBoolean("profile", true);
						editor.commit();
						is_show=true;
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("payment check","yes");
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_payment_history), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("payment_history", true);
						editor.commit();
						is_show=true;
						
					}
					
						//Utils.log("notification list","yes");
					if(notifyId.length()>0){
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_notification_list), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("notification_list", true);
						editor.commit();
						is_show=true;
					
					
					NotifyObj.setMemberId(menberId);
					NotifyObj.setIsNotify(isNotify);
					NotifyObj.setNotifyId(notifyId);
					NotifyObj.setDataFrom(dataFrom);
					NotifyObj.setIcard_id(icard_id);
					
					Utils.log("createdBy","id:"+icard_id);
					
					NotificationId = notifyId;
					NotifyObj.setNotificationMessage(notificationMessage);
					Utils.log("Get NotifyId",":"+notifyId);
					notificationList.add(NotifyObj);
					}
		        	
		        }
	    	  
	    	  if(newDataSet.get("Table") instanceof JSONArray)	{
					JSONArray jsonMainNode1 = newDataSet.optJSONArray("Table");
				
					int lengthJsonArr1 = jsonMainNode1.length();  
					
					for(int a=0; a < lengthJsonArr1; a++) 
					{
						Notificationobj NotifyObj = new Notificationobj();
						JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(a);
						is_show=true;	
					/******* Fetch node values **********/
					String isNotify   = jsonChildNode1.optString("IsNotify").toString();
					String menberId = jsonChildNode1.optString("MemberId").toString();
					
					String notifyId = jsonChildNode1.optString("NotifyId").toString();
					String notificationMessage = jsonChildNode1.optString("NotificationMessage").toString();
					String dataFrom=jsonChildNode1.optString("DataFrom").toString();
					String icard_id=jsonChildNode1.optString("CreatedBy").toString();
					
					if(dataFrom.equalsIgnoreCase("Profile Update")){
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_profile), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("profile", true);
						editor.commit();
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("inonhome","yes");
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_renewal), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("renewal", true);
						editor.putBoolean("profile", true);
						editor.commit();
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("payment check","yes");
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_payment_history), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("payment_history", true);
						editor.commit();
						
					}
					
						//Utils.log("notification list","yes");
						SharedPreferences sharedPreferences1 = ctx
								.getSharedPreferences(ctx.getString(R.string.shared_preferences_notification_list), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("notification_list", true);
						editor.commit();
						Utils.log("I Card ","id:"+icard_id);
					
					NotifyObj.setMemberId(menberId);
					NotifyObj.setIsNotify(isNotify);
					NotifyObj.setNotifyId(notifyId);
					NotifyObj.setDataFrom(dataFrom);
					NotifyObj.setIcard_id(icard_id);
					
					Utils.log("createdBy","id:"+icard_id);
					
					NotificationId = notifyId;
					NotifyObj.setNotificationMessage(notificationMessage);
					Utils.log("Get NotifyId",":"+notifyId);
					notificationList.add(NotifyObj);
				}
		}
	    	  
	    	  if(is_show){
	    	         showNotification();
	    	         Utils.log("Show Notification",":"+is_show);
	    			 }
	    			 else{
	    				 Utils.log("Show Notification",":"+is_show);	 
	    			 }
	    	}
	    	catch(JSONException e){
	    		
	    	}
	    }
	    
	    
	    private void showNotification(){
			 int notifID = rand.nextInt(100);
			
			 String notifyId="";
			 String memberId="";
			Intent notificateionIntent=new Intent(this,NotificationListActivity.class);
			notificateionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putString("NotifyId",utils.getMemberId());
			PendingIntent detailsIntent = PendingIntent.getActivity(this, 1, notificateionIntent, notifID);
				
			final Object systemService = this.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationManager notificationManager = (NotificationManager) systemService;
				
		        
			PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificateionIntent, notifID);
			
			if(Build.VERSION.SDK_INT > 15){
				
			//	Iterator iter = notificationList.iterator();
				
				//while(iter.hasNext()){
					for(int i=0;i<notificationList.size();i++){
					Notificationobj	NotifyObj	= notificationList.get(i);
					//Log.i("--------------------NotifyId-----------------", NotifyObj.getNotifyId());
					if(NotifyObj.getIsNotify()!=null){
						if(NotifyObj.getIsNotify().length()>0){
					Notification notification = new Notification.Builder(this)
				     .setContentTitle("ODiGitel Broadband Notification")
				     .setContentText(NotifyObj.getNotificationMessage())
				     .setSmallIcon(R.drawable.ic_launcher)
				     .setAutoCancel(true)
				     .setVibrate( new long[] { 0, 100, 200, 300 })
				     .setContentIntent(contentIntent)
				     .build();
					notification.defaults |= Notification.DEFAULT_SOUND;
				    // Hide the notification after its selected
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
				    notificationManager.notify(Integer.parseInt(NotifyObj.getNotifyId()), notification);
				    
				    StrictMode.ThreadPolicy policy = new
				    		 StrictMode.ThreadPolicy.Builder().permitAll().build();
				    		        StrictMode.setThreadPolicy(policy);
				    		        Utils.log("Update Notify Id",":"+NotifyObj.getNotifyId());
				    //updateStatus(NotifyObj.getNotifyId());
				    		        if(notifyId.length()>0){
				    		        	notifyId+=","+NotifyObj.getNotifyId();
				    		        }
				    		        else{
				    		        	notifyId=NotifyObj.getNotifyId();
				    		        }
				    memberId=	NotifyObj.getMemberId(); 
				    updteNotification(memberId,notifyId);
					}
				//}
						
					}
					}
			}else{
				for(int i=0;i<notificationList.size();i++){
					Notificationobj	NotifyObj	= notificationList.get(i);
					if(NotifyObj.getIsNotify()!=null){
						if(NotifyObj.getIsNotify().length()>0){
						
						//Log.i("--------------------NotifyId-----------------", NotifyObj.getNotifyId());
						
						Notification notification = new Notification(R.drawable.ic_launcher, "ODiGitel Broadband Notification",System.currentTimeMillis());
						notification.defaults |= Notification.DEFAULT_SOUND;
						notification.vibrate = new long[] { 0, 100, 200, 300 };
						  //Clear the status notification when the user selects it
						notification.flags|=Notification.FLAG_AUTO_CANCEL;  
					
					//	notification.setLatestEventInfo(this, "ODiGitel Broadband Notification ", NotifyObj.getNotificationMessage(), contentIntent);
						notificationManager.notify(Integer.parseInt(NotifyObj.getNotifyId()), notification);
						
						  Utils.log("Update Notify Id",":"+NotifyObj.getNotifyId());
						 if(notifyId.length()>0){
		    		        	notifyId+=","+NotifyObj.getNotifyId();
		    		        }
		    		        else{
		    		        	notifyId=NotifyObj.getNotifyId();
		    		        }
						 memberId=	NotifyObj.getMemberId();   
						 updteNotification(memberId,notifyId);
					}
					
					
					}
				}
			}
		}
	    
	    private void updteNotification(String memberId, String notifyId){
			UpdateNotificationWS update = new UpdateNotificationWS(this,memberId,notifyId);
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 update.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			 else	 
				 update.execute();
		}
}
