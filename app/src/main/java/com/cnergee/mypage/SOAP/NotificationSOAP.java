package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;


public class NotificationSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "NotificationSOAP"; 
	private ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
	
	public NotificationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String setNotificationList(String MemberId)throws SocketException,SocketTimeoutException,Exception{
		try{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		Log.i(" #	#####################  ", " START ");
		Log.i(TAG+" userId ", ""+MemberId);
		
		Log.i(TAG+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(TAG+" METHOD_NAME ", METHOD_NAME);
		Log.i(TAG+" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
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
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
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

		String str_msg = "ok";
		ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();

		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Object response1 = envelope.getResponse();
		Utils.log(TAG+" Request ", ""+androidHttpTransport.requestDump);
		Utils.log(TAG+" Response ", ""+androidHttpTransport.responseDump);
		/*JSONObject Obj = new JSONObject(response.toString());
		
		JSONArray  Obj1 = Obj.getJSONArray("NewDataSet");
		//Log.i("MyCheckPoint",Obj.toString());
		//JSONArray Obj2 = Obj1.JSONArray("Table");
		
		for(int i=0; i<Obj1.length(); i++)
		{
		        JSONObject obj=Obj1.getJSONObject(i);
		        JSONArray Obj2 = obj.getJSONArray("Table");
		        Notificationobj notiobj = new Notificationobj();
		        for(int j=0; j< Obj2.length(); j++)
		        {
		        	JSONObject obj1 =Obj2.getJSONObject(j);
		        	
		        	
		        	notiobj.setNotifyId(obj1.getString("NotifyId"));
		        	notiobj.setNotificationMessage(obj1.getString("NotificationMessage"));
		        	
		        	Value = obj1.getString("myCheckPoint");
		        	Utils.log("Item name: ", Value);
		        }
		        notificationList.add(notiobj);
		        
		        //String value = obj.getString("FieldName"); 
		        
		}
		
		String AuthCode = Value;
		Log.i("MyCheckPoint",AuthCode);
		
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Log.i(">>>>>This is test<<<<", "My Data");
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i(">>>>>NOTIFICATION RESPONSE<<<<<",response.toString());
				if (response != null) {
					
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
							Notificationobj obj = new Notificationobj();
							obj.setNotifyId(tableObj.getPropertyAsString("NotifyId").toString());
							obj.setNotificationMessage(tableObj.getPropertyAsString("NotificationMessage").toString());
							obj.setPush(Boolean.parseBoolean(tableObj.getPropertyAsString("IsPush").toString()));
							obj.setRead(Boolean.parseBoolean(tableObj.getPropertyAsString("IsRead").toString()));
							obj.setSclosed(Boolean.parseBoolean(tableObj.getPropertyAsString("IsClosed").toString()));
							obj.setUpdated(Boolean.parseBoolean(tableObj.getPropertyAsString("IsUpdated").toString()));
							obj.setMemberId(tableObj.getPropertyAsString("MemberId").toString());
							obj.setMemberId(MemberId);
							
							notificationList.add(obj);						
						}
						
					} else {
						notificationList.add(new Notificationobj());	
					}
				} else {
					notificationList.add(new Notificationobj());
				}

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				notificationList.add(new Notificationobj());
			}
			setNotificationList(notificationList);
		return str_msg;*/
		
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			
			SoapObject response = (SoapObject) envelope.getResponse();
			if (response != null) {
				
				Utils.log(" >>>>Notification Data ",response.toString());
				response = (SoapObject) response.getProperty("NewDataSet");
				if (response.getPropertyCount() > 0) {
					for (int i = 0; i < response.getPropertyCount(); i++) {
						SoapObject tableObj = (SoapObject) response.getProperty(i);
						Notificationobj obj = new Notificationobj();
						/*obj.setComplaintId(tableObj.getPropertyAsString("Comptid").toString());
						obj.setComplaintNo(tableObj.getPropertyAsString("ComplaintNo").toString());
						obj.setPush(Boolean.parseBoolean(tableObj.getPropertyAsString("IsPush").toString()));
						obj.setRead(Boolean.parseBoolean(tableObj.getPropertyAsString("IsRead").toString()));
						obj.setSclosed(Boolean.parseBoolean(tableObj.getPropertyAsString("IsClosed").toString()));
						obj.setUpdated(Boolean.parseBoolean(tableObj.getPropertyAsString("IsUpdated").toString()));
						obj.SetMemberLoginId(tableObj.getPropertyAsString("MemberLoginID").toString());
						obj.setUserId(userId);*/
						if(tableObj.hasProperty("NotifyId"))
						obj.setNotifyId(tableObj.getPropertyAsString("NotifyId").toString());
						else
						obj.setNotifyId("");
						
						if(tableObj.hasProperty("NotificationMessage"))
						obj.setNotificationMessage(tableObj.getPropertyAsString("NotificationMessage").toString());
						else
						obj.setNotificationMessage("");	
						
						if(tableObj.hasProperty("IsNotify"))
						obj.setIsNotify(tableObj.getPropertyAsString("IsNotify").toString());
						else
						obj.setIsNotify("");
						
						if(tableObj.hasProperty("DataFrom"))
						obj.setDataFrom(tableObj.getPropertyAsString("DataFrom").toString());
						else
						obj.setDataFrom("");	
						
						if(tableObj.hasProperty("CreatedBy"))
						obj.setIcard_id(tableObj.getPropertyAsString("CreatedBy").toString());
						else
						obj.setIcard_id("");	
						notificationList.add(obj);						
					}
					
				} else {
					//notificationList.add(new Notificationobj());	
				}
			} else {
				//notificationList.add(new Notificationobj());
			}

		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			notificationList.add(new Notificationobj());
		}
		setNotificationList(notificationList);
		return str_msg;
		}
		catch(Exception e){
			Utils.log("Error in ",":"+e);
			return "error";
		}
	
		
		
	}

	public ArrayList<Notificationobj> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(ArrayList<Notificationobj> notificationList) {
		this.notificationList = notificationList;
	}

	
	
	
	

}
