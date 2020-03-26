package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.Profile;
import com.cnergee.mypage.SOAP.ComplaintSOAP;


public class ComplaintCaller extends Thread{
	
	public ComplaintSOAP ComplaintSoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String memberid;
	private boolean isAllData;
	
	public ComplaintCaller(){}
	
	public ComplaintCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			ComplaintSoap = new ComplaintSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			//ComplaintSoap.setAllData(isAllData());
			
				ComplaintSoap.setMemberid(getMemberid());
				Complaints.rslt = ComplaintSoap.CallComplaintNoSOAP(
						memberid);
				Complaints.complaintno = ComplaintSoap
						.getServerMessage();
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Internet connection not available!!";
			else
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Internet connection not available!!";
			else
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Invalid web-service response.<br>"+e.toString();
			else
				MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	
	

}
