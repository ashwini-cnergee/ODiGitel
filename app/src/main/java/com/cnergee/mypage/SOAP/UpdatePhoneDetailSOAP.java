package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.Utils;

public class UpdatePhoneDetailSOAP {


	

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private PhoneDetailsOBJ phonedetailsObj;
	
	public UpdatePhoneDetailSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	

public String updateDetails(String MemberId,String PhoneUniqueId,String DeviceId,String AppVersion)throws SocketException,SocketTimeoutException,Exception {
		
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("PhoneUniqueId");
		pi.setValue(PhoneUniqueId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		
		pi = new PropertyInfo();
		pi.setName("DeviceId");
		pi.setValue(DeviceId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AppVersion");
		pi.setValue(AppVersion);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);


	 Utils.log("UpdateDetailsSOAP",":"+request.toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<<", request.toString());
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);

			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}



	public PhoneDetailsOBJ getPhonedetailsObj() {
		return phonedetailsObj;
	}



	public void setPhonedetailsObj(PhoneDetailsOBJ phonedetailsObj) {
		this.phonedetailsObj = phonedetailsObj;
	}



	
	
	
	
	

}
