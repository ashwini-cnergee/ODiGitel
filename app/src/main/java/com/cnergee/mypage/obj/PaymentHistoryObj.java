package com.cnergee.mypage.obj;

public class PaymentHistoryObj {
	
	private String PaymentDate;
	private String Amount;
	private boolean isRead;
	private boolean isUpdated;
	private boolean isSclosed;
	private boolean isPush;
	
	private String Payment;
	public String getPayment() {
		return Payment;
	}

	public void setPayment(String payment) {
		Payment = payment;
	}

	public PaymentHistoryObj(){}
	
	public PaymentHistoryObj (String Payment ){
		this.Payment = Payment;
	}
	
	public String getPaymentDate() {
		return PaymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		PaymentDate = paymentDate;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public boolean isUpdated() {
		return isUpdated;
	}
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	public boolean isSclosed() {
		return isSclosed;
	}
	public void setSclosed(boolean isSclosed) {
		this.isSclosed = isSclosed;
	}
	public boolean isPush() {
		return isPush;
	}
	public void setPush(boolean isPush) {
		this.isPush = isPush;
	}
	
	

}
