package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;



import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.SOAP.ComplaintsStatusSOAP;

public class ComplaintsStatusCaller extends Thread {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String MemberId;
	
	ComplaintsStatusSOAP complaintsStatusSOAP;
	
	public ComplaintsStatusCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
public void run() {
		
		try{
			complaintsStatusSOAP = new ComplaintsStatusSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			Complaints.statusRslt = complaintsStatusSOAP.getComplaintStatus(MemberId);
			Complaints.statusResponse=complaintsStatusSOAP.getResponse();
			//Utils.log("Complaint status caller",""+Complaints.statusResponse);
			//Utils.log("Complaint status Result",""+Complaints.statusRslt);
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
public void setMemberIdCaller(String MemberId){
	this.MemberId=MemberId;
}
}
