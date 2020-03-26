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

public class GetUsernamePwdSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "GetUsernamePwdSOAP"; 
	private String Username;
	private String Password;
	
	public GetUsernamePwdSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getUsernamePwd(String MemberId)throws SocketException,SocketTimeoutException,Exception,SoapFault{
		String str_msg = "ok";
		try{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(Long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
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
		/*Utils.log(TAG ,"request: "+androidHttpTransport.requestDump);
		Utils.log(TAG,"response :"+androidHttpTransport.responseDump);*/
		str_msg="ok";
		if (envelope.bodyIn instanceof SoapObject) {
			try{
			// SoapObject = SUCCESS
			SoapObject response = (SoapObject) envelope.getResponse();
			if (response != null) {
				//Log.i(" RESPONSE ", response.toString());
				response = (SoapObject) response.getProperty("NewDataSet");
				if (response.getPropertyCount() > 0) {
					for (int i = 0; i < response.getPropertyCount(); i++) 
					{
						SoapObject tableObj = (SoapObject) response.getProperty(i);
						Username=tableObj.getPropertyAsString("MemberLoginID");
						Password=tableObj.getPropertyAsString("LoginPassword");	
						/*Log.i(" Username ", tableObj.getPropertyAsString("MemberLoginID"));
						Log.i(" Password ", tableObj.getPropertyAsString("LoginPassword"));*/
					}
					
				} 
			}
			}catch (Exception e) {
				// TODO: handle exception
				return str_msg="not";
			}
			}
		
		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
			// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			return str_msg="not";
		}
		//Log.i(" Message ", "s "+str_msg);
		return str_msg;
		}
		catch (Exception e) {
			str_msg="not";
			return str_msg;
		}
		
	}
	public String getUsername(){
		return Username;
	}
	
	public String getPassword(){
		return Password;
	}
	
}
