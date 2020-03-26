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

public class PaymentDetailsSOAP {
	
	private String PaymentDate;
	private long Memberid;
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String paymentDetails;
	
	public PaymentDetailsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
public String CallTodaysCollectionSOAP()throws SocketException,SocketTimeoutException,Exception {
		
		
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);

		Log.i("#####################", "");*/
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(getMemberid());
		pi.setType(long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaymentDateTime");
		pi.setValue(getPaymentDate());
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
		Utils.log(">>>>>Request<<<<<", request.toString());
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log("PaymentDetailsSOAP ","request:"+androidHttpTransport.requestDump);
			Utils.log("PaymentDetailsSOAP ","response:"+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				//Object response = envelope.getResponse();
				SoapObject response = (SoapObject) envelope.getResponse();
				
				if(response != null){
					
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						
						StringBuffer sb = new StringBuffer();
							
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response	.getProperty(i);
							sb.append("<br>");
							sb.append("Plan : ");
							sb.append(tableObj.getPropertyAsString("PackageFullName").toString());
							sb.append("<br>");
							sb.append("Paid on : ");
							sb.append(tableObj.getPropertyAsString("PaymentDate").toString());
							sb.append("<br>");
							sb.append("Mode : ");
							sb.append(tableObj.getPropertyAsString("Mode").toString());
							sb.append("<br>");
							sb.append("Amount : ");
							sb.append(tableObj.getPropertyAsString("Amount").toString());
							sb.append("<br>");
							sb.append("Renewed on : ");
							sb.append(tableObj.getPropertyAsString("RenewalDate").toString());
							sb.append("<br>");
							sb.append(" ");
							
						}
						
						setPaymentDetails(sb.toString());
						sb = null;
					
					} else {
						setPaymentDetails(" No Data Found.");
					}
				}
				else
				{
					setPaymentDetails("Nil");
				}
				
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault
						.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
		
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}



public String getPaymentDate() {
	return PaymentDate;
}

public void setPaymentDate(String paymentDate) {
	PaymentDate = paymentDate;
}

public long getMemberid() {
	return Memberid;
}

public void setMemberid(long memberid) {
	Memberid = memberid;
}

public String getPaymentDetails() {
	return paymentDetails;
}

public void setPaymentDetails(String paymentDetails) {
	this.paymentDetails = paymentDetails;
}


	
	

}
