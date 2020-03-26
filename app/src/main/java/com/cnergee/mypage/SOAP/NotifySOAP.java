package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.acl.NotOwnerException;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;




import com.cnergee.mypage.obj.Notificationobj;


public class NotifySOAP  {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "MypageService"; 
	private ArrayList<Notificationobj> notificationList = new ArrayList<Notificationobj>();
	
	public NotifySOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String setnotify(String MemberId)throws SocketException,SocketTimeoutException,Exception{
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		//NotificationAsyncWS();
		return "OK";
		
		
	}

	public ArrayList<Notificationobj> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(ArrayList<Notificationobj> notificationList) {
		this.notificationList = notificationList;
	}

	
	
	

}
