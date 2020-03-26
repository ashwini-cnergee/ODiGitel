package com.cnergee.mypage.caller;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.fragments.ExistingConnFragment;
import com.cnergee.mypage.ChangePackage;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.MainActivity;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.PaymentPickup_New;
import com.cnergee.mypage.SMSAuthenticationActivity;
import com.cnergee.mypage.SOAP.SMSAuthenticationSOAP;
import com.cnergee.mypage.utils.Utils;

public class SMSAuthenticationCaller extends Thread {
public SMSAuthenticationSOAP SmsSOAP;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String MemberId;
	public String OneTimePwd;
	public String PhoneUniqueId;
	public boolean isAllData;
	public String checkCall;
	public SMSAuthenticationCaller() {
	}

	public SMSAuthenticationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		
		

	}

	public void run() {
		
		try {
			Utils.log("Sms Soap  ","Executed");
			SmsSOAP = new SMSAuthenticationSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			if (checkCall.equalsIgnoreCase("splash")) {
				MainActivity.rslt = SmsSOAP.CallSMSSOAP(MemberId, OneTimePwd,
						PhoneUniqueId);
				MainActivity.isVaildUser = SmsSOAP.isValidUser();
			}
			if (checkCall.equalsIgnoreCase("sms")) {
				SMSAuthenticationActivity.rslt = SmsSOAP.CallSMSSOAP(MemberId,
						OneTimePwd, PhoneUniqueId);
				SMSAuthenticationActivity.isVaildUser = SmsSOAP.isValidUser();
			}

			if (checkCall.equalsIgnoreCase("complaints")) {
				Complaints.rslt = SmsSOAP.CallSMSSOAP(MemberId, OneTimePwd,
						PhoneUniqueId);
				Complaints.isVaildUser = SmsSOAP.isValidUser();
			}
			
			if (checkCall.equalsIgnoreCase("pickup")) {
				PaymentPickup_New.rslt = SmsSOAP.CallSMSSOAP(MemberId, OneTimePwd,
						PhoneUniqueId);
				PaymentPickup_New.isVaildUser = SmsSOAP.isValidUser();
			}
			 
			if(checkCall.equalsIgnoreCase("changepack")){
				PackgedetailActivity.rslt = SmsSOAP.CallSMSSOAP(MemberId, OneTimePwd,
						PhoneUniqueId);
				PackgedetailActivity.isVaildUser = SmsSOAP.isValidUser();
			}
			
			//LoginSoap.CallLoginSOAP(mobilenumber);
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(isAllData){
				MainActivity.rslt = "Internet connection not available!!";
			}else{
				SMSAuthenticationActivity.rslt = "Internet connection not available!!";
			}
			
			if(checkCall.equalsIgnoreCase("splash")){
				MainActivity.rslt = "Internet connection not available!!";
			}
			if(checkCall.equalsIgnoreCase("sms")){
				SMSAuthenticationActivity.rslt = "Internet connection not available!!";
			}
			if(checkCall.equalsIgnoreCase("complaints")){
				Complaints.rslt = "error";
			}
			if(checkCall.equalsIgnoreCase("pickup")){
				PaymentPickup_New.rslt = "Internet connection not available!!";
			}
			if(checkCall.equalsIgnoreCase("changepack")){
				ChangePackage.rslt = "Internet connection not available!!";
			}
			
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(checkCall.equalsIgnoreCase("splash")){
				MainActivity.rslt = "Internet connection not available!!";
			}if(checkCall.equalsIgnoreCase("sms")){
				SMSAuthenticationActivity.rslt = "Internet connection not available!!";
			}
			if(checkCall.equalsIgnoreCase("complaints")){
				Utils.log("Complaint","Error");
				Complaints.rslt = "error";
			}
			if(checkCall.equalsIgnoreCase("pickup")){
				PaymentPickup_New.rslt = "error";
				}
			if(checkCall.equalsIgnoreCase("changepack")){
				ChangePackage.rslt = "Internet connection not available!!";
				}
			
		}
		catch (Exception e) {
			if(checkCall.equalsIgnoreCase("splash")){
			MainActivity.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			if(checkCall.equalsIgnoreCase("sms")){
			ExistingConnFragment.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			if(checkCall.equalsIgnoreCase("complaints")){
				Complaints.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			if(checkCall.equalsIgnoreCase("pickup")){
				PaymentPickup_New.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			if(checkCall.equalsIgnoreCase("changepack")){
				ChangePackage.rslt = "Invalid web-service response.<br>"+e.toString();
			}
		}
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}
	
	public String isCallData() {
		return checkCall;
	}

	public void setCallData(String checkCall) {
		this.checkCall = checkCall;
	}
	
}
