package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;

public class DeleteNotificationSoap {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public String response;
	//private static final String TAG = "MypageService"; 
	private ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
	
	public DeleteNotificationSoap(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String deleteNotificationList(String NotifyId,String MemberId)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		/*Log.i(" #	#####################  ", " START ");
		Log.i(TAG+" userId ", ""+MemberId);
		Log.i(TAG+" IMEI No ", Authobj.getIMEINo());
		Log.i(TAG+" Mobile ", Authobj.getMobileNumber());
		Log.i(TAG+" Mobile User ", Authobj.getMobLoginId());
		Log.i(TAG+" Mobile Password ", Authobj.getMobUserPass());*/
		/*Log.i("DeleteNotificationSoap"+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i("DeleteNotificationSoap"+" METHOD_NAME ", METHOD_NAME);
		Log.i("DeleteNotificationSoap"+" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);*/
		//Log.i("#####################", "");
		
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
	
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		//Utils.log("Request","s "+androidHttpTransport.requestDump);
		//Object response1 = envelope.getResponse();
		//Utils.log("Notification Delted","s "+envelope.getResponse().toString());
		
		 response=  envelope.getResponse().toString();
		
	
	return str_msg;
		
		
	}

	
	public String getResponse(){
		return response;
	}
	
	
}
