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

public class GetPlanFor_TopupSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String response;
	
	
	public GetPlanFor_TopupSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String getPlanDeatilsFor_Topup(String SubscriberId) throws SocketException,SocketTimeoutException,Exception {

	

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName("SubscriberId");
		pi.setValue(SubscriberId);
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
		
		
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

		try {
			
			
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Utils.log(this.getClass().getSimpleName()+":","request:"+androidHttpTransport.requestDump);
		Utils.log(this.getClass().getSimpleName()+":","response:"+androidHttpTransport.responseDump);
		 
		response = envelope.getResponse().toString();
				
			
		return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	public String getResponse(){
		return response;
	}
	
}
