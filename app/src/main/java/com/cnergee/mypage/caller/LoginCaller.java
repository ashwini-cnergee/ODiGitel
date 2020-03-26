package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;


import com.cnergee.fragments.ExistingConnFragment;
import com.cnergee.mypage.SOAP.LoginSOAP;

public class LoginCaller extends Thread {
	
	public LoginSOAP LoginSoap;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String mobilenumber;
	
	
	public LoginCaller() {
	}

	public LoginCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
				
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
				
	}

	public void run() {
		
		try {
			
			LoginSoap = new LoginSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			ExistingConnFragment.rslt = LoginSoap.CallLoginSOAP(mobilenumber);
			ExistingConnFragment.isVaildUser = LoginSoap.isValidUser();
			ExistingConnFragment.getAuthcount = LoginSoap.getAuthcount();
			if(LoginSoap.isValidUser()){
			 
				ExistingConnFragment.userId = LoginSoap.getMobilenumber();
				
			}
			
			//LoginSoap.CallLoginSOAP(mobilenumber);
			
		}catch (SocketException e) {
			e.printStackTrace();
			ExistingConnFragment.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			ExistingConnFragment.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			ExistingConnFragment.rslt = "Invalid web-service response.<br>"+e.toString();
		}
	}

}
