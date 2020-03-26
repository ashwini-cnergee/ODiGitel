
package com.cnergee.mypage.caller;

import com.cnergee.mypage.ChangePackage;
import com.cnergee.mypage.SOAP.AdjustmentSOAP;


public class AdjustmentCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private AdjustmentSOAP adjustmentSOAP;
	private String  newPlan;
	private long MemberId;

	public AdjustmentCaller() {
	}

	public AdjustmentCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {

		try {
			adjustmentSOAP = new AdjustmentSOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			adjustmentSOAP.setNewPlan(getNewPlan());
			adjustmentSOAP.setMemberId(getMemberId());
			ChangePackage.rslt = adjustmentSOAP
					.CallAdjustmentAmountSOAP();
			ChangePackage.adjStringVal = adjustmentSOAP
					.getServerMessage();

		} catch (Exception e) {
			ChangePackage.rslt = e.toString();
		}
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}

	/**
	 * @return the newPlan
	 */
	public String getNewPlan() {
		return newPlan;
	}

	/**
	 * @param newPlan
	 *            the newPlan to set
	 */
	public void setNewPlan(String newPlan) {
		this.newPlan = newPlan;
	}

	

}
