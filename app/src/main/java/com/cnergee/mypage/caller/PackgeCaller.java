
package com.cnergee.mypage.caller;

import com.cnergee.mypage.ChangePackage;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.SOAP.PackgeSOAP;

public class PackgeCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private long MemberId;
	public String username;
	public String password;
	PackgeSOAP packageSOAP;
	String ConnectionTypeId="",AreaCode="";
	private String SubscriberID;

	public PackgeCaller() {
	}

	public PackgeCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
						String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {

		try {
			packageSOAP = new PackgeSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			packageSOAP.setMemberId(getMemberId());
			packageSOAP.setConnectionTypeId(getConnectionTypeId());
			packageSOAP.setAreaCode(getAreaCode());
			PackgedetailActivity.rslt = packageSOAP.CallPackageSOAP();
			PackgedetailActivity.strXML = packageSOAP.getXMLData();

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

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}




}
