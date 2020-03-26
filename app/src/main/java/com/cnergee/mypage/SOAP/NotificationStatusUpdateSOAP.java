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
import com.cnergee.mypage.obj.NotificationUpdateStatusObj;




public class NotificationStatusUpdateSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "myPageService"; 
	private String responseMessage;
	
	public NotificationStatusUpdateSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
public String updateComplaintStatus(NotificationUpdateStatusObj updateObj)throws SocketException,SocketTimeoutException,Exception{
		
		SoapObject request = new SoapObject("http://tempuri.org/", METHOD_NAME);
				
		//Log.i(" #	#####################  ", WSDL_TARGET_NAMESPACE);
		//Log.i(" #	#####################  ", SOAP_URL);
		//Log.i(" #	#####################  ", METHOD_NAME);
		
				
		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(updateObj.getMemberid());
		pi.setType(Long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("NotifyId");
		pi.setValue(updateObj.getNotifyid());
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
		
		//Log.i("----------------Response-----------------", request.toString());
		
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE("http://114.79.129.12:8001/CCRMToMobileIntegration.asmx?wsdl");
		androidHttpTransport.debug = true;

		String str_msg = "ok";
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			
			Object response = envelope.getResponse();
			if (response != null) {
				//Log.i(" RESPONSE ", response.toString());
				setResponseMessage(response.toString());
			
			} else {
				str_msg = "ok";
			}

		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			str_msg = "failed";
		}
		return str_msg;
	}
	
	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	

}
