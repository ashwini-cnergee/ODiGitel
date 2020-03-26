package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.fragments.ExistingConnFragment;
import com.cnergee.mypage.Authentication;

import com.cnergee.mypage.SMSAuthenticationActivity;
import com.cnergee.mypage.SOAP.InsertPhoneDetailsSOAP;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;



public class InsertPhoneDetailsCaller extends Thread{
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private boolean isAllData;
	private InsertPhoneDetailsSOAP insertphonedetails;
	private PhoneDetailsOBJ phonedetailobj;
	
		public InsertPhoneDetailsCaller(){}
	
	public InsertPhoneDetailsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	
	public void run() {

		try {
			insertphonedetails = new InsertPhoneDetailsSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			//paymentpickup.setMemberId(memberid);
			
			insertphonedetails.setPhonedetailsObj(getPhonedetailobj());
			if(isAllData)
				ExistingConnFragment.rslt = insertphonedetails.CalComplaintSOAP();
			else
			Authentication.rslt = insertphonedetails.CalComplaintSOAP();
			//Complaints.responseMsg = insertcomplaint.getResponseMsg();
			//SMSAuthenticationActivity.rslt=insertphonedetails.CalComplaintSOAP();
		}catch (SocketException e) {
			e.printStackTrace();
			if(isAllData)
				ExistingConnFragment.rslt = "Internet connection not available!!";
			else
				Authentication.rslt = "Internet connection not available!!";	
			SMSAuthenticationActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(isAllData)
				ExistingConnFragment.rslt = "Internet connection not available!!";
			else
				Authentication.rslt = "Internet connection not available!!";
			SMSAuthenticationActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			if(isAllData)
				ExistingConnFragment.rslt = "Invalid web-service response.<br>"+e.toString();
			else
				Authentication.rslt = "Invalid web-service response.<br>"+e.toString();	
			SMSAuthenticationActivity.rslt = "Internet connection not available!!";
		}
		
	}

	public PhoneDetailsOBJ getPhonedetailobj() {
		return phonedetailobj;
	}

	public void setPhonedetailobj(PhoneDetailsOBJ phonedetailobj) {
		this.phonedetailobj = phonedetailobj;
	}

	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}

}
