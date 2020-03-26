package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.database.Cursor;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;



public class UpdateNotifyIdSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	DatabaseAdapter dbAdapter;
	LinkedHashMap<String,String> alNotifyIdDatabase;
	Cursor mCursor;
	private String METHOD_NAME;
	public static int messageCount=0;
	
	public UpdateNotifyIdSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String updateNotifyId(String UserId,ArrayList<String>alNotifyId,Context ctx)throws SocketException,SocketTimeoutException,Exception
	{
		String str_msg="ok";
		//dbAdapter= new DatabaseAdapter(ctx);
		dbAdapter= new DatabaseAdapter(ctx);
		Utils.log("UpdateNotifyIdSOAP"," is: "+alNotifyId.size());
		dbAdapter.open();
		mCursor=dbAdapter.getNotifyId();
		
		/*Profile_Id means ClientId */
		if(mCursor!=null){
	if(mCursor.getCount()>0){
		
			while(mCursor.moveToNext())
			{
				try{
		SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		Utils.log("UpdateNotifyIdSOAP"," is: "+alNotifyId.size());
		PropertyInfo pi;
		pi= new PropertyInfo();
		pi.setName("NotifyId");
		pi.setValue(mCursor.getString(mCursor
				.getColumnIndex(TableConstants.NotifYId)));
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("MemberId");
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
		Utils.log("UpdateNotifyIdSOAP request","is: "+androidHttpTransport.requestDump);
		Utils.log("UpdateNotifyIdSOAP response","is: "+androidHttpTransport.responseDump);
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
			dbAdapter.close();
			return str_msg;
			
		}
		
		}
				
		catch(Exception e){
			str_msg="error";
			Utils.log("UpdateNotifyId SOAP main try","error: "+e);
			dbAdapter.close();
			return str_msg;
		}
				
		}
			return str_msg;
	}
	else{
		dbAdapter.close();
	}
		}
		else{
			dbAdapter.close();
		}
		return str_msg;
	}
	
}
