package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;


import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;

import com.cnergee.mypage.SOAP.RenewalSOAP;
import com.cnergee.mypage.SOAP.RenewalSOAPTopup;
import com.cnergee.mypage.obj.PaymentsObj;

public class RenewalCallerTopup extends Thread{
	public RenewalSOAPTopup renewalsoap;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	public String TrackId;
	public String Amount;
	public String PackageName;
	public String ServiceTax;
	public PaymentsObj paymentdata;
	
	

	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}

	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}

	public RenewalCallerTopup(){}
	
	public RenewalCallerTopup(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			renewalsoap = new RenewalSOAPTopup(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			//ComplaintSoap.setAllData(isAllData());
			renewalsoap.setPaymentdata(getPaymentdata());

			
			
			/*beforepaymentsoap.setMemberid(getMemberid());
			beforepaymentsoap.setTrackId(getTrackId());
			beforepaymentsoap.setAmount(getAmount());
			beforepaymentsoap.setPackageName(getPackageName());
			beforepaymentsoap.setServiceTax(getServiceTax());
			*/
				MakeMyPaymentsTopUp.rslt = renewalsoap.CallComplaintNoSOAP(
						);
				MakeMyPaymentsTopUp.responseMsg = renewalsoap
						.getServerMessage();
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			e.printStackTrace();
			
				MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

}
