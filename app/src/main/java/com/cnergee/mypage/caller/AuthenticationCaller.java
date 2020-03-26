package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.Authentication;

import com.cnergee.mypage.SOAP.AuthenticationSOAP;

public class AuthenticationCaller extends Thread{
public AuthenticationSOAP authentication;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String mobilenumber;
	public String userid;
	
	
	public AuthenticationCaller() {
	}

	public AuthenticationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		
	}

	public void run() {
		
		try {
			authentication = new AuthenticationSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			Authentication.rslt = authentication.CallAuthenticationSOAP(mobilenumber,userid);
			Authentication.isVaildUser = authentication.isValidUser();
			/*if(authentication.isValidUser())
			{
			 	Authentication.mobilenumber = authentication.getMobilenumber();
				Authentication.userId = authentication.getUserid();
			}*/
			
			//LoginSoap.CallLoginSOAP(mobilenumber);
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			Authentication.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			Authentication.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			Authentication.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

}
