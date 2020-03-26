package com.cnergee.mypage.SOAP;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class GetAtomSignatureSoap {
	
	
	private String SOAP_URL;
	private String WSDL_TARGET_NAMESPACE;
	private String METHOD_NAME;
	public static String TAG = "GetAtomSignatureSoap";
	public String response;
	
	
	
	public GetAtomSignatureSoap(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		
	}
	
	public String GetAtomSignatureResult(long MemberId,String Amount,String TrackId)throws Exception,SocketException,SocketTimeoutException{
		
		String Result = "OK";
	
		try{
			Utils.log(""+this.getClass().getSimpleName(),":"+SOAP_URL);
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
			
			PropertyInfo pi = new PropertyInfo();
			pi.setName("MemberId");	
			Utils.log(TAG+"member:",""+MemberId);
			pi.setValue(MemberId);//78846
			pi.setType(long.class);
			request.addProperty(pi);
			
			
			pi = new PropertyInfo();
			pi.setName("Amount");
			pi.setValue(Amount);
			Utils.log("Amount ", "" + Amount);
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
			 
			 
			 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			 envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 envelope.encodingStyle =  SoapSerializationEnvelope.ENC;
			 envelope.implicitTypes = true;
			 envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
						this.getClass());
			 
			 HttpTransportSE transport = new HttpTransportSE(SOAP_URL);
			 transport.debug=true;
			 
			 
			 transport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME, envelope);
			 
			 Utils.log("GetAtomSoapRequest",""+transport.requestDump);
			 Utils.log("GetAtomSoapResponse", "" + transport.responseDump);
			 Utils.log("Amount",""+Amount);
			 
			 response = envelope.getResponse().toString();
			Utils.log("GetAtomSoapRequest",""+request.toString());
			Utils.log("GetAtomSoapResponse",""+response.toString());
			 
		}catch(Exception e){
			Utils.log("Error",":"+e);
		}
		
	
		
		return Result;
	
	}

	public String getResponse() {
		// TODO Auto-generated method stub
		return response;
	}
	


	

}
