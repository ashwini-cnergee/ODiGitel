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

public class SelfResolutionSOAP  {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static String TAG = "SelfResolution";
	
	private String jsonResponse;
	
	public SelfResolutionSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,String METHOD_NAME){
		
		this.METHOD_NAME = METHOD_NAME;
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		
		Utils.log("line no 32", " Selfresolution Executed ");
	}
	
	public String GetSelfResolutionResult(String MemberId)throws SocketException,SocketTimeoutException,Exception{
		
		String Result = "OK";
		
		try{
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME); 
			
			Utils.log(TAG+"member:",""+MemberId);
			Utils.log("line no b43 ","in Soap GetResolution");
			
			PropertyInfo pi = new PropertyInfo();
			pi.setName("Memberid");	
			Utils.log(TAG+"member:",""+MemberId);
			pi.setValue(MemberId);//78846
			pi.setType(String.class);
			
			request.addProperty(pi);
			
			Utils.log(TAG+"member:",""+MemberId);
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
			 
				Utils.log(TAG+"Response :",""+transport.responseDump);
			 Utils.log(TAG+":","request :"+transport.requestDump);
			
			// Object response = envelope.getResponse();
			 
			 
			 Utils.log(TAG+"","URl :"+SOAP_URL);
			 Utils.log(TAG+"","METHOD :"+METHOD_NAME);
			
			 
			jsonResponse = envelope.getResponse().toString(); 
			
			return Result;
			
			
		}	
		catch(SocketException e){
			Utils.log("Socket","Exception"+e);
			Utils.log(TAG+"error",""+e);
			return "Timeout";
		}catch(SocketTimeoutException e){
				Utils.log("Timeout","Exception"+e);
				Utils.log(TAG+"error",""+e);
				return "Timeout";
			}
			
		catch(Exception e){
			
			e.printStackTrace();
		
		
		Utils.log(TAG+"error",""+e);
		return "Error";

		}
	}

	public String getjsonResponse() {
		// TODO Auto-generated method stub
		return jsonResponse;
	}


}
