package com.cnergee.mypage.obj;

import java.io.Serializable;

public class AdditionalAmount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String PackageRate,AdditionalAmount,AdditionalAmountType,DaysFineAmount,DiscountAmount,finalcharges,FineAmount,DiscountPercentage;
	public boolean isChequeBounced;

	
	
	public AdditionalAmount(String PackageRate,String additionalAmount,
			String additionalAmountType, String daysFineAmount,
			String discountAmount, String finalcharges, String fineAmount,
			String discountPercentage,boolean is_cheque_bounced) {
		super();
		this.PackageRate=PackageRate;
		AdditionalAmount = additionalAmount;
		AdditionalAmountType = additionalAmountType;
		DaysFineAmount = daysFineAmount;
		DiscountAmount = discountAmount;
		this.finalcharges = finalcharges;
		FineAmount = fineAmount;
		DiscountPercentage = discountPercentage;
		is_cheque_bounced=isChequeBounced;
	}

	public String getAdditionalAmount() {
		return AdditionalAmount;
	}

	public void setAdditionalAmount(String additionalAmount) {
		AdditionalAmount = additionalAmount;
	}

	public String getAdditionalAmountType() {
		return AdditionalAmountType;
	}

	public void setAdditionalAmountType(String additionalAmountType) {
		AdditionalAmountType = additionalAmountType;
	}

	public String getDaysFineAmount() {
		return DaysFineAmount;
	}

	public void setDaysFineAmount(String daysFineAmount) {
		DaysFineAmount = daysFineAmount;
	}

	public String getDiscountAmount() {
		return DiscountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		DiscountAmount = discountAmount;
	}

	public String getFinalcharges() {
		return finalcharges;
	}

	public void setFinalcharges(String finalcharges) {
		this.finalcharges = finalcharges;
	}

	public String getFineAmount() {
		return FineAmount;
	}

	public void setFineAmount(String fineAmount) {
		FineAmount = fineAmount;
	}

	public String getDiscountPercentage() {
		return DiscountPercentage;
	}

	public void setDiscountPercentage(String discountPercentage) {
		DiscountPercentage = discountPercentage;
	}

	public String getPackageRate() {
		return PackageRate;
	}

	public void setPackageRate(String packageRate) {
		PackageRate = packageRate;
	}

	public boolean isChequeBounced() {
		return isChequeBounced;
	}

	public void setChequeBounced(boolean isChequeBounced) {
		this.isChequeBounced = isChequeBounced;
	}
	
	
}
