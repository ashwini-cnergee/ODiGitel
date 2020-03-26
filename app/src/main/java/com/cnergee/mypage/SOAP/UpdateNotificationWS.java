package com.cnergee.mypage.SOAP;

import android.content.Context;
import android.os.AsyncTask;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cnergee.krishna.ookla.odigitel.R;

public class UpdateNotificationWS extends AsyncTask<Void, Void, Void>{
	
	private String TAG ="NotificationAsyncWS";
	private Context context;
	//private Iterator<JSONObject> notificationItear;
	private Iterator notificationItear;
	private String NotifyId;
	private String MemberId;
	private String str_msg;
	private String ResponseMessage;
	private int SDK_INT;
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	String NotificationId;
	 Random rand = new Random();
		ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
	
	public UpdateNotificationWS(Context context,String Memberid, String NotifyId){
		this.context = context;
		this.MemberId = Memberid;
		this.NotifyId = NotifyId;
	}
	@Override
	protected Void doInBackground(Void... arg0) {

		//Log.i(TAG, "doInBackground");
        try {
			//getNotificationData();
			updateNotificationData();
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
         ///showNotification();
     }

     @Override
     protected void onPreExecute() {
        // Log.i(TAG, "onPreExecute");
     }

     @Override
     protected void onProgressUpdate(Void... values) {
         //Log.i(TAG, "onProgressUpdate");
     }
     
     private String updateNotificationData() throws IOException, Exception{
 		String WSDL_TARGET_NAMESPACE = "http://tempuri.org/"; //this.WSDL_TARGET_NAMESPACE;
 		String SOAP_URL = "http://114.79.129.12:8001/CCRMToMobileIntegration.asmx?wsdl";//this.SOAP_URL;
 		String METHOD_NAME = "UpdateMultipleNotification";//this.METHOD_NAME;
 		
 		//Utils.log("Notification:", " did something!!");
 		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
 		
 		
 		//	Log.i(" #	#####################  ", " START ");
 		//	Log.i(TAG+" notifyid ", ""+NotifyId);
 			/*Log.i(TAG+" IMEI No ", Authobj.getIMEINo());
 			Log.i(TAG+" Mobile ", Authobj.getMobileNumber());
 			Log.i(TAG+" Mobile User ", Authobj.getMobLoginId());
 			Log.i(TAG+" Mobile Password ", Authobj.getMobUserPass());*/
 			//Log.i(TAG+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
 		//	Log.i(TAG+" METHOD_NAME ", METHOD_NAME);
 		//	Log.i(TAG+" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
 		//	Log.i("#####################", "");
 			
 			
 			
 			PropertyInfo pi = new PropertyInfo();
 			pi.setName("MemberId");
 			pi.setValue(MemberId);
 			pi.setType(String.class);
 			request.addProperty(pi);
 			
 			pi = new PropertyInfo();
 			pi.setName("NotifyId");
 			pi.setValue(NotifyId);
 			pi.setType(String.class);
 			request.addProperty(pi);
 			
 			pi = new PropertyInfo();
 			pi.setName(AuthenticationMobile.CliectAccessName);
 			pi.setValue(AuthenticationMobile.CliectAccessId);
 			pi.setType(String.class);
 			request.addProperty(pi);
 			
 			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
 					SoapEnvelope.VER11);
 			envelope.dotNet = true;
 			envelope.setOutputSoapObject(request);
 			
 			//Log.i("----------------Response-----------------", request.toString());
 			
 			envelope.encodingStyle = SoapSerializationEnvelope.ENC;
 			envelope.implicitTypes = true;
 			envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
 					this.getClass());
 			
 			
 			HttpTransportSE androidHttpTransport = new HttpTransportSE(context.getString(R.string.SOAP_URL));
 			androidHttpTransport.debug = true;

 			String str_msg = "ok";
 			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
 			Utils.log("Upadate Request",":"+androidHttpTransport.requestDump);
 			Utils.log("Upadate Response",":"+androidHttpTransport.responseDump);
 			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
 				
 				Object response = envelope.getResponse();
 				if (response != null) {
 					//Log.i(" RESPONSE ", response.toString());
 					//setResponseMessage(response.toString());
 				
 				} else {
 					str_msg = "ok";
 				}

 			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
 																// FAILURE
 				SoapFault soapFault = (SoapFault) envelope.bodyIn;
 				str_msg = "failed";
 			}
 			return str_msg;
 		
     }
 			
	
	
	

}
