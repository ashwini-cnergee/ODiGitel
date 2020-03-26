package com.cnergee.mypage.caller;

import com.cnergee.mypage.MakeMyPayment_AggrePay;
import com.cnergee.mypage.MakeMyPayment_PayUActivity;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.SOAP.RenewalSOAP;
import com.cnergee.mypage.WebView_AtomPayments;
import com.cnergee.mypage.WebView_CCAvenueActivity;
import com.cnergee.mypage.WebView_PayUMoneyActivity;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RenewalCaller extends Thread{
	public RenewalSOAP renewalsoap;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	public String TrackId;
	public String Amount;
	public String PackageName;
	public String ServiceTax;
	public PaymentsObj paymentdata;
	private String RenewalType;
	

	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}

	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}

	public RenewalCaller(){}
	
	public RenewalCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			renewalsoap = new RenewalSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			//ComplaintSoap.setAllData(isAllData());
			renewalsoap.setPaymentdata(getPaymentdata());

			renewalsoap.setRenewalType(getRenewalType());

			if(Utils.is_aggrepay){
				MakeMyPayment_AggrePay.rslt = renewalsoap.CallComplaintNoSOAP();
				MakeMyPayment_AggrePay.responseMsg = renewalsoap.getServerMessage();
			}

		}catch (SocketException e) {
			e.printStackTrace();
			if(Utils.is_aggrepay)
				MakeMyPayment_AggrePay.rslt = "Internet connection not available!!";
			else
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(Utils.is_aggrepay)
				MakeMyPayment_AggrePay.rslt = "Internet connection not available!!";
			else
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			e.printStackTrace();
			if(Utils.is_PayUmoney)
				WebView_PayUMoneyActivity.rslt = "Invalid web-service response.<br>"+e.toString();
			else
				MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

	public String getRenewalType() {
		return RenewalType;
	}

	public void setRenewalType(String renewalType) {
		RenewalType = renewalType;
	}

}
