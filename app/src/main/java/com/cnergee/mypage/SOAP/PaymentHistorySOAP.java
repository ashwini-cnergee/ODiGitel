package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PaymentHistoryObj;
import com.cnergee.mypage.utils.Utils;


public class PaymentHistorySOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "MyPage"; 
	private ArrayList<PaymentHistoryObj> paymentList = new ArrayList<PaymentHistoryObj>();
	
	
	public PaymentHistorySOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String setComplaintList(long memberid)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		/*Log.i(" #	#####################  ", " START ");
		Log.i(TAG+" userId ", ""+memberid);
		Log.i(TAG+" IMEI No ", Authobj.getIMEINo());
		Log.i(TAG+" Mobile ", Authobj.getMobileNumber());
		Log.i(TAG+" Mobile User ", Authobj.getMobLoginId());
		Log.i(TAG+" Mobile Password ", Authobj.getMobUserPass());
		Log.i(TAG+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(TAG+" METHOD_NAME ", METHOD_NAME);
		Log.i(TAG+" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("#####################", "");*/
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(memberid);
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
		
		String str_msg = "ok";
		ArrayList<PaymentHistoryObj> paymentlist = new ArrayList<PaymentHistoryObj>();
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		Utils.log(this.getClass().getSimpleName(), "Request"+androidHttpTransport.requestDump);
		
		Utils.log(this.getClass().getSimpleName(),"Response"+ androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				
				SoapObject response = (SoapObject) envelope.getResponse();
				
				if (response != null) {
					
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
							PaymentHistoryObj obj = new PaymentHistoryObj();
							
							obj.setPaymentDate(tableObj.getPropertyAsString("PaymentDate").toString());
							obj.setAmount(tableObj.getPropertyAsString("Amount").toString());
							
							/*obj.setComplaintId(tableObj.getPropertyAsString("Comptid").toString());
							obj.setComplaintNo(tableObj.getPropertyAsString("ComplaintNo").toString());
							obj.setPush(Boolean.parseBoolean(tableObj.getPropertyAsString("IsPush").toString()));
							obj.setRead(Boolean.parseBoolean(tableObj.getPropertyAsString("IsRead").toString()));
							obj.setSclosed(Boolean.parseBoolean(tableObj.getPropertyAsString("IsClosed").toString()));
							obj.setUpdated(Boolean.parseBoolean(tableObj.getPropertyAsString("IsUpdated").toString()));
							obj.SetMemberLoginId(tableObj.getPropertyAsString("MemberLoginID").toString());
							obj.setUserId(userId);*/
							
							paymentlist.add(obj);
							
							
						}
						
					} 
				}

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				paymentlist.add(new PaymentHistoryObj());
			}
			setPaymentList(paymentlist);
		return str_msg;
	}


	public ArrayList<PaymentHistoryObj> getPaymentList() {
		return paymentList;
	}


	public void setPaymentList(ArrayList<PaymentHistoryObj> paymentList) {
		this.paymentList = paymentList;
	}

	
	
	

}
