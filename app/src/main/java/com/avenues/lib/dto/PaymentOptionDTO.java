package com.avenues.lib.dto;

public class PaymentOptionDTO {
	private String payOptId;
	private String payOptName;
	private int icon_resource;
	
	public PaymentOptionDTO(String payOptId, String payOptName,int icon_resource) {
		super();
		this.payOptId = payOptId;
		this.payOptName = payOptName;
		this.icon_resource=icon_resource;
	}
	public String getPayOptId() {
		return payOptId;
	}
	public void setPayOptId(String payOptId) {
		this.payOptId = payOptId;
	}
	public String getPayOptName() {
		return payOptName;
	}
	public void setPayOptName(String payOptName) {
		this.payOptName = payOptName;
	}
	public int getIcon_resource() {
		return icon_resource;
	}
	public void setIcon_resource(int icon_resource) {
		this.icon_resource = icon_resource;
	}
	
	
}
