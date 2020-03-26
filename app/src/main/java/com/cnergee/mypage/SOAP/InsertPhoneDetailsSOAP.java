package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.Utils;


public class InsertPhoneDetailsSOAP {
	

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private PhoneDetailsOBJ phonedetailsObj;
	
	public InsertPhoneDetailsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	

public String CalComplaintSOAP()throws SocketException,SocketTimeoutException,Exception {
		
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" MemberId ", getCollectionObj().getMemberLoginId());
		Log.i(" PayPickUpId ", getCollectionObj().getpaypickid());
		Log.i(" TypeName ", getCollectionObj().getTypeName());
		Log.i(" CallBackDate ", getCollectionObj().getCBDate());
		Log.i(" Comment ", getCollectionObj().getComment());
		Log.i(" UserName ",getUsername() );*/
	
		
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(getPhonedetailsObj().getMemberId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PhoneName");
		pi.setValue(getPhonedetailsObj().getPhoneName());
		pi.setType(String.class);
		request.addProperty(pi);
		
		
	
		pi = new PropertyInfo();
		pi.setName("PhoneVersion");
		pi.setValue(getPhonedetailsObj().getPhoneVersion());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PhoneUniqueId");
		pi.setValue(getPhonedetailsObj().getPhoneUniqueId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Phoneplatform");
		pi.setValue(getPhonedetailsObj().getPhoneplatform());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("phonepackage");
		pi.setValue(getPhonedetailsObj().getPhonepackage());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PhoneNumber");
		pi.setValue(getPhonedetailsObj().getPhoneNumber());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("AppVersion");
		pi.setValue(getPhonedetailsObj().getAppVersion());
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
			Utils.log("Insert Call Confirmation",":"+request.toString());
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				/*setResponseMsg(response.toString());*/
				//Log.i(" >>>> " ,response.toString());
				Utils.log("Insert Call Confirmation",":"+response.toString());
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
		
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
