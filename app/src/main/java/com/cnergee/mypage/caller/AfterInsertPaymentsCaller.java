package com.cnergee.mypage.caller;

import com.cnergee.mypage.MakeMyPayment_AggrePay;
import com.cnergee.mypage.MakeMyPayment_PayUActivity;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.MakeMyPayments_CCAvenue;
import com.cnergee.mypage.SOAP.AfterInsertPaymentSOAP;
import com.cnergee.mypage.WebView_AtomPayments;
import com.cnergee.mypage.WebView_CCAvenueActivity;
import com.cnergee.mypage.WebView_CCAvenue_TopUpActivity;
import com.cnergee.mypage.WebView_PayUMoneyActivity;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class AfterInsertPaymentsCaller extends Thread{
	
	public AfterInsertPaymentSOAP afterpaymentsoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String authIdCode;
	public String TxId;
	public String TxStatus;
	public String pgTxnNo;
	public String issuerRefNo;
	public String TxMsg;
	
	public PaymentsObj paymentdata;
	
	boolean is_makemypayments=true;
	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}

	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}
	
	public AfterInsertPaymentsCaller(){}
	
	public AfterInsertPaymentsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,boolean is_makemypayments) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.is_makemypayments=is_makemypayments;
	}
	
	public void run() {

		try {
			afterpaymentsoap = new AfterInsertPaymentSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			//ComplaintSoap.setAllData(isAllData());
			
			afterpaymentsoap.setPaymentdata(getPaymentdata());
			
			if(is_makemypayments){
				if(Utils.is_aggrepay){
					MakeMyPayment_AggrePay.rslt=afterpaymentsoap.CallComplaintNoSOAP();
					MakeMyPayment_AggrePay.responseMsg = afterpaymentsoap.getServerMessage();
				}
			}
			else{
				if(Utils.is_aggrepay){
					MakeMyPayment_AggrePay.rslt=afterpaymentsoap.CallComplaintNoSOAP();
					MakeMyPayment_AggrePay.responseMsg = afterpaymentsoap.getServerMessage();
				}
			}
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(is_makemypayments){
				if(Utils.is_CCAvenue)
				WebView_CCAvenueActivity.rslt = "Internet connection not available!!";
				else
				MakeMyPayments.rslt = "Internet connection not available!!";
			}
			else{
				if(Utils.is_CCAvenue)
					WebView_CCAvenue_TopUpActivity.rslt = "Internet connection not available!!";
					else
					MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
			}
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(is_makemypayments){
				if(Utils.is_CCAvenue)
					WebView_CCAvenueActivity.rslt = "Internet connection not available!!";
				else
					MakeMyPayments.rslt = "Internet connection not available!!";
			}
			else{
				if(Utils.is_CCAvenue)
					WebView_CCAvenue_TopUpActivity.rslt = "Internet connection not available!!";
				else
					MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
			}
		}catch (Exception e) {
			e.printStackTrace();
			if(is_makemypayments){
				if(Utils.is_CCAvenue)
					WebView_CCAvenueActivity.rslt = "Invalid web-service response.<br>"+e.toString();
				else
					MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			else{
				if(Utils.is_CCAvenue)
					WebView_CCAvenue_TopUpActivity.rslt = "Invalid web-service response.<br>"+e.toString();
				else
					MakeMyPaymentsTopUp.rslt = "Invalid web-service response.<br>"+e.toString();
			}
		}
	}

	public AfterInsertPaymentSOAP getAfterpaymentsoap() {
		return afterpaymentsoap;
	}

	public void setAfterpaymentsoap(AfterInsertPaymentSOAP afterpaymentsoap) {
		this.afterpaymentsoap = afterpaymentsoap;
	}

	public String getAuthIdCode() {
		return authIdCode;
	}

	public void setAuthIdCode(String authIdCode) {
		this.authIdCode = authIdCode;
	}

	public String getTxId() {
		return TxId;
	}

	public void setTxId(String txId) {
		TxId = txId;
	}

	public String getTxStatus() {
		return TxStatus;
	}

	public void setTxStatus(String txStatus) {
		TxStatus = txStatus;
	}

	public String getPgTxnNo() {
		return pgTxnNo;
	}

	public void setPgTxnNo(String pgTxnNo) {
		this.pgTxnNo = pgTxnNo;
	}

	public String getIssuerRefNo() {
		return issuerRefNo;
	}

	public void setIssuerRefNo(String issuerRefNo) {
		this.issuerRefNo = issuerRefNo;
	}

	public String getTxMsg() {
		return TxMsg;
	}

	public void setTxMsg(String txMsg) {
		TxMsg = txMsg;
	}

	
	
	

}
