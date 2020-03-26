package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.SOAP.DeleteNotificationSoap;
import com.cnergee.mypage.SOAP.NotificationSOAP;

public class DeleteNotificationCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;

	
	private String NotifyId,MemberId;
	private DeleteNotificationSoap notificationSoap;
	
	public DeleteNotificationCaller() {
	}

	public DeleteNotificationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {
		
		try{
			notificationSoap = new DeleteNotificationSoap(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			NotificationListActivity.Deleterslt = notificationSoap.deleteNotificationList(NotifyId,MemberId);
			NotificationListActivity.DeleteResponse=notificationSoap.getResponse();
			
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
	
	public void setNotifyIdCaller(String notifyid){
		this.NotifyId=notifyid;
	}
	
	public void setMemberIdCaller(String MemberId){
		this.MemberId=MemberId;
	}
}
