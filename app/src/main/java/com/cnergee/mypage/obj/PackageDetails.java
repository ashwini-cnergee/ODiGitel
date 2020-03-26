package com.cnergee.mypage.obj;

import java.util.Map;

public class PackageDetails {

	private String PackageName;
	private String Amount;
	private String PackageValidity;
	private String expiryDate;
	private String MemberLoginId;
	private String MemberRegsiterDate;
	private String IpAddress;
	private String ServiceTax;
	private String AreaCode;
	private String IsFreePackage;
	private String CheckForRenewal;
	private String DaysRemaining;
	private String MemberName;
	private String SubscriberName;;
	private String ConnectionTypeId;
	private boolean is_24ol;
	private int isCC_Avenue=0;
	private int is_citrus=0;
	private int isAtom;
	private int payuMoney=0;
	private int aggrePay=0;


	public String getCheckForRenewal() {
		return CheckForRenewal;
	}

	public void setCheckForRenewal(String checkForRenewal) {
		CheckForRenewal = checkForRenewal;
	}


	public String getMemberLoginId() {
		return MemberLoginId;
	}
	public void setMemberLoginId(String memberLoginId) {
		MemberLoginId = memberLoginId;
	}
	public String getMemberRegsiterDate() {
		return MemberRegsiterDate;
	}
	public void setMemberRegsiterDate(String memberRegsiterDate) {
		MemberRegsiterDate = memberRegsiterDate;
	}
	public String getIpAddress() {
		return IpAddress;
	}
	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}
	private Map<String, PackageDetails> packagedetails;


	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getPackageValidity() {
		return PackageValidity;
	}
	public void setPackageValidity(String packageValidity) {
		PackageValidity = packageValidity;
	}

	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public String getIsFreePackage() {
		return IsFreePackage;
	}
	public void setIsFreePackage(String isFreePackage) {
		IsFreePackage = isFreePackage;
	}

	public void setPackagedetails(Map<String, PackageDetails> packagedetails) {
		this.packagedetails = packagedetails;
	}
	public String getServiceTax() {
		return ServiceTax;
	}
	public void setServiceTax(String serviceTax) {
		ServiceTax = serviceTax;
	}
/*
	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
*/


	public String getMemberName() {
		return MemberName;
	}

	public String getSubscriberName() {
		return SubscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		SubscriberName = subscriberName;
	}

	public void setMemberName(String memberName) {
		MemberName = memberName;
	}




	public String getDaysRemaining() {
		return DaysRemaining;
	}

	public void setDaysRemaining(String daysRemaining) {
		DaysRemaining = daysRemaining;
	}

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}


	public boolean isIs_24ol() {
		return is_24ol;
	}

	public void setIs_24ol(boolean is_24ol) {
		this.is_24ol = is_24ol;
	}

	public int getIsCC_Avenue() {
		return isCC_Avenue;
	}

	public void setIsCC_Avenue(int isCC_Avenue) {
		this.isCC_Avenue = isCC_Avenue;
	}

	public int getIs_citrus() {
		return is_citrus;
	}

	public void setIs_citrus(int is_citrus) {
		this.is_citrus = is_citrus;
	}

	public int getIsAtom() {
		return isAtom;
	}

	public void setIsAtom(int isAtom) {
		this.isAtom = isAtom;
	}

	public int getPayuMoney() {
		return payuMoney;
	}

	public void setPayuMoney(int payuMoney) {
		this.payuMoney = payuMoney;
	}

	public int getAggrePay() {
		return aggrePay;
	}

	public void setAggrePay(int aggrePay) {
		this.aggrePay = aggrePay;
	}
}