package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.SOAP.NotificationSOAP;


public class NotificationCaller extends Thread {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	
	private String MemberId;
	private NotificationSOAP notificationSoap;
	
	public NotificationCaller() {
	}

	public NotificationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {
		
		try{
			notificationSoap = new NotificationSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			NotificationListActivity.rslt = notificationSoap.setNotificationList(MemberId);
			NotificationListActivity.notificationtList = notificationSoap.getNotificationList();
			
		}catch (SocketException e) {
			e.printStackTrace();
			NotificationListActivity.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			NotificationListActivity.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			NotificationListActivity.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	
}
