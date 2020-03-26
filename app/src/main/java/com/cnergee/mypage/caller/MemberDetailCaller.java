package com.cnergee.mypage.caller;

import com.cnergee.mypage.MakeMyPayment_AggrePay;
import com.cnergee.mypage.MakeMyPayment_Atom;
import com.cnergee.mypage.MakeMyPayment_PayUActivity;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.MakeMyPayments_CCAvenue;
import com.cnergee.mypage.Profile;
import com.cnergee.mypage.SOAP.MemberDetailSOAP;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MemberDetailCaller extends Thread{
	
	public MemberDetailSOAP MemberSoap;

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public long memberid;
	private boolean isAllData=false;
	private boolean topup_flag=false;
	public MemberDetailCaller(){}
	
	public MemberDetailCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			MemberSoap = new MemberDetailSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			MemberSoap.setAllData(isAllData());
			
			
			if(isAllData()){
				Profile.rslt = MemberSoap.CallSearchMemberSOAP(memberid);
				Profile.mapMemberDetails = MemberSoap.getMapMemberDetails();
			
			}else{
				if(isTopup_flag()){
					MakeMyPaymentsTopUp.rslt = MemberSoap.CallSearchMemberSOAP(memberid);
					MakeMyPaymentsTopUp.mapMemberDetails= MemberSoap.getMapMemberDetails();
				}
				else{
				/*	if(!Utils.is_CCAvenue){
						MakeMyPayments.rslt = MemberSoap.CallSearchMemberSOAP(
						memberid);
						MakeMyPayments.mapMemberDetails= MemberSoap
						.getMapMemberDetails();
					}
					else{
						MakeMyPayments_CCAvenue.rslt = MemberSoap.CallSearchMemberSOAP(
							memberid);
						MakeMyPayments_CCAvenue.mapMemberDetails= MemberSoap
							.getMapMemberDetails();
					}*/

					/*if(Utils.is_atom){
						MakeMyPayment_Atom.rslt = MemberSoap.CallSearchMemberSOAP(memberid);
						MakeMyPayment_Atom.mapMemberDetails= MemberSoap.getMapMemberDetails();
					}else if(Utils.is_CCAvenue){
						MakeMyPayments_CCAvenue.rslt = MemberSoap.CallSearchMemberSOAP(memberid);
						MakeMyPayments_CCAvenue.mapMemberDetails= MemberSoap.getMapMemberDetails();
					}else{
						MakeMyPayments.rslt = MemberSoap.CallSearchMemberSOAP(memberid);
						MakeMyPayments.mapMemberDetails= MemberSoap.getMapMemberDetails();
					}*/

					if(Utils.is_aggrepay){
						MakeMyPayment_AggrePay.rslt=MemberSoap.CallSearchMemberSOAP(memberid);
						MakeMyPayment_AggrePay.mapMemberDetails=MemberSoap.getMapMemberDetails();
						Utils.log("Member Web Service  Executed",":"+getClass().toString());
					}else  {
						Utils.log("Member Web Service Not Executed",":"+getClass().toString());
					}
				}
			}
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Internet connection not available!!";
			else{
				
				if(isTopup_flag())
					MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
					else{
						if(!Utils.is_CCAvenue){
						MakeMyPayments.rslt = "Internet connection not available!!";
						}
						else{
							MakeMyPayments_CCAvenue.rslt = "Internet connection not available!!";	
						}
						
					}
			}
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Internet connection not available!!";
			else{
				if(isTopup_flag())
				MakeMyPaymentsTopUp.rslt = "Internet connection not available!!";
				else{
					/*MakeMyPayments.rslt = "Internet connection not available!!";*/
					if(!Utils.is_CCAvenue){
						MakeMyPayments.rslt = "Internet connection not available!!";
						}
						else{
							MakeMyPayments_CCAvenue.rslt = "Internet connection not available!!";	
						}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			if(isAllData())
			Profile.rslt = "Invalid web-service response.<br>"+e.toString();
			else{
				if(isTopup_flag())
					MakeMyPaymentsTopUp.rslt = "Invalid web-service response.<br>"+e.toString();
					else{
						if(!Utils.is_CCAvenue){
						MakeMyPayments.rslt = "Invalid web-service response.<br>"+e.toString();
						}
						else{
						MakeMyPayments_CCAvenue.rslt = "Invalid web-service response.<br>"+e.toString();
						}
					}
				
			}
		}
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}

	public boolean isTopup_flag() {
		return topup_flag;
	}

	public void setTopup_flag(boolean topup_flag) {
		this.topup_flag = topup_flag;
	}
	
	
}
