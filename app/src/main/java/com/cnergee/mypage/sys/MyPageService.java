package com.cnergee.mypage.sys;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import com.cnergee.mypage.SOAP.NotificationAsyncWS;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;

public class MyPageService extends Service{
	
	public static Context context;
	private static final String TAG = "Mypage";  
    public static String rslt = "";
    public static ArrayList<Notificationobj> notificationlist = new ArrayList<Notificationobj>();
    Utils utils = new Utils();
    private String sharedPreferences_name;
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public void onCreate() { 
        super.onCreate();
       // Toast.makeText(this, "Mypage Service Created.)", Toast.LENGTH_LONG).show();
        //Utils.log(TAG, TAG + ": Service Created");
        context = this;
        
        sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Utils.log(TAG, TAG + ": Mypage Service Destroyed");
       // utils = null;
       // complaintList = null;
       // Authobj = null;
    }
   /* @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Toast.makeText(this, "ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Utils.log("Testing", "Service got started");
    }
*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
      //  initDoSomethingTimer();             
        
      		
      //  Utils.log(TAG, TAG + ": Cnergee Service Started "+utils.getMemberId());
        /*Utils.log(TAG, TAG + ": IS NET CONNECT  "+utils.isOnline(context));*/
        
        if(Utils.isOnline(context)){
	      //  complaintListWebService.execute((String)null);
        	NotificationAsyncWS nws = new NotificationAsyncWS(context, Build.VERSION.SDK_INT,utils.getMemberId());
        	nws.setMETHOD_NAME(getApplicationContext()
					.getResources().getString(
							R.string.METHOD_GET_MULTIPLE_NOTIFICATION));
        	nws.setSOAP_URL(getApplicationContext().getResources().getString(
					R.string.SOAP_URL));
        	nws.setWSDL_TARGET_NAMESPACE(getApplicationContext().getResources().getString(
					R.string.WSDL_TARGET_NAMESPACE));
        	 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        		 nws.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
        	 else
        		 nws.execute(); 
        	SharedPreferences sharedPreferences_ = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode

			if(sharedPreferences_.getString("Gcm_reg_id", "").length()>0){
				Utils.log("Reg id","is:"+sharedPreferences_.getString("Gcm_reg_id", ""));
			}
			else{
				//Utils.getRegId(context);
			}
        }
        stopSelf();
        	return START_STICKY;    
        	
    }


/*    private void doSomething() {
     
        Random rand = new Random();
        Iterator iter = notificationlist.iterator();
        
 		while(iter.hasNext()){
			 int notifID = rand.nextInt(100);
			 Notificationobj notifyObj1 = (Notificationobj)iter.next(); 	
			 String id = notifyObj1.getMemberId().trim();
			 //boolean isUpdated = complObj.isUpdated();
			 boolean isPush = notifyObj1.isPush();
			 
			 if(!isPush){
				 updateStatus(id);
				 
				 //Utils.log(TAG, TAG + " COMPL ID "+id);
				 
				 Intent intent = new Intent(this,NotificationListActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle bundle = new Bundle();
					bundle.putString("NotifyId", notifyObj1.getMemberId());
					PendingIntent detailsIntent = PendingIntent.getActivity(this, 1, intent, notifID);
					
			        NotificationManager notifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			        final Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
					
			        if(android.os.Build.VERSION.SDK_INT < 11){
			        	 Notification notifyObj = new Notification(
					                R.drawable.alert, 
					                getString(R.string.app_notification_label_1),
					                System.currentTimeMillis());
					        
					        CharSequence from = getString(R.string.app_notification_label_2);
					        //CharSequence message = "Complaint No."+id;        
					        notifyObj.setLatestEventInfo(this, from, notifyObj1.getNotificationMessage(), detailsIntent);
					     
					        notifyObj.defaults |= Notification.DEFAULT_VIBRATE;
					        //Set default notification sound
					        notifyObj.defaults |= Notification.DEFAULT_SOUND;
					        //Clear the status notification when the user selects it
					        notifyObj.flags|=Notification.FLAG_AUTO_CANCEL;   
					        //Send notification
					        notifyMgr.notify(notifID, notifyObj);
					     
			        }else{
			        	NotificationManager notificationManager = (NotificationManager) systemService;
						Notification notification = new Notification.Builder(context)
					     .setContentTitle(getString(R.string.app_notification_label_1))
					     .setContentText( notifyObj1.getNotificationMessage())
					     .setSmallIcon(R.drawable.alert)
					     .setAutoCancel(true)
					     .setVibrate( new long[] { 0, 100, 200, 300 })
					     .setContentIntent(detailsIntent)
					     .build();
						notification.defaults |= Notification.DEFAULT_SOUND;
					    // Hide the notification after its selected
						notification.flags |= Notification.FLAG_AUTO_CANCEL;
					    notificationManager.notify(notifID, notification);
			        }
		        //---100ms delay, vibrate for 250ms, pause for 100 ms and
		        // then vibrate for 500ms---
		        notif.vibrate = new long[] { 100, 250, 100, 500};        
		        nm.notify(notifID, notif);
			 }
		       
		}
    }*/
    
/*private void updateStatus(String NotifyId){
    	
    	//Log.i("*********** STATUS CHANGE... ",""+complID);
    	
    	
    	try {
    		NotificationStatusUpdateSOAP soap = new NotificationStatusUpdateSOAP(getApplicationContext().getResources().getString(
    				R.string.WSDL_TARGET_NAMESPACE), getApplicationContext().getResources().getString(
    						R.string.SOAP_URL),getApplicationContext()
    						.getResources().getString(
    								R.string.METHOD_UPDATE_NOTIFICATION));
        	
        	StatusUpdateHelper helper = new StatusUpdateHelper(soap,utils.getMemberId(),NotifyId,true);
        	helper.updateStatus();
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }*/
    
  
}
