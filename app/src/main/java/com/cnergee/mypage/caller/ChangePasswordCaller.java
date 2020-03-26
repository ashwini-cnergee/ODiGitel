package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.SOAP.ReleaseMacSOAP;

public class ChangePasswordCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String MemberId,MemberLoginId,MobileNumber;
	
	
	ReleaseMacSOAP releaseMacSOAP;
	
	public ChangePasswordCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
public void run() {
		
		try{
			releaseMacSOAP = new ReleaseMacSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			Complaints.rslt = releaseMacSOAP.releaseMac(MemberId,MemberLoginId,MobileNumber);
			Complaints.statusResponse=releaseMacSOAP.getResponse();
			//Utils.log("Complaint status caller",""+Complaints.statusResponse);
			//Utils.log("Complaint status Result",""+Complaints.statusRslt);
		}catch (SocketException e) {
			e.printStackTrace();
			Complaints.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			Complaints.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			Complaints.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}
	public void setMemberId(String MemberId){
	this.MemberId=MemberId;
	}
	
	public void setMemberLoginId(String MemberLoginId){
		this.MemberLoginId=MemberLoginId;
	}
	public void setMobileNumber(String MobileNumber){
		this.MobileNumber=MobileNumber;
	}
	
}
