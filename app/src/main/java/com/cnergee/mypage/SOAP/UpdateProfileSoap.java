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

public class UpdateProfileSoap {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static String TAG = "UpdateProfileSoap";
	private String JsonResponse;
	
	public UpdateProfileSoap(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		
		Utils.log("On  Upda SOAP ", "Profile SOAP Executed");

		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
	}
	

	public String GetUpdateProfile(String MemberId, String Message, String stringType)throws Exception,SocketTimeoutException,SocketException{
		
		String Result ="OK";
		try{
			
			Utils.log("On  UpdateWeb ser ", " Reqest Executed");

			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
			
			PropertyInfo pi = new PropertyInfo();
			pi.setName("MemberId");
			pi.setValue(MemberId);
			pi.setType(String.class);
			request.addProperty(pi);
			
			
		 pi = new PropertyInfo();
		 pi.setName("Message");
		 pi.setValue(Message);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 
		 pi = new PropertyInfo();
		 pi.setName("stringType");  //Address
		 pi.setValue(stringType);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 

		/* pi = new PropertyInfo();
		 pi.setName("mob");      //Mobile no
		 pi.setValue(stringType);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 pi = new PropertyInfo();
		 pi.setName("alt");     //alltt mobile no
		 pi.setValue(stringType);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 
		 pi = new PropertyInfo();
		 pi.setName("eml");			//Email
		 pi.setValue(stringType);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 

		 pi = new PropertyInfo();
		 pi.setName("dob");    //Dateofbirth
		 pi.setValue(stringType);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 */
		 

		 
		 pi = new PropertyInfo();     ///clientAccess name 
		 pi.setName(AuthenticationMobile.CliectAccessName);			
		 pi.setValue(AuthenticationMobile.CliectAccessId);
		 pi.setType(String.class);
		 request.addProperty(pi);
		 
		 
		 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		 envelope.dotNet = true;
		 envelope.setOutputSoapObject(request);
		 envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		 envelope.implicitTypes = true;
		 
		 
		
		 HttpTransportSE transportSE = new HttpTransportSE(SOAP_URL);
		
		 transportSE.debug = true;
		 
		 transportSE.call(WSDL_TARGET_NAMESPACE+METHOD_NAME, envelope);
		 
		 
		 
		 Utils.log("Request :",""+transportSE.requestDump);
		 Utils.log("Response :",""+transportSE.responseDump);
		 
		 Utils.log(TAG+"","URl :"+SOAP_URL);
		 Utils.log(TAG+"","METHOD :"+METHOD_NAME);
			
		 
		 
		 
		 JsonResponse = envelope.getResponse().toString(); 
		 return Result;
			
		}catch(Exception e){
			Utils.log(TAG+"error",""+e);
			return "Error";
			
		}
		
		
	}


	public String getjsonResponse() {
		// TODO Auto-generated method stub
		return JsonResponse;
	}
}
