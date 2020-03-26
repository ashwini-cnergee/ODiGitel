package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.LoginObj;
import com.cnergee.mypage.utils.Utils;

public class SMSAuthenticationSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "MyPage"; 
	private String mobilenumber;
	private boolean isvalid;
	private String Authcount;
	private String Value;
	private String responseMsg;
	
	LoginObj Login = new LoginObj();
	
	
	public SMSAuthenticationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		//Log.i(" #	#####################  ", " START ");
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallSMSSOAP(String MemberId,String OneTimePwd,String PhoneUniqueId
			)throws SocketException,SocketTimeoutException,Exception {
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" #	#####################  ", mobilenumber);
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PhoneUniqueId");
		pi.setValue(PhoneUniqueId);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("Ontimepassword");
		pi.setValue(OneTimePwd);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		// envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		// envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE,"",this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log(TAG+" SMS REQUEST ",androidHttpTransport.requestDump);
		Object response = envelope.getResponse();
		Utils.log(TAG+" SMS RESPONSE ",androidHttpTransport.responseDump);
		
		
		//Log.i("MyCheckPoint",AuthCode);
		
		try{
			if(response.toString().equalsIgnoreCase("1")){
				setIsValidUser(true);
			}
			else{
				setIsValidUser(false);
			}
				//setIsValidUser(Boolean.getBoolean(response.toString()));
				
				
				
		
			
			
		}catch(NumberFormatException n){
			//Log.i(TAG+" LOGIN RESPONSE ",n.getMessage());
		}
		return "OK";
	}

	
	public void setIsValidUser(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public boolean isValidUser() {
		return isvalid;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getAuthcount() {
		return Authcount;
	}

	public void setAuthcount(String authcount) {
		Authcount = authcount;
	}

	

	
	
	

}
