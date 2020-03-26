package com.cnergee.mypage.SOAP;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PaymentPickUpObj;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PaymentPickUpSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private PaymentPickUpObj paymentpickupobj;
	private String responseMsg;
	
	
	public PaymentPickUpSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	
public String CallCollectionSOAP()throws SocketException,SocketTimeoutException,Exception {
		
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
	
			//Log.i("#####################", "");
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(getpaymentpickupobj().getMemberId());
		pi.setType(long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("Visitdatetime");
		pi.setValue(getpaymentpickupobj().getVisitDateTime());
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("Message");
		pi.setValue(getpaymentpickupobj().getMessage());
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
		//Log.i(">>>>REquest>>>>>" , request.toString());
	Utils.log("Payment Pickup request",":"+request.toString());

		envelope.setOutputSoapObject(request);
		
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
			Utils.log("Request",":"+androidHttpTransport.requestDump);
			Utils.log("Response",""+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				setResponseMsg(response.toString());
				//Log.i(" >>>> " ,response.toString());
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

public void setPaymentPickupObj(PaymentPickUpObj paymentpickupObj) {
	this.paymentpickupobj = paymentpickupObj;
}

public PaymentPickUpObj getpaymentpickupobj() {
	return paymentpickupobj;
}
	
	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}
