package com.cnergee.mypage.obj;

import java.text.DecimalFormat;

public class PaymentsObj {

	private long MemberId;
	private String TrackId;
	private String Amount;
	private String PackageName;
	private String ServiceTax;
	private String AuthIdCode;
	private String TxId;
	private String TxStatus;
	private String pgTxnNo;
	private String issuerRefNo;
	private String TxMsg;
	private String MobileNumber;
	private String SubscriberID;
	private String PlanName;
	private double PaidAmount;
	private boolean IsChangePlan;
	private String ActionType;
	private String PaymentId;
	private String Speed;
	private String Data;
	private String Topup_validity;
	private String Discount;
	private String Discount_Amount;
	private String pg_sms_unique_id;
	private String is_renew;

	private String bankcode;


	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public long getMemberId() {
		return MemberId;
	}
	public void setMemberId(long memberId) {
		MemberId = memberId;
	}
	public String getPg_sms_unique_id() {
		return pg_sms_unique_id;
	}
	public void setPg_sms_unique_id(String pg_sms_unique_id) {
		this.pg_sms_unique_id = pg_sms_unique_id;
	}
	public String getTrackId() {
		return TrackId;
	}
	public void setTrackId(String trackId) {
		TrackId = trackId;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getServiceTax() {
		return ServiceTax;
	}
	public void setServiceTax(String serviceTax) {
		ServiceTax = serviceTax;
	}
	public String getAuthIdCode() {
		return AuthIdCode;
	}
	public void setAuthIdCode(String authIdCode) {
		AuthIdCode = authIdCode;
	}
	public String getTxId() {
		return TxId;
	}
	public void setTxId(String txId) {
		TxId = txId;
	}
	public String getTxStatus() {
		return TxStatus;
	}
	public void setTxStatus(String txStatus) {
		TxStatus = txStatus;
	}
	public String getPgTxnNo() {
		return pgTxnNo;
	}
	public void setPgTxnNo(String pgTxnNo) {
		this.pgTxnNo = pgTxnNo;
	}
	public String getIssuerRefNo() {
		return issuerRefNo;
	}
	public void setIssuerRefNo(String issuerRefNo) {
		this.issuerRefNo = issuerRefNo;
	}
	public String getTxMsg() {
		return TxMsg;
	}
	public void setTxMsg(String txMsg) {
		TxMsg = txMsg;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	public String getSubscriberID() {
		return SubscriberID;
	}
	public void setSubscriberID(String subscriberID) {
		SubscriberID = subscriberID;
	}
	public String getPlanName() {
		return PlanName;
	}
	public void setPlanName(String planName) {
		PlanName = planName;
	}
	public double getPaidAmount() {
		return PaidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		PaidAmount = paidAmount;
	}
	public boolean getIsChangePlan() {
		return IsChangePlan;
	}
	public void setIsChangePlan(boolean isChangePlan) {
		IsChangePlan = isChangePlan;
	}
	public String getActionType() {
		return ActionType;
	}
	public void setActionType(String actionType) {
		ActionType = actionType;
	}
	public String getPaymentId() {
		return PaymentId;
	}
	public void setPaymentId(String paymentId) {
		PaymentId = paymentId;
	}
	public String getSpeed() {
		return Speed;
	}
	public void setSpeed(String speed) {
		Speed = speed;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	public String getTopup_validity() {
		return Topup_validity;
	}
	public void setTopup_validity(String topup_validity) {
		Topup_validity = topup_validity;
	}
	public String getDiscount() {
		return Discount;
	}
	public void setDiscount(String discount) {
		Discount = discount;
	}
	public String getDiscount_Amount() {
		return Discount_Amount;
	}
	public void setDiscount_Amount(String discount_Amount) {
		Discount_Amount = discount_Amount;
	}

	public String getIs_renew() {
		return is_renew;
	}
	public void setIs_renew(String is_renew) {
		this.is_renew = is_renew;
	}



}
