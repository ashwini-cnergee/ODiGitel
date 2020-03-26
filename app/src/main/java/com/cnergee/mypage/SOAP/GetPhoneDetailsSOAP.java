package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;

public class GetPhoneDetailsSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "GetPhoneDetailsSOAP"; 
	private String Username;
	private String Password;
	
	public GetPhoneDetailsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getPhoneDetails(long MemberId,String Manufacturername,String AppVersion)throws SocketException,SocketTimeoutException,Exception,SoapFault{
		String str_msg = "ok";
		try{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("Memberid");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("AppVersion");
		pi.setValue(AppVersion);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("Manufacturername");
		pi.setValue(Manufacturername);
		pi.setType(String.class);
		request.addProperty(pi);
		//Log.i(">>>>>Request<<<<<", request.toString());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log("Check",""+WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Utils.log(TAG ,"request: "+androidHttpTransport.requestDump);
		Utils.log(TAG,"response :"+androidHttpTransport.responseDump);
		Utils.log("Server ","response :"+envelope.getResponse().toString());
		str_msg="ok";
		return str_msg;
				
	}
		catch(Exception e){
			return "error";
		}
	}
	public String getUsername(){
		return Username;
	}
	
	public String getPassword(){
		return Password;
	}
	
}
