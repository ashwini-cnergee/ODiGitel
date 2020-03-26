package com.cnergee.mypage.obj;

import java.util.Date;
import java.util.Map;

public class PaymentPickUpObj {

	private long MemberId;
	private String Message;
	private String VisitDateTime;
	private Map<String, PaymentPickUpObj> PaymentPickUp;
	
	public long getMemberId() {
		return MemberId;
	}
	public void setMemberId(long memberId) {
		MemberId = memberId;
	}
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getVisitDateTime() {
		return VisitDateTime;
	}
	public void setVisitDateTime(String visitDateTime) {
		VisitDateTime = visitDateTime;
	}
	public Map<String, PaymentPickUpObj> getPaymentPickUp() {
		return PaymentPickUp;
	}
	public void setPaymentPickUp(Map<String, PaymentPickUpObj> paymentPickUp) {
		PaymentPickUp = paymentPickUp;
	}
	
	
	
}
