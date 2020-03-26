package com.cnergee.mypage.SOAP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;






public class GetServerMessageSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	DatabaseAdapter dbAdapter;
	ArrayList<String> alNotifyId;
	Context ctx;
	
	private String METHOD_NAME;
	public static int messageCount=0;
	public GetServerMessageSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,Context context) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getServerMessage(String Profile_Id,Context ctx)throws SocketException,SocketTimeoutException,Exception{
		String str_msg="ok";
		//TableConstants.MESSAGE_COUNT=0;
		dbAdapter= new DatabaseAdapter(ctx);
		alNotifyId= new ArrayList<String>();
		/*Profile_Id means ClientId */
		 messageCount=0;
		try{
		SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi;
		pi= new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(Profile_Id);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle=SoapSerializationEnvelope.ENC;
		
		envelope.implicitTypes=true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "", this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		try{
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log("GetServerMessage SOAP request","is: "+androidHttpTransport.requestDump);
		Utils.log("GetServerMessage SOAP response","is: "+androidHttpTransport.responseDump);
		
		
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			
			SoapObject response = (SoapObject) envelope.getResponse();
			if (response != null) {
				response = (SoapObject) response.getProperty("NewDataSet");
				
				
				
				if (response.getPropertyCount() > 0) {
					Utils.log("GetServerMessage SOAP ","Response count: "+response.getPropertyCount());
					dbAdapter.open();
					for (int i = 0; i < response.getPropertyCount(); i++) {
						SoapObject tableObj = (SoapObject) response.getProperty(i);
						
				if(Boolean.valueOf(tableObj.getPropertyAsString("ChatResponse"))){
					
					Intent intentBroadcast= new Intent("chat_online");
					 LocalBroadcastManager.getInstance(ctx).sendBroadcast(intentBroadcast);
					
					String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
					SharedPreferences sharedPreferences = ctx.getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
					
					SharedPreferences.Editor editor= sharedPreferences.edit();
					editor.putBoolean("chat_status", true);
					editor.commit();
					
					String message=	tableObj.getPropertyAsString("Message").toString();
					String type=	"";//tableObj.getPropertyAsString("MessageType").toString();
					if(tableObj.getPropertyAsString("MessageType").toString().equalsIgnoreCase("0")){
						type=	"Text";
					}
					if(tableObj.getPropertyAsString("MessageType").toString().equalsIgnoreCase("")){
						type=	"Image";
					}
					String date=	tableObj.getPropertyAsString("CreatedDate").toString();
					String time=	Convert24to12(tableObj.getPropertyAsString("CreatedTime").toString());
					String source=	"server";
					String path=	"no path"; //tableObj.getPropertyAsString("MessagePath").toString();
					String status=	"download";
					String date_change=	"false";
					String source_id="1";	//tableObj.getPropertyAsString("CreatedBy").toString();
					String sync_status=	"no";
					String url= "server";  //	tableObj.getPropertyAsString("MessageSource").toString();
					String notifyId=tableObj.getPropertyAsString("MessageId").toString();
					String Admin_name=tableObj.getPropertyAsString("LastChatWith").toString();
					/* When a user is assigned to a group ,Group Id is updated ***starts here***** */
					
					if(type.equalsIgnoreCase("Group")){
						//dbAdapter.open();
						dbAdapter.updateOrInsertGroup(url, message);
						//dbAdapter.open();
						TableConstants.MESSAGE_COUNT=0;
						UpdateGroupNotifyIdSOAP updateGroup= new UpdateGroupNotifyIdSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, "UpdateNotification");
						String updateGroup1=updateGroup.updateGroupNotifyId(Profile_Id, notifyId);
						Utils.log("Response for Group Update",":"+updateGroup1);
					}
					
					/* When a user is assigned to a group ,Group Id is updated ***ends here***** */
					
					
					alNotifyId.add(tableObj.getPropertyAsString("MessageId").toString());
					
					if(!type.equalsIgnoreCase("Group")){
					if(dbAdapter.getNotifyCount(notifyId)==0){
						dbAdapter.insertMessgae(message, type, date, time, source, path, status, date_change, source_id, sync_status, url,notifyId,"uploaded",Admin_name);
						TableConstants.MESSAGE_COUNT=TableConstants.MESSAGE_COUNT+1;
					}
					}
				
					
					
					messageCount=TableConstants.MESSAGE_COUNT;
					//UpdateNotifyIdSOAP getServerMessageSOAP= new UpdateNotifyIdSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL),getString(R.string.METHOD_UPDATE_NOTIFY_ID));	
					Utils.log("******update notifyId**********","starts"+messageCount);
					/*UpdateEachNotifyIdSOAP updateEachNotifyIdSOAP= new UpdateEachNotifyIdSOAP(ctx.getString(R.string.WSDL_TARGET_NAMESPACE), ctx.getString(R.string.SOAP_URL),ctx.getString(R.string.METHOD_UPDATE_NOTIFY_ID));
					if(updateEachNotifyIdSOAP.updateEachNotifyId(Profile_Id,tableObj.getPropertyAsString("NotifyId").toString(),ctx).equalsIgnoreCase("ok"))
					{
						Utils.log("GetServerMessage SOAP","OK RESPONSE");
						Utils.log("NotifyId updated ",":");
					}
					else{
						Utils.log("In GetServerMessageSOAP","updateEachNotifyIdSOAP error");
					}
					Utils.log("******update notifyId**********","starts");
					*/
					}
				else{
					String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
					SharedPreferences sharedPreferences = ctx.getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
					
					SharedPreferences.Editor editor= sharedPreferences.edit();
					editor.putBoolean("chat_status", false);
					editor.commit();
				}
					}
					Utils.log("Close ","Database ");
					dbAdapter.close();
					
				} else {
					
				}
			} else {
			//	Utils.log("GetServerMessage SOAP ","no Response: ");
			}

		}
			else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
			// FAILURE
				
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			str_msg="error";
// throw new Exception(soapFault.getMessage());
			return str_msg;
			}
		
		
		}
		catch(Exception e){
			String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = ctx.getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			
			SharedPreferences.Editor editor= sharedPreferences.edit();
			editor.putBoolean("chat_status", false);
			editor.commit();
			
			str_msg="error";
			Utils.log("inner try","error: "+e);
			if(dbAdapter!=null){
				if(dbAdapter.isOpen()){
					dbAdapter.close();
				}
			}
			return str_msg;
		}
		return str_msg;
		}
		catch(Exception e){
			
			String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = ctx.getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			
			SharedPreferences.Editor editor= sharedPreferences.edit();
			editor.putBoolean("chat_status", false);
			editor.commit();
			
			str_msg="error";
			Utils.log("main try","error: "+e);
			if(dbAdapter!=null){
				if(dbAdapter.isOpen()){
					dbAdapter.close();
				}
			}
			return str_msg;
		}
	}
	public ArrayList<String> getNotifyIdList(){
	//	Utils.log("GetServerMessageSOAP NotifyId","Count"+alNotifyId.size());
		return alNotifyId;
	}
	public static String Convert24to12(String time)
	{
	    String convertedTime ="";
	    try {
	        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
	        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
	        Date date = parseFormat.parse(time);        
	        convertedTime=displayFormat.format(date);
	        //System.out.println("convertedTime : "+convertedTime);
	    } catch (final java.text.ParseException e) {
	        e.printStackTrace();
	    }
	    return convertedTime;
	    //Output will be 10:23 PM
	}
}
