package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.MyappDataUsage;
import com.cnergee.mypage.SOAP.DataUsageSOAP;

public class DataUsageCaller extends Thread{
	
	public DataUsageSOAP Datasoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	private boolean isAllData;
	
	public DataUsageCaller(){}
	
	public DataUsageCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			Datasoap = new DataUsageSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
				
			MyappDataUsage.rslt = Datasoap.CallSearchMemberSOAP(
						memberid);
			MyappDataUsage.mapdatausage= Datasoap
						.getMapdatausagedetails();
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
			
			MyappDataUsage.rslt = "Internet connection not available!!";
			
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			
			MyappDataUsage.rslt = "Internet connection not available!!";
			
		}catch (Exception e) {
			e.printStackTrace();
			
			MyappDataUsage.rslt = "Invalid web-service response.<br>"+e.toString();
			
		}
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}
	
}
