package com.cnergee.mypage.caller;

import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.SOAP.CitrusSignatureSOAP;

public class CitrusSignatureCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private CitrusSignatureSOAP citrussignaturesoap;
	private long MemberId;
	private String Amount;
	private String TrackId;
	boolean is_makemypayments=true;
	public CitrusSignatureCaller() {
	}

	public CitrusSignatureCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,boolean is_makemypayments) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.is_makemypayments=is_makemypayments;
	}

	public void run() {

		try {
			citrussignaturesoap = new CitrusSignatureSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			citrussignaturesoap.setAmount(getAmount());
			citrussignaturesoap.setMemberId(getMemberId());
			citrussignaturesoap.setTrackId(getTrackId());
			if(is_makemypayments){
			MakeMyPayments.rslt = citrussignaturesoap
					.CallCitrusSignatureSOAP();
			MakeMyPayments.HMACSignature = citrussignaturesoap
					.getServerMessage();
			}
			else{
				MakeMyPaymentsTopUp.rslt = citrussignaturesoap
						.CallCitrusSignatureSOAP();
				MakeMyPaymentsTopUp.HMACSignature = citrussignaturesoap
						.getServerMessage();
			}

		} catch (Exception e) {
			MakeMyPayments.rslt = e.toString();
		}
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getTrackId() {
		return TrackId;
	}

	public void setTrackId(String trackId) {
		TrackId = trackId;
	}

	

}
