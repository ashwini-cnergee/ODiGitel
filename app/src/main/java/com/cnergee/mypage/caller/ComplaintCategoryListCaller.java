package com.cnergee.mypage.caller;

import com.cnergee.fragments.LaunchComplaintFragment;
import com.cnergee.mypage.ChangePackage;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.SOAP.ComplaintCategorySOAP;
import com.cnergee.mypage.SOAP.PackgeSOAP;

public class ComplaintCategoryListCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private long MemberId;
	public String username;
	public String password;
	ComplaintCategorySOAP complaintcategorylistSOAP;
	
	private String SubscriberID;

	public ComplaintCategoryListCaller() {
	}

	public ComplaintCategoryListCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {

		try {
			complaintcategorylistSOAP = new ComplaintCategorySOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			Complaints.rslt = complaintcategorylistSOAP.CallPackageSOAP();
			Complaints.complaintcategorylist = complaintcategorylistSOAP.GetComplaintList();

		} catch (Exception e) {
			ChangePackage.rslt = e.toString();
		}
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}
}
