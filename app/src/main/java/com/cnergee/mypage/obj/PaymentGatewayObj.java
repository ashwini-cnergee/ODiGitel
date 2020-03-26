package com.cnergee.mypage.obj;

public class PaymentGatewayObj {
	
	private String CompanyId;
	private String TransportalId;
	private String transportalPw;
	private String LangId;
	private String CurrencyCode;
	private String RedirectUrl;
	private String GateWayType;
	private String PGID;
	private String BankName;
	public String getCompanyId() {
		return CompanyId;
	}
	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}
	public String getTransportalId() {
		return TransportalId;
	}
	public void setTransportalId(String transportalId) {
		TransportalId = transportalId;
	}
	public String getTransportalPw() {
		return transportalPw;
	}
	public void setTransportalPw(String transportalPw) {
		this.transportalPw = transportalPw;
	}
	public String getLangId() {
		return LangId;
	}
	public void setLangId(String langId) {
		LangId = langId;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getRedirectUrl() {
		return RedirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		RedirectUrl = redirectUrl;
	}
	public String getGateWayType() {
		return GateWayType;
	}
	public void setGateWayType(String gateWayType) {
		GateWayType = gateWayType;
	}
	public String getPGID() {
		return PGID;
	}
	public void setPGID(String pGID) {
		PGID = pGID;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	
	
	
	

}
