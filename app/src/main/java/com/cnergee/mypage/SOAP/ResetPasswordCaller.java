package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.SelfResolution;

public class ResetPasswordCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String MemberId,MemberLoginId;
	
	private String action="";
	ResetPasswordSOAP resetPasswordSOAP;
	
	public ResetPasswordCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,String action) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.action=action;
	}
public void run() {
		
		try{
			resetPasswordSOAP = new ResetPasswordSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			if(action.equalsIgnoreCase("self")){
			SelfResolution.rslt = resetPasswordSOAP.resetPassword(MemberId,MemberLoginId);
			SelfResolution.statusResponseForPwd=resetPasswordSOAP.getResponse();
			}
			if(action.equalsIgnoreCase("complaints")){
				Complaints.rslt = resetPasswordSOAP.resetPassword(MemberId,MemberLoginId);
				Complaints.statusResponseForPwd=resetPasswordSOAP.getResponse();
				}
			//Utils.log("ResetPasswordCaller status caller",""+Complaints.statusResponse);
			//Utils.log("ResetPasswordCaller status Result",""+Complaints.statusRslt);
		}catch (SocketException e) {
			e.printStackTrace();
			if(action.equalsIgnoreCase("self")){
			SelfResolution.rslt = "Internet connection not available!!";
			}
			if(action.equalsIgnoreCase("complaints")){
				Complaints.rslt = "Internet connection not available!!";
				}
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(action.equalsIgnoreCase("self")){
			SelfResolution.rslt = "Internet connection not available!!";
			}
			if(action.equalsIgnoreCase("complaints")){
				Complaints.rslt = "Internet connection not available!!";
			}
		}catch (Exception e) {
			if(action.equalsIgnoreCase("self"))
			SelfResolution.rslt = "Invalid web-service response.<br>"+e.toString();
			if(action.equalsIgnoreCase("complaints"))
				Complaints.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}
	public void setMemberId(String MemberId){
	this.MemberId=MemberId;
	}
	
	public void setMemberLoginId(String MemberLoginId){
		this.MemberLoginId=MemberLoginId;
	}
}
