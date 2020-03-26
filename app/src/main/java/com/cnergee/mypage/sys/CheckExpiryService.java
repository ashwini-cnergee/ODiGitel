package com.cnergee.mypage.sys;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings.Secure;

import com.cnergee.mypage.MainActivity;
import com.cnergee.mypage.SOAP.UpdatePhoneDetailSOAP;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cnergee.krishna.ookla.odigitel.R;

public class CheckExpiryService extends Service {
Context context;
String AppVersion,MemberId;
Utils utils= new Utils();
String reg_id="";
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	  public int onStartCommand(Intent intent, int flags, int startId) {
	        super.onStartCommand(intent, flags, startId);
	        context=this;
	    	Utils.log("CheckExpiry","starts");
	    	String sharedPreferences_name = context.getString(R.string.shared_preferences_name);
	    	SharedPreferences sharedPreferences = context
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
			PackageInfo pInfo;
			try {
				pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				AppVersion= pInfo.versionName;				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
										
			utils.setSharedPreferences(sharedPreferences);
			MemberId = utils.getMemberId();
			
		 	if(Utils.checkPlayServices(context)){
		   		
			   	 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			   		new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
			   	 else
			   		new GetGCMIDAsyncTask().execute((String)null);
			  
			   	}
			
	        checkExpiry();
	        
	     //daysRemaianing();
	        stopSelf();
	        return START_STICKY;    
	  }
	  
	  @SuppressLint("SimpleDateFormat")
	public boolean checkExpiry(){
		  int expDay,expMonth,expYear;
		  int todDay,todMonth,todYear;
		  Calendar cal=Calendar.getInstance();
		  todDay=cal.get(Calendar.DAY_OF_MONTH);
		  todMonth=cal.get(Calendar.MONTH)+1;
		  todYear=cal.get(Calendar.YEAR);
		  
		  	SharedPreferences sharedPreferences2 = context
					.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0);
			SharedPreferences.Editor editor = sharedPreferences2.edit();
			editor.putBoolean("check_expiry", false);
			editor.commit();
		  
		  SharedPreferences sharedPreferences1 = context
					.getSharedPreferences(context.getString(R.string.shared_preferences_renewal), 0);	
		  String expDate=sharedPreferences1.getString("ExpiryDate", "0");
		Utils.log("checking date","now");
		if(expDate!="0"){
		String arrDate[]=expDate.split("-");
		
			/*Utils.log("Day",""+arrDate[0]);
			Utils.log("Month",""+arrDate[1]);
			Utils.log("Year",""+arrDate[2]);*/
			 
			expDay=Integer.valueOf(arrDate[0]);
			expMonth=Integer.valueOf(arrDate[1]);
			expYear=Integer.valueOf(arrDate[2]);
			
			/*Utils.log("ToDay",""+todDay);
			Utils.log("ToMonth",""+todMonth);
			Utils.log("ToYear",""+todYear);
			
			Utils.log("ExpiryDay",""+expDay);
			Utils.log("ExpiryMonth",""+expMonth);
			Utils.log("ExpiryYear",""+expYear);*/
			/*if(expYear==todYear){
				Utils.log("ExpiryYear","ok "+expYear);
				if(expMonth==todMonth || expMonth-1==todMonth){
					
					Utils.log("ExpiryMonth","ok "+expMonth);
					if(expDay==todDay){
						Utils.log("Date check","ok today"+expDay);
						showNotification("Your Package is going to expire today.");
						//return true;
					}
					if(expDay-10==todDay){
						Utils.log("Date check","ok 10 "+expDay);
						showNotification("Your Package is going to expire in 10 days.");
						//return true;
					}
					if(expDay-5==todDay){
						Utils.log("Date check","ok 5 "+expDay);
						showNotification("Your Package is going to expire in 5 days.");
						//return true;
					}
				}
			}*/
			
			
			
			SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
		    Date d = null;
		    Date d1 = null;
		    Calendar cal1 = Calendar.getInstance();
		    try {
		            d = dfDate.parse(expDay+"/"+expMonth+"/"+expYear);
		            Utils.log("expiry date","is:"+d);
		           
		            d1 = dfDate.parse(dfDate.format(cal1.getTime()));//Returns 15/10/2012
		            Utils.log("todays date","is:"+d1);
		        } catch (java.text.ParseException e) {
		            e.printStackTrace();
		        }

		    int diffInDays = (int) ((d.getTime() - d1.getTime())/ (1000 * 60 * 60 * 24));
		    Utils.log("difference in days","is:"+(diffInDays));
		    //System.out.println(diffInDays);
		    if(diffInDays==10){
		    	showNotification("Your Package is going to expire in 10 days.");
		    }
		    if(diffInDays==5){
		    	showNotification("Your Package is going to expire in 5 days.");
		    }
		    if(diffInDays==0){
		    	showNotification("Your Package is going to expire today.");
		    }
		
		}
			return false;
	  }
	  //Days Remainig
	  
	  
	  
/*	  
	 public boolean daysRemaianing(){
		
		  int expDay,expMonth,expYear;
		 int todDay,todMonth,todYear;
	
		 Calendar cal=Calendar.getInstance();
		  todDay=cal.get(Calendar.DAY_OF_MONTH);
		  todMonth=cal.get(Calendar.MONTH)+1;
		  todYear=cal.get(Calendar.YEAR);
		  
		  
		  
		  
	    
	  	SharedPreferences sharedPreferences2 = context
				.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0);
		SharedPreferences.Editor editor = sharedPreferences2.edit();
		editor.putBoolean("days_remaining", false);
		editor.commit();
		
		
		
	  
	  SharedPreferences sharedPreferences1 = context
				.getSharedPreferences(context.getString(R.string.shared_preferences_renewal), 0);	
	  String daysremaining =sharedPreferences1.getString("DaysRemaining", "0");
	Utils.log("checking date","now");
	if(daysremaining!="0"){	
	String arrDate[]=daysremaining.split("-");
	 
	expDay=Integer.valueOf(arrDate[0]);
	expMonth=Integer.valueOf(arrDate[1]);
	expYear=Integer.valueOf(arrDate[2]);
	
	

	SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date d = null;
    java.util.Date d1 = null;
    Calendar cal1 = Calendar.getInstance();
    try {
            d = dfDate.parse(expDay+"/"+expMonth+"/"+expYear);
            Utils.log("expiry date","is:"+d);
           
            d1 = dfDate.parse(dfDate.format(cal1.getTime()));//Returns 15/10/2012
            Utils.log("todays date","is:"+d1);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    int diffInDays = (int) ((d.getTime() - d1.getTime())/ (1000 * 60 * 60 * 24));
    Utils.log("difference in days","is:"+(diffInDays));
    System.out.println(diffInDays);
    
	
	
	

	//SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy");
	//String formattedDate = sdf.format(cal.getTime());
	
	}
	return false;
	}*/
	
	@SuppressWarnings("deprecation")
	private void showNotification(String Message) {
			int notifID = 1;

			Intent notificateionIntent = new Intent(context, MainActivity.class);
			notificateionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		

			
			final Object systemService = context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationManager notificationManager = (NotificationManager) systemService;

			PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
					notificateionIntent, PendingIntent.FLAG_CANCEL_CURRENT);

			if (Build.VERSION.SDK_INT > 15) {

				// Log.i("--------------------NotifyId-----------------",
				// NotifyObj.getNotifyId());

				Notification notification = new Notification.Builder(context)
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

				//notification.setLatestEventInfo(context, "ODiGitel Broadband Notification ", Message,
				//		contentIntent);
				notificationManager.notify(notifID, notification);

			}
		}	
	
	

	 
	 public class GetGCMIDAsyncTask extends AsyncTask<String, Void, String>{
		
	   	  //GoogleCloudMessaging gcm;
	           @Override
	           protected String doInBackground(String... params) {
	               String msg = "";
	               try {
	                  /* if (gcm == null) {
	                       gcm = GoogleCloudMessaging.getInstance(context);
	                   }
	                   reg_id = gcm.register(AuthenticationMobile.PROJECT_NUMBER);*/

					   SharedPreferences	sharedPreferences_ = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
					   if(sharedPreferences_.getString("Gcm_reg_id", "").length()>0){
						   Utils.log("Reg id","is:"+sharedPreferences_.getString("Gcm_reg_id", ""));
						   reg_id=sharedPreferences_.getString("Gcm_reg_id", "");
					   }

	                   msg = "Check Expiry Service Device registered, registration ID=" + reg_id;
	                   Utils.log("GCM",  msg);
	                   
	                   Utils.log("RegID",":"+reg_id);
	                  
	               } catch (Exception ex) {
	                   msg = "error";
	                   Utils.log("GCM error",  ""+ex);
	               }
	               return msg;
	           }
	        

	           protected void onPostExecute(String msg) {
	        	   
	           	if(msg.equalsIgnoreCase("error")){
	           	/*	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
	           		reg_id="NO";
	           		
	           	}
	           	else{
	           		SharedPreferences	sharedPreferences_ = context.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0); // 0 - for private mode
	           		SharedPreferences.Editor editor=sharedPreferences_.edit();
	           		editor.putString("Gcm_reg_id", reg_id);
	           		editor.commit();
	           	}


	       	 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	       		 new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
	          else
	           	new InsertPhoneDetailsWebService().execute();
	              
	           }			
	 }
	 
	 private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

			//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
		UpdatePhoneDetailSOAP  updatePhoneDetailSOAP;

			protected void onPreExecute() {
				//Dialog.setMessage(getString(R.string.app_please_wait_label));
			}

			
			protected void onPostExecute(Void unused) {
			}

			@Override
			protected Void doInBackground(String... params) {
				try {
					updatePhoneDetailSOAP= new UpdatePhoneDetailSOAP(context.getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),
									context.getResources().getString(
									R.string.SOAP_URL), context
									.getResources().getString(R.string.METHOD_PING_SERVER));
					
					updatePhoneDetailSOAP.updateDetails(MemberId, Secure.getString(context.getContentResolver(), Secure.ANDROID_ID), reg_id,AppVersion);

				} catch (Exception e) {
//					AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
				}
				return null;
			}

			/* (non-Javadoc)
			 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
			}
		}
}
