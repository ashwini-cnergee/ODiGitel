package com.cnergee.mypage.obj;

import java.io.Serializable;

public class MemberDetailsObj implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String MemberName;
	private String MemberLoginId;
	private String ActivationDate;
	private String DateofBirth;
	private String MobileNo;
	private String AlternateNo;
	private String EmailId;
	private String InstLocAddressLine1;
	private String InstLocAddressLine2;
	private String Status;
	private String PackageName;
	private String IPAddress;
	private String pincode;
	private String Mb_secondary;
	private String city;
	private String state;
	
	public String getPackageName() {
		return PackageName;
	}

	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	
	public String getMemberLoginId() {
		return MemberLoginId;
	}


	public void setMemberLoginId(String memberLoginId) {
		MemberLoginId = memberLoginId;
	}


	public String getActivationDate() {
		return ActivationDate;
	}


	public void setActivationDate(String activationDate) {
		ActivationDate = activationDate;
	}


	public String getDateofBirth() {
		return DateofBirth;
	}


	public void setDateofBirth(String dateofBirth) {
		DateofBirth = dateofBirth;
	}


	public String getMobileNo() {
		return MobileNo;
	}


	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}


	public String getAlternateNo() {
		return AlternateNo;
	}


	public void setAlternateNo(String alternateNo) {
		AlternateNo = alternateNo;
	}


	public String getEmailId() {
		return EmailId;
	}


	public void setEmailId(String emailId) {
		EmailId = emailId;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}


	public MemberDetailsObj(){}


	public String getInstLocAddressLine1() {
		return InstLocAddressLine1;
	}


	public void setInstLocAddressLine1(String instLocAddressLine1) {
		InstLocAddressLine1 = instLocAddressLine1;
	}


	public String getInstLocAddressLine2() {
		return InstLocAddressLine2;
	}


	public void setInstLocAddressLine2(String instLocAddressLine2) {
		InstLocAddressLine2 = instLocAddressLine2;
	}


	public String getMemberName() {
		return MemberName;
	}


	public void setMemberName(String memberName) {
		MemberName = memberName;
	}


	public String getIPAddress() {
		return IPAddress;
	}


	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}


	/*public String getPackageAmount() {
		return PackageAmount;
	}


	public void setPackageAmount(String packageAmount) {
		PackageAmount = packageAmount;
	}


	public String getServiceTax() {
		return ServiceTax;
	}


	public void setServiceTax(String serviceTax) {
		ServiceTax = serviceTax;
	}*/

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getMb_secondary() {
		return Mb_secondary;
	}

	public void setMb_secondary(String mb_secondary) {
		Mb_secondary = mb_secondary;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
