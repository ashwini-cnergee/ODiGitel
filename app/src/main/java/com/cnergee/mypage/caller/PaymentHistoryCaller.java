package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.PaymentHistory;
import com.cnergee.mypage.SOAP.PaymentHistorySOAP;


public class PaymentHistoryCaller extends Thread{
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private long memberid;
	private PaymentHistorySOAP paymentSoap;
	
	public PaymentHistoryCaller(){}
	
	public PaymentHistoryCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
public void run() {
		
		try{
			paymentSoap = new PaymentHistorySOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			PaymentHistory.rslt = paymentSoap.setComplaintList(getMemberid());
			PaymentHistory.paymentlist = paymentSoap.getPaymentList();
			
		}catch (SocketException e) {
			e.printStackTrace();
			PaymentHistory.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			PaymentHistory.rslt = "error";
		}catch (Exception e) {
			PaymentHistory.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

public long getMemberid() {
	return memberid;
}

public void setMemberid(long memberid) {
	this.memberid = memberid;
}

}
