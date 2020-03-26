package com.cnergee.mypage.caller;

import com.cnergee.mypage.MakeMyPayment_AggrePay;
import com.cnergee.mypage.MakeMyPayment_Atom;
import com.cnergee.mypage.MakeMyPayment_PayUActivity;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.MakeMyPaymentsTopUp_CCAvenue;
import com.cnergee.mypage.MakeMyPayments_CCAvenue;
import com.cnergee.mypage.SOAP.BeforeInsertPaymentSOAP;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class BeforePaymentInsertCaller extends Thread{
	public BeforeInsertPaymentSOAP beforepaymentsoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	public String TrackId;
	public String Amount;
	public String PackageName;
	public String ServiceTax;
	public PaymentsObj paymentdata;
	boolean ismakemypayments;
	

	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}

	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}

	public BeforePaymentInsertCaller(){}
	
	public BeforePaymentInsertCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL, String METHOD_NAME,boolean ismakemypayments) {

		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.ismakemypayments=ismakemypayments;
	}
	
	public void run() {

		try {
			beforepaymentsoap = new BeforeInsertPaymentSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			//ComplaintSoap.setAllData(isAllData());
			beforepaymentsoap.setPaymentdata(getPaymentdata());

			/*beforepaymentsoap.setMemberid(getMemberid());
			beforepaymentsoap.setTrackId(getTrackId());
			beforepaymentsoap.setAmount(getAmount());
			beforepaymentsoap.setPackageName(getPackageName());
			beforepaymentsoap.setServiceTax(getServiceTax());
			*/
			if(ismakemypayments){
			/*	if(Utils.is_CCAvenue){
				MakeMyPayments_CCAvenue.rslt = beforepaymentsoap.CallComplaintNoSOAP(
						);
				MakeMyPayments_CCAvenue.responseMsg = beforepaymentsoap
						.getServerMessage();
				}
				else{
					MakeMyPayments.rslt = beforepaymentsoap.CallComplaintNoSOAP(
							);
					MakeMyPayments.responseMsg = beforepaymentsoap
							.getServerMessage();
				}*/

			/*	if(Utils.is_atom){
					MakeMyPayment_Atom.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayment_Atom.responseMsg = beforepaymentsoap.getServerMessage();
				}else if(Utils.is_CCAvenue){
					MakeMyPayments_CCAvenue.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments_CCAvenue.responseMsg = beforepaymentsoap.getServerMessage();
				}else{
					MakeMyPayments.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments.responseMsg = beforepaymentsoap.getServerMessage();
				}*/

				if(Utils.is_aggrepay){
					MakeMyPayment_AggrePay.rslt=beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments_CCAvenue.responseMsg = beforepaymentsoap.getServerMessage();
				}
			}
			else{
				if(Utils.is_aggrepay){
					MakeMyPayment_AggrePay.rslt=beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments_CCAvenue.responseMsg = beforepaymentsoap.getServerMessage();
				}

				/*if(Utils.is_CCAvenue){
					MakeMyPaymentsTopUp_CCAvenue.rslt = beforepaymentsoap.CallComplaintNoSOAP(
							);
					MakeMyPaymentsTopUp_CCAvenue.responseMsg = beforepaymentsoap
							.getServerMessage();
				}
				else{
				MakeMyPaymentsTopUp.rslt = beforepaymentsoap.CallComplaintNoSOAP(
						);
				MakeMyPaymentsTopUp.responseMsg = beforepaymentsoap
						.getServerMessage();
				}*/

			}/*	if(Utils.is_atom){
					MakeMyPayment_Atom.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayment_Atom.responseMsg = beforepaymentsoap.getServerMessage();
				}else if(Utils.is_CCAvenue){
					MakeMyPayments_CCAvenue.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments_CCAvenue.responseMsg = beforepaymentsoap.getServerMessage();
				}else{
					MakeMyPayments.rslt = beforepaymentsoap.CallComplaintNoSOAP();
					MakeMyPayments.responseMsg = beforepaymentsoap.getServerMessage();
				}*/
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(ismakemypayments) {
				/*if(Utils.is_CCAvenue)
					MakeMyPayments_CCAvenue.rslt = "Internet connection not available!!";
				else
					MakeMyPayments.rslt = "Internet connection not available!!";
				
			}
			else{				
				if(Utils.is_CCAvenue)
					MakeMyPaymentsTopUp_CCAvenue.rslt = "Internet connection not available!!";
				else
					MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
			}*/
				if (Utils.is_PayUmoney) {
					MakeMyPayment_PayUActivity.rslt = "Internet connection not available!!";
				}
			}
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(ismakemypayments){
				/*if(Utils.is_CCAvenue)
					MakeMyPayments_CCAvenue.rslt = "Internet connection not available!!";
				else
					MakeMyPayments.rslt = "Internet connection not available!!";
			}
			else{
				if(Utils.is_CCAvenue)
					MakeMyPaymentsTopUp_CCAvenue.rslt = "Internet connection not available!!";
				else
					MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";*/
				if (Utils.is_PayUmoney) {
					MakeMyPayment_PayUActivity.rslt = "Internet connection not available!!";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			if(ismakemypayments){
				if (Utils.is_PayUmoney) {
					MakeMyPayment_PayUActivity.rslt = "Internet connection not available!!";
				}

				/*if(Utils.is_CCAvenue)
					MakeMyPayments_CCAvenue.rslt = "Invalid web-service response.<br>"+e.toString();
				else
					MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			else{
				if(Utils.is_CCAvenue)
					MakeMyPaymentsTopUp_CCAvenue.rslt = "Invalid web-service response.<br>"+e.toString();
				else
					MakeMyPaymentsTopUp.rslt = "Invalid web-service response.<br>"+e.toString();*/
			}
		}
	}

	public BeforeInsertPaymentSOAP getBeforepaymentsoap() {
		return beforepaymentsoap;
	}

	public void setBeforepaymentsoap(BeforeInsertPaymentSOAP beforepaymentsoap) {
		this.beforepaymentsoap = beforepaymentsoap;
	}

	public long getMemberid() {
		return memberid;
	}

	public void setMemberid(long memberid) {
		this.memberid = memberid;
	}

	public String getTrackId() {
		return TrackId;
	}

	public void setTrackId(String trackId) {
		TrackId = trackId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getPackageName() {
		return PackageName;
	}

	public void setPackageName(String packageName) {
		PackageName = packageName;
	}

	public String getServiceTax() {
		return ServiceTax;
	}

	public void setServiceTax(String serviceTax) {
		ServiceTax = serviceTax;
	}
	
	

}
