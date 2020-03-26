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

public class ChatRequestSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "ChatRequestSOAP"; 

	
	public ChatRequestSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String sendRequest(String MemberId,String Gcm_Reg_Id)throws SocketException,SocketTimeoutException,Exception{
		String str_msg = "ok";
		
		try{
			//Utils.log(TAG+"id",": "+MemberId);
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("DeviceId");
		pi.setValue(Gcm_Reg_Id);
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
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			try{
			SoapObject response = (SoapObject) envelope.getResponse();
			
			if (response != null) {
				
				response = (SoapObject) response.getProperty("NewDataSet");
				if (response.getPropertyCount() > 0) {
					for (int i = 0; i < response.getPropertyCount(); i++) {
						SoapObject tableObj = (SoapObject) response
								.getProperty(i);
						Utils.log("Soap Object", "is:"+tableObj.toString());
						if(tableObj.hasProperty("UserName")){
							Utils.FTP_USERNAME=tableObj.getPropertyAsString("UserName").toString();
						}
						if(tableObj.hasProperty("Password")){
							Utils.FTP_PASSWORD=tableObj.getPropertyAsString("Password").toString();
						}
						if(tableObj.hasProperty("address")){
							Utils.FTP_IP=tableObj.getPropertyAsString("address").toString();
						}
						 Utils.log("FTP IP ", this.getClass().getSimpleName()+":"+Utils.FTP_IP);
						  Utils.log("FTP_USERNAME ",this.getClass().getSimpleName()+":"+Utils.FTP_USERNAME);
						  Utils.log("FTP_PASSWORD ",this.getClass().getSimpleName()+":"+Utils.FTP_PASSWORD);
					}
					
					str_msg = "ok";
				} else {
					str_msg = "not";
				}
			}
			}
			catch(Exception e){
				//Utils.log("Soap Exception 94 ","is:"+e);
				str_msg="not";
				return str_msg;
			}
		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			
		
		}
		
		
		return str_msg;
		}
		catch (Exception e) {
			str_msg="not";
			return str_msg;
		}
	}
	

}
