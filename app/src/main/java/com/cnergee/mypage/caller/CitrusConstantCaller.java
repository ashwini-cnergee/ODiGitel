package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.SOAP.CitrusConstantSOAP;;

public class CitrusConstantCaller extends Thread{
	
	public CitrusConstantSOAP CitrusConstantssoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	private boolean isAllData;
	private boolean topup_flag;
	public CitrusConstantCaller(){}
	
	public CitrusConstantCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,boolean topup_flag) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.topup_flag=topup_flag;
	}
	
	public void run() {

		try {
			CitrusConstantssoap = new CitrusConstantSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			if(topup_flag){
				MakeMyPaymentsTopUp.rslt = CitrusConstantssoap.CallSearchMemberSOAP(
						memberid);
			MakeMyPaymentsTopUp.mapcitrusconstants= CitrusConstantssoap
						.getMapcitrusconstants();
			}
			else{
				MakeMyPayments.rslt = CitrusConstantssoap.CallSearchMemberSOAP(
						memberid);
			MakeMyPayments.mapcitrusconstants= CitrusConstantssoap
						.getMapcitrusconstants();
			}
			
			
			
			
		}catch (SocketException e) {
			e.printStackTrace();
				if(topup_flag)
				MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
				else
				MakeMyPayments.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(topup_flag)
				MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
				else
				MakeMyPayments.rslt = "error";
		}catch (Exception e) {
			if(topup_flag)
				MakeMyPaymentsTopUp.rslt = "Invalid web-service response.<br>"+e.toString();
				else
				MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
				
		}
	}
}
