package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.PaymentPickup_New;
import com.cnergee.mypage.SOAP.PaymentPickUpSOAP;
import com.cnergee.mypage.obj.PaymentPickUpObj;
import com.cnergee.mypage.utils.Utils;

public class PaymentPickUpCaller extends Thread {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private PaymentPickUpSOAP paymentpickup;
	private PaymentPickUpObj paymentpickupobj;
	
	
	public PaymentPickUpCaller(){}
	
	public PaymentPickUpCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		//Log.i(">>>>This is Test<<<<","My TEst");
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	
	public void run() {

		try {
			
			
			Utils.log("run","run Caller Exc");
			paymentpickup = new PaymentPickUpSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			paymentpickup.setPaymentPickupObj(getPaymentpickupobj());
			
			Utils.log("run","run Caller Exc");
			
			PaymentPickup_New.rslt = paymentpickup.CallCollectionSOAP();
			PaymentPickup_New.responseMsg = paymentpickup.getResponseMsg();
			
		}catch (SocketException e) {
			e.printStackTrace();
			PaymentPickup_New.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			PaymentPickup_New.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			PaymentPickup_New.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

	public PaymentPickUpObj getPaymentpickupobj() {
		return paymentpickupobj;
	}

	public void setPaymentpickupobj(PaymentPickUpObj paymentpickupobj) {
		this.paymentpickupobj = paymentpickupobj;
	}
	
	/*public void setPaymentPickupObj(PaymentPickUpObj paymentpickupObj) {
		this.paymentpickupobj = paymentpickupObj;
	}

	public PaymentPickUpObj getpaymentpickupobj() {
		return paymentpickupobj;
	}
		
		public long getMemberId() {
			return memberid;
		}

		public void setMemberId(long memberid) {
			this.memberid = memberid;
		}*/

		
	

}
