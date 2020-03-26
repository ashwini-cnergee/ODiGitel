package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;


import com.cnergee.fragments.ExistingConnFragment;
import com.cnergee.mypage.Authentication;
import com.cnergee.mypage.Confirmation;

import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.SOAP.ConfirmationSOAP;

public class ConfirmationCallerAuthentication extends Thread{
	
	public ConfirmationSOAP ConfirmationSoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String MemberLoginId;
	public String Mobilenumber;
	private boolean isAllData;
	
	public ConfirmationCallerAuthentication(){}
	
	public ConfirmationCallerAuthentication(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			ConfirmationSoap = new ConfirmationSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			ConfirmationSoap.setAllData(isAllData());
			
			
				if(isAllData){
				Authentication.rslt = ConfirmationSoap.CallSearchMemberSOAP(
						MemberLoginId,Mobilenumber);
				Authentication.mapConfirmationDetails = ConfirmationSoap
						.getMapconfirmationDetails();
				}
				else{
					ExistingConnFragment.rslt = ConfirmationSoap.CallSearchMemberSOAP(
							MemberLoginId,Mobilenumber);
					ExistingConnFragment.mapConfirmationDetails = ConfirmationSoap
							.getMapconfirmationDetails();
					/*Authentication.rslt = ConfirmationSoap.CallSearchMemberSOAP(
							MemberLoginId,Mobilenumber);
					Authentication.mapConfirmationDetails = ConfirmationSoap
							.getMapconfirmationDetails();*/
				}
				
			
		
			
		}catch (SocketException e) {
			e.printStackTrace();
			
				Confirmation.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			
				Confirmation.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			e.printStackTrace();
			Confirmation.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}
	

}
