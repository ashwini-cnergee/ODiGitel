package com.cnergee.mypage.caller;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.RenewPackage;
import com.cnergee.mypage.SOAP.PAckageDetailSOAP;
import com.cnergee.mypage.utils.Utils;

public class PackageDetailCaller extends Thread{
	
public PAckageDetailSOAP PackageSoap;
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public String memberloginid;
	
	public PackageDetailCaller(){}
	
	public PackageDetailCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		
		
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public void run() {

		try {
			PackageSoap = new PAckageDetailSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			Utils.log(this.getClass().getSimpleName()," called");
			
			if(IONHome.is_home_running){
				Utils.log(""+this.getClass().getSimpleName(),":PackageSOAP Called");
			IONHome.rslt = PackageSoap.CallSearchMemberSOAP(
					memberloginid);
			IONHome.mapPackageDetails = PackageSoap
					.getMapPackageDetails();
			}
		/*	MyappDataUsage.rslt = PackageSoap.CallSearchMemberSOAP(
					memberloginid);
			MyappDataUsage.mapPackageDetails = PackageSoap
					.getMapPackageDetails();*/
			if(RenewPackage.is_renew_running){
			RenewPackage.rslt = PackageSoap.CallSearchMemberSOAP(memberloginid);
			RenewPackage.mapPackageDetails = PackageSoap
					.getMapPackageDetails();
			}

		}catch (SocketException e) {
			e.printStackTrace();
			if(IONHome.is_home_running)
			IONHome.rslt = "Internet connection not available!!";
			//MyappDataUsage.rslt = "Internet connection not available!!";
			if(RenewPackage.is_renew_running)
				RenewPackage.rslt = "error";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			if(IONHome.is_home_running)
			IONHome.rslt = "Internet connection not available!!";
			//MyappDataUsage.rslt = "Internet connection not available!!";
			if(RenewPackage.is_renew_running)
			RenewPackage.rslt = "error";
		}catch (Exception e) {
			e.printStackTrace();
			if(IONHome.is_home_running)
			IONHome.rslt = "Invalid web-service response.<br>"+e.toString();
			//MyappDataUsage.rslt = "Internet connection not available!!";
			if(RenewPackage.is_renew_running)
			RenewPackage.rslt = "Internet connection not available!!";
		}
	}

}
