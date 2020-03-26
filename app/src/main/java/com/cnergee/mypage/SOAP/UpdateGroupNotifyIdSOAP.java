package com.cnergee.mypage.SOAP;


import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.database.Cursor;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;




public class UpdateGroupNotifyIdSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	DatabaseAdapter dbAdapter;
	LinkedHashMap<String,String> alNotifyIdDatabase;
	Cursor mCursor;
	private String METHOD_NAME;
	public static int messageCount=0;
	
	public UpdateGroupNotifyIdSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	public String updateGroupNotifyId(String UserId,String alNotifyId)
	{
		String str_msg="ok";
		
		try{
			SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
			Utils.log("UpdateGroupNotifyIdSOAP"," is: "+alNotifyId);
			PropertyInfo pi;
			pi= new PropertyInfo();
			pi.setName("Notifyid");
			pi.setValue(alNotifyId);
			pi.setType(String.class);
			request.addProperty(pi);
			

			
			pi= new PropertyInfo();
			pi.setName("UserId");
			pi.setValue(UserId);
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
				Utils.log("UpdateGroupNotifyIdSOAP request","is: "+androidHttpTransport.requestDump);
				Utils.log("UpdateGroupNotifyIdSOAP response","is: "+androidHttpTransport.responseDump);
				String response=envelope.getResponse().toString();
				if(response.equalsIgnoreCase("1")){
					dbAdapter.open();
					dbAdapter.UpdateNotifyIdStatus(mCursor.getString(mCursor
							.getColumnIndex(TableConstants.NotifYId)));
					dbAdapter.close();
				}
				else{
					
				}
				
				}
				catch(Exception e){
					str_msg="error";
					Utils.log("UpdateNotifyId SOAP inner try","error: "+e);
					return str_msg;
				}
		
		return str_msg;
		}
		catch(Exception e){
			return str_msg;
		}
	}
	
}
