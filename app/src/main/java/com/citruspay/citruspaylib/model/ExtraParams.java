/*
 * Copyright (c) 2012 CitrusPay. All Rights Reserved.
 *
 * This software is the proprietary information of CitrusPay.
 * Use is subject to license terms.
 */
package com.citruspay.citruspaylib.model;

import java.io.Serializable;

public class ExtraParams implements Serializable {
	private static final long serialVersionUID = 808741385738242641L;

	private String HmacUrl;
	private String currency;
	private String merchantTxnId;
	private String orderAmountValue;
	private String returnUrl;
	private String HMACValue;

	public String getHMACValue() {
		return HMACValue;
	}

	public void setHMACValue(String hMACValue) {
		HMACValue = hMACValue;
	}

	public String getOrderAmountValue() {
		return orderAmountValue;
	}

	public void setOrderAmountValue(String orderAmountValue) {
		this.orderAmountValue = orderAmountValue;
	}

	public String getMerchantTxnId() {
		return merchantTxnId;
	}

	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}

	

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHmacUrl() {
		return HmacUrl;
	}

	public void setHmacUrl(String hmacUrl) {
		HmacUrl = hmacUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}