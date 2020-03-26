package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;



import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.SelfResolution;
import com.cnergee.mypage.SOAP.LogOutSOAP;


public class LogOutCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String MemberId,MemberLoginId,MobileNumber;
	
	
	LogOutSOAP logOutSOAP;
	boolean is_24_ol;
	
	public LogOutCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,boolean is_24_ol) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.is_24_ol=is_24_ol;
	}
public void run() {		
		try{
			logOutSOAP = new LogOutSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			if(is_24_ol){
				Complaints.rsltLogOut = logOutSOAP.logOut(MemberId,MemberLoginId);
				Complaints.statusResponseForLogOut=logOutSOAP.getResponse();
			}
			else{
			SelfResolution.rsltLogOut = logOutSOAP.logOut(MemberId,MemberLoginId);
			SelfResolution.statusResponseForLogOut=logOutSOAP.getResponse();
			}
			//Utils.log("Complaint Logout caller",""+Complaints.statusResponseForLogOut);
			//Utils.log("Complaint Logout Result",""+Complaints.rsltLogOut);
		}catch (SocketException e) {
			e.printStackTrace();
			SelfResolution.rsltLogOut = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			SelfResolution.rsltLogOut = "Internet connection not available!!";
		}catch (Exception e) {
			SelfResolution.rsltLogOut = "Invalid web-service response.<br>"+e.toString();
		}
		
	}
	public void setMemberId(String MemberId){
	this.MemberId=MemberId;
	}
	
	public void setMemberLoginId(String MemberLoginId){
		this.MemberLoginId=MemberLoginId;
	}
	
	
}
