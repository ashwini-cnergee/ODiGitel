package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import com.cnergee.mypage.PaymentHistory;
import com.cnergee.mypage.SOAP.PaymentDetailsSOAP;

public class PaymentDetailsCaller extends Thread{
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String PaymentDate;
	private long Memberid;
	
	public PaymentDetailsSOAP paymentCollectionSOAP;
	
	public PaymentDetailsCaller() {
	}

	public PaymentDetailsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			paymentCollectionSOAP = new PaymentDetailsSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			paymentCollectionSOAP.setPaymentDate(getPaymentDate());
			paymentCollectionSOAP.setMemberid(getMemberid());
			PaymentHistory.rslt = paymentCollectionSOAP.CallTodaysCollectionSOAP();
			PaymentHistory.paymentDetails = paymentCollectionSOAP.getPaymentDetails();
		}catch (SocketException e) {
			e.printStackTrace();
			PaymentHistory.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			PaymentHistory.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			PaymentHistory.rslt = "Invalid web-service response.<br>"+e.toString();
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
	
	
}
