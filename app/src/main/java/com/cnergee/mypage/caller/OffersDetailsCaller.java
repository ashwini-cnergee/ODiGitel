package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.SpecialOfferActivity;
import com.cnergee.mypage.SOAP.OfferDetailsSOAP;

public class OffersDetailsCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String Memberid;
	private OfferDetailsSOAP offerDetailsSoap;

	public OffersDetailsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		offerDetailsSoap= new OfferDetailsSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
		try {
			SpecialOfferActivity.rslt=offerDetailsSoap.getOffers(getMemberId());
			SpecialOfferActivity.alOffersLink=offerDetailsSoap.getOfferLinksArrayList();
			SpecialOfferActivity.alOffersName=offerDetailsSoap.getOfferNamesArrayList();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			SpecialOfferActivity.rslt="Please try again later!!!";
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			SpecialOfferActivity.rslt="Please try again later!!!";
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SpecialOfferActivity.rslt="Please try again later!!!";
			e.printStackTrace();
		}
	}
	
	public String getMemberId(){
		return Memberid;
	}
	
	public void setMemberId(String MemberId){
		this.Memberid=MemberId;
	}
}
