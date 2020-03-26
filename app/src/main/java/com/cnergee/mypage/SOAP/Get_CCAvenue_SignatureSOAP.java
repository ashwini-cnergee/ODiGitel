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
import com.cnergee.mypage.utils.Utils;

public class Get_CCAvenue_SignatureSOAP {


	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String response;
	
	
	public Get_CCAvenue_SignatureSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String getSignature(String MemberId,String Amount,String TrackId) throws SocketException,SocketTimeoutException,Exception {

	

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		Utils.log("SOAP_URL", ":"+SOAP_URL);
		

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(MemberId);//chandra_shekhar
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Amount");
		pi.setValue(Amount);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TrackId");
		pi.setValue(TrackId);
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
