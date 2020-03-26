package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.util.Log;

import com.cnergee.fragments.LaunchComplaintFragment;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.PaymentPickup_New;

import com.cnergee.mypage.SelfResolution;

import com.cnergee.mypage.SOAP.InsertComplaintSOAP;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.utils.Utils;



public class InsertComplaintCaller extends Thread{
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private InsertComplaintSOAP insertcomplaint;
	private ComplaintObj complaintobj;
	private String activity;
	public InsertComplaintCaller(){}
	
	public InsertComplaintCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,String activity) {
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.activity=activity;
	}
	
	
	
	public void run() {

		try {
			
			Utils.log("in Insert Compl", "41 Soap Executed");
			insertcomplaint = new InsertComplaintSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,
					METHOD_NAME);
			//paymentpickup.setMemberId(memberid);
			
			insertcomplaint.setComplaintobj(getComplaintobj());

			if(activity.equalsIgnoreCase("complaint")){
				if(Utils.self_res_question_show){
				LaunchComplaintFragment.rslt = insertcomplaint.CalComplaintSOAP();
				LaunchComplaintFragment.responseMsg = insertcomplaint.getResponseMsg();
				}
				else{
					Complaints.rslt = insertcomplaint.CalComplaintSOAP();
					Complaints.responseMsg = insertcomplaint.getResponseMsg();
				}
			}
			if(activity.equalsIgnoreCase("self")){
				SelfResolution.rslt = insertcomplaint.CalComplaintSOAP();
				SelfResolution.responseMsg = insertcomplaint.getResponseMsg();
			}
			
			Utils.log("Response :",""+insertcomplaint);
			
		}catch (SocketException e) {
			e.printStackTrace();
			if(activity.equalsIgnoreCase("complaint")){
				if(Utils.self_res_question_show)
				LaunchComplaintFragment.rslt = "Internet connection not available!!";
				else
				Complaints.rslt = "Internet connection not available!!";	
			}
			else{
				SelfResolution.rslt = "Internet connection not available!!";
			}
			PaymentPickup_New.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(activity.equalsIgnoreCase("complaint")){
				if(Utils.self_res_question_show)
					LaunchComplaintFragment.rslt = "Internet connection not available!!";
					else
					Complaints.rslt = "Internet connection not available!!";
			}
			else{
				SelfResolution.rslt = "Internet connection not available!!";
			}
			
		}catch (Exception e) {
			if(activity.equalsIgnoreCase("complaint")){
				if(Utils.self_res_question_show)
				LaunchComplaintFragment.rslt = "Invalid web-service response.<br>"+e.toString();
				else	
				Complaints.rslt = "Invalid web-service response.<br>"+e.toString();
			}
			else{
				SelfResolution.rslt = "Invalid web-service response.<br>"+e.toString();
			}
		
		}
		
	}

	public ComplaintObj getComplaintobj() {
		return complaintobj;
	}

	public void setcomplaintobj(ComplaintObj complaintobj) {
		this.complaintobj = complaintobj;
	}
	

}
