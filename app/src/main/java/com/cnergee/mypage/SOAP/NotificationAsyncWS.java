
package com.cnergee.mypage.SOAP;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cnergee.krishna.ookla.odigitel.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NotificationAsyncWS extends AsyncTask<Void, Void, Void>{
	private String TAG ="NotificationAsyncWS";
	private Context context;
	//private Iterator<JSONObject> notificationItear;
	private Iterator notificationItear;
	private String MemberId;
	private int SDK_INT;
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	String NotificationId;
	String ResponseMessage,str_msg;
	 Random rand = new Random();
	 public boolean is_show=false;
		ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
		
	
	public NotificationAsyncWS(Context context,int SDK_INT,String MemberId){
		this.context = context;
		this.MemberId = MemberId;
		this.SDK_INT = SDK_INT;
	}
	@Override
	protected Void doInBackground(Void... arg0) {

		//Log.i(TAG, "doInBackground");
        try {
        	Utils utils= new Utils();
        	//if(utils.isOnline(context))
			getNotificationData();
			//updatenotification();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	
	 @Override
     protected void onPostExecute(Void result) {
        // Log.i(TAG, "onPostExecute");
		 if(is_show){
         showNotification();
         Utils.log("Show Notification",":"+is_show);
		 }
		 else{
			 Utils.log("Show Notification",":"+is_show);	 
		 }
     }

     @Override
     protected void onPreExecute() {
         //Log.i(TAG, "onPreExecute");
     }

     @Override
     protected void onProgressUpdate(Void... values) {
       //  Log.i(TAG, "onProgressUpdate");
     }
	
	@SuppressWarnings("unused")
	private void getNotificationData() throws IOException, Exception{
		String WSDL_TARGET_NAMESPACE = this.WSDL_TARGET_NAMESPACE; //"http://tempuri.org/";
		String SOAP_URL = this.SOAP_URL;//"http://114.79.129.12:8001/CCRMToMobileIntegration.asmx?wsdl";
		String METHOD_NAME = this.METHOD_NAME;//"GetNotification";
		
		//Utils.log("Notification:", " did something!!");
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		try {
			//Log.i(" #	#####################  ", " START ");
			//Log.i(TAG+" userId ", ""+MemberId);
			/*Log.i(TAG+" IMEI No ", Authobj.getIMEINo());
			Log.i(TAG+" Mobile ", Authobj.getMobileNumber());
			Log.i(TAG+" Mobile User ", Authobj.getMobLoginId());
			Log.i(TAG+" Mobile Password ", Authobj.getMobUserPass());*/
			//Log.i(TAG+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
			//Log.i(TAG+" METHOD_NAME ", METHOD_NAME);
			//Log.i(TAG+" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
			//Log.i("#####################", "");
			
			PropertyInfo pi = new PropertyInfo();
			
			pi.setName("MemberId");
			pi.setValue(MemberId);
			pi.setType(String.class);
			request.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.setName(AuthenticationMobile.CliectAccessName);
			pi.setValue(AuthenticationMobile.CliectAccessId);
			pi.setType(String.class);
			request.addProperty(pi);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			//Log.i(">>>>>Request<<<<<", request.toString());
			envelope.encodingStyle = SoapSerializationEnvelope.ENC;
			envelope.implicitTypes = true;
			envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
					this.getClass());

			/*MarshalLong mlong = new MarshalLong();
			mlong.register(envelope);*/
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log("Notitfication data ","response"+androidHttpTransport.requestDump);
			Utils.log("Notitfication data ","request"+androidHttpTransport.responseDump);
			Object response1 = envelope.getResponse();
			String strJson = response1.toString();
			
			Utils.log(" >>>Notitfication Data from Server<<< ", ":"+strJson);
			String OutputData = "";
	        JSONObject jsonResponse = new JSONObject(strJson);
	        JSONObject jsonMainNode = jsonResponse.optJSONObject("NewDataSet");
        	
        	//int lengthJsonArr = jsonMainNode.length();  
	
			//for(int i=0; i < lengthJsonArr; i++) 
		//	{
					
					
					/****** Get Object for each JSON node.***********/
					//JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	        if(jsonMainNode.get("Table") instanceof JSONObject){
	        	Notificationobj NotifyObj = new Notificationobj();
	        	JSONObject jsonMainNode1 = jsonMainNode.getJSONObject("Table");
	        	
	        	String isNotify   = jsonMainNode1.optString("IsNotify").toString();
				String menberId = jsonMainNode1.optString("MemberId").toString();
				
				String notifyId = jsonMainNode1.optString("NotifyId").toString();
				String notificationMessage = jsonMainNode1.optString("NotificationMessage").toString();
				String dataFrom=jsonMainNode1.optString("DataFrom").toString();
				String icard_id=jsonMainNode1.optString("CreatedBy").toString();
			
				if(dataFrom.equalsIgnoreCase("Profile Update")){
					SharedPreferences sharedPreferences1 = context
							.getSharedPreferences(context.getString(R.string.shared_preferences_profile), 0);
					SharedPreferences.Editor editor = sharedPreferences1.edit();
					editor.putBoolean("profile", true);
					editor.commit();
					is_show=true;
				}
				if(dataFrom.equalsIgnoreCase("renewal")){
					//Utils.log("inonhome","yes");
					SharedPreferences sharedPreferences1 = context
							.getSharedPreferences(context.getString(R.string.shared_preferences_renewal), 0);
					SharedPreferences.Editor editor = sharedPreferences1.edit();
					editor.putBoolean("renewal", true);
					editor.putBoolean("profile", true);
					editor.commit();
					is_show=true;
				}
				if(dataFrom.equalsIgnoreCase("renewal")){
					//Utils.log("payment check","yes");
					SharedPreferences sharedPreferences1 = context
							.getSharedPreferences(context.getString(R.string.shared_preferences_payment_history), 0);
					SharedPreferences.Editor editor = sharedPreferences1.edit();
					editor.putBoolean("payment_history", true);
					editor.commit();
					is_show=true;
					
				}
				
					//Utils.log("notification list","yes");
				if(notifyId.length()>0){
					SharedPreferences sharedPreferences1 = context
							.getSharedPreferences(context.getString(R.string.shared_preferences_notification_list), 0);
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
	        
	       
	        if(jsonMainNode.get("Table") instanceof JSONArray)	{
					JSONArray jsonMainNode1 = jsonMainNode.optJSONArray("Table");
				
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
						SharedPreferences sharedPreferences1 = context
								.getSharedPreferences(context.getString(R.string.shared_preferences_profile), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("profile", true);
						editor.commit();
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("inonhome","yes");
						SharedPreferences sharedPreferences1 = context
								.getSharedPreferences(context.getString(R.string.shared_preferences_renewal), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("renewal", true);
						editor.putBoolean("profile", true);
						editor.commit();
					}
					if(dataFrom.equalsIgnoreCase("renewal")){
						//Utils.log("payment check","yes");
						SharedPreferences sharedPreferences1 = context
								.getSharedPreferences(context.getString(R.string.shared_preferences_payment_history), 0);
						SharedPreferences.Editor editor = sharedPreferences1.edit();
						editor.putBoolean("payment_history", true);
						editor.commit();
						
					}
					
						//Utils.log("notification list","yes");
						SharedPreferences sharedPreferences1 = context
								.getSharedPreferences(context.getString(R.string.shared_preferences_notification_list), 0);
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
			//}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			//Utils.log("Error is",""+e);
			throw e;
		}
		
	}
	
	private void showNotification(){
		 int notifID = rand.nextInt(100);
		
		 String notifyId="";
		 String memberId="";
		Intent notificateionIntent=new Intent(context,NotificationListActivity.class);
		notificateionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putString("NotifyId",MemberId);
		PendingIntent detailsIntent = PendingIntent.getActivity(context, 1, notificateionIntent, notifID);
			
		final Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationManager notificationManager = (NotificationManager) systemService;
			
	        
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, notificateionIntent, notifID);
		
		if(SDK_INT > 15){
			
		//	Iterator iter = notificationList.iterator();
			
			//while(iter.hasNext()){
				for(int i=0;i<notificationList.size();i++){
				Notificationobj	NotifyObj	= notificationList.get(i);
				//Log.i("--------------------NotifyId-----------------", NotifyObj.getNotifyId());
				if(NotifyObj.getIsNotify()!=null){
					if(NotifyObj.getIsNotify().length()>0){
				Notification notification = new Notification.Builder(context)
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
				
					//notification.setLatestEventInfo(context, "ODiGitel Broadband Notification ", NotifyObj.getNotificationMessage(), contentIntent);
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
	

/*private void updateStatus(String NotifyId){
    	
    	//Log.i("*********** STATUS CHANGE... ",""+complID);
    	
    	
    	try {
    		NotificationStatusUpdateSOAP soap = new NotificationStatusUpdateSOAP(context.getResources().getString(
    				R.string.WSDL_TARGET_NAMESPACE), context.getResources().getString(
    						R.string.SOAP_URL),context
    						.getResources().getString(
    								R.string.METHOD_UPDATE_NOTIFICATION));
        	Log.i("THIS is Notification Test------------------" ,MemberId);
        	StatusUpdateHelper helper = new StatusUpdateHelper(soap,MemberId,NotifyId,true);
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
	
	
	
	private void updteNotification(String memberId, String notifyId){
		UpdateNotificationWS update = new UpdateNotificationWS(context,memberId,notifyId);
		 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			 update.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		 else	 
			 update.execute();
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public Iterator getNotificationItear() {
		return notificationItear;
	}
	public void setNotificationItear(Iterator notificationItear) {
		this.notificationItear = notificationItear;
	}
	public int getSDK_INT() {
		return SDK_INT;
	}
	public void setSDK_INT(int sDK_INT) {
		SDK_INT = sDK_INT;
	}
	public String getWSDL_TARGET_NAMESPACE() {
		return WSDL_TARGET_NAMESPACE;
	}
	public void setWSDL_TARGET_NAMESPACE(String wSDL_TARGET_NAMESPACE) {
		WSDL_TARGET_NAMESPACE = wSDL_TARGET_NAMESPACE;
	}
	public String getSOAP_URL() {
		return SOAP_URL;
	}
	public void setSOAP_URL(String sOAP_URL) {
		SOAP_URL = sOAP_URL;
	}
	public String getMETHOD_NAME() {
		return METHOD_NAME;
	}
	public void setMETHOD_NAME(String mETHOD_NAME) {
		METHOD_NAME = mETHOD_NAME;
	}
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}
	public String getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}
	
	
}
