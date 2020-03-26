package com.citruspay.citruspaylib.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.cnergee.mypage.utils.Utils;

import android.graphics.Paint.Join;
import android.util.Log;

public class CitrusGetWebClientJSResponse {

	String responseWebClient;
	JSONObject jObj;
	
	public String getResponseWebClient() {
		return responseWebClient;
	}

	public void setResponseWebClient(String responseWebClient) {
		this.responseWebClient = responseWebClient;
	}

	
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTranssactionId() {
		return transsactionId;
	}

	public void setTranssactionId(String transsactionId) {
		this.transsactionId = transsactionId;
	}

	public String getPgRespCode() {
		return pgRespCode;
	}

	public void setPgRespCode(String pgRespCode) {
		this.pgRespCode = pgRespCode;
	}

	public String getTxMsg() {
		return TxMsg;
	}

	public void setTxMsg(String txMsg) {
		TxMsg = txMsg;
	}

	public String getAuthIdCode() {
		return authIdCode;
	}

	public void setAuthIdCode(String authIdCode) {
		this.authIdCode = authIdCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAddressStreet1() {
		return addressStreet1;
	}

	public void setAddressStreet1(String addressStreet1) {
		this.addressStreet1 = addressStreet1;
	}

	public String getAddressStreet2() {
		return addressStreet2;
	}

	public void setAddressStreet2(String addressStreet2) {
		this.addressStreet2 = addressStreet2;
	}

	public String getIsCOD() {
		return isCOD;
	}

	public void setIsCOD(String isCOD) {
		this.isCOD = isCOD;
	}

	public String getLoadStatus() {
		return loadStatus;
	}

	public void setLoadStatus(String loadStatus) {
		this.loadStatus = loadStatus;
	}

	public String getTxId() {
		return TxId;
	}

	public void setTxId(String txId) {
		TxId = txId;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTxGateway() {
		return TxGateway;
	}

	public void setTxGateway(String txGateway) {
		TxGateway = txGateway;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getTxRefNo() {
		return TxRefNo;
	}

	public void setTxRefNo(String txRefNo) {
		TxRefNo = txRefNo;
	}

	public String getLoadAmount() {
		return loadAmount;
	}

	public void setLoadAmount(String loadAmount) {
		this.loadAmount = loadAmount;
	}

	public String getPgTxnNo() {
		return pgTxnNo;
	}

	public void setPgTxnNo(String pgTxnNo) {
		this.pgTxnNo = pgTxnNo;
	}

	public String getTxStatus() {
		return TxStatus;
	}

	public void setTxStatus(String txStatus) {
		TxStatus = txStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIssuerRefNo() {
		return issuerRefNo;
	}

	public void setIssuerRefNo(String issuerRefNo) {
		this.issuerRefNo = issuerRefNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddressZip() {
		return addressZip;
	}

	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}

	String paymentMode,transsactionId,pgRespCode,TxMsg,authIdCode,currency,amount,addressStreet1,addressStreet2,isCOD,loadStatus,TxId,addressCountry,firstName,TxGateway,
	signature,addressState,lastName,addressCity,TxRefNo,loadAmount,pgTxnNo,TxStatus,email,issuerRefNo,mobileNo,addressZip;
	
	//{"paymentMode":"","transactionId":"32007","pgRespCode":"3","TxMsg":"Canceled by user","authIdCode":"","currency":"INR","amount":"1.00",
	//"addressStreet1":"","addressStreet2":"","isCOD":"","loadStatus":"fail","TxId":"15645229578009953895","addressCountry":"","firstName":"","TxGateway":"",
	//"signature":"a0f8528c796f2b16f3154e7628b420c283f76aa9","addressState":"","lastName":"","addressCity":"","TxRefNo":"CTX1307172343029220339",
	//"loadAmount":"1.00 INR","pgTxnNo":"CTX1307172343029220339","TxStatus":"CANCELED","email":"","issuerRefNo":"","mobileNo":"","addressZip":""}

	public CitrusGetWebClientJSResponse(String responseWebClient) {
		super();
		this.responseWebClient = responseWebClient;
		 try {
			jObj=new JSONObject(responseWebClient);
			paymentMode=jObj.getString("paymentMode");
			transsactionId=jObj.getString("transactionId");
			pgRespCode=jObj.getString("pgRespCode");
			TxMsg=jObj.getString("TxMsg");
			authIdCode=jObj.getString("authIdCode");
			currency=jObj.getString("currency");
			amount=jObj.getString("amount");
			addressStreet1=jObj.getString("addressStreet1");
			addressStreet2=jObj.getString("addressStreet2");
			isCOD=jObj.getString("isCOD");
			//loadStatus=jObj.getString("loadStatus");
			TxId=jObj.getString("TxId");
			addressCountry=jObj.getString("addressCountry");
		firstName=jObj.getString("firstName");
		TxGateway=jObj.getString("TxGateway");
		signature=jObj.getString("signature");
		addressState=jObj.getString("addressState");
		lastName=jObj.getString("lastName");
		addressCity=jObj.getString("addressCity");
		TxRefNo=jObj.getString("TxRefNo");
		//loadAmount=jObj.getString("loadAmount");
		pgTxnNo=jObj.getString("pgTxnNo");
		TxStatus=jObj.getString("TxStatus");
		Log.i("---------------------Tranaction Status------------------", TxStatus);
		email=jObj.getString("email");
		issuerRefNo=jObj.getString("issuerRefNo");
		mobileNo=jObj.getString("mobileNo");
		addressZip=jObj.getString("addressZip");
		
		Utils.log(">>>>>>TransactionId<<<<<<<<<<", transsactionId);
		//System.out.println(transsactionId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

