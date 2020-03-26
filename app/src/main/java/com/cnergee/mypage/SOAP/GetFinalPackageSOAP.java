package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;

public class GetFinalPackageSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "GetFinalPackageSOAP"; 
	private String response="";

	
	public GetFinalPackageSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getFinalPrice(String PlanName,String Price)throws SocketException,SocketTimeoutException,Exception{
		String str_msg = "ok";
		
		try{
			//Utils.log(TAG+"id",": "+MemberId);
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("PackageName");
		pi.setValue(PlanName);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Amount");
		pi.setValue(Price);
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
		Utils.log(TAG,"request :"+androidHttpTransport.requestDump);
		Utils.log(TAG ,"response: "+androidHttpTransport.responseDump);
		str_msg="ok";
		
		response=envelope.getResponse().toString();
		
		return str_msg;
		}
		catch (Exception e) {
			str_msg="error";
			return str_msg;
		}
	}
	
	public String getResponse(){
		return response;
	}

}
