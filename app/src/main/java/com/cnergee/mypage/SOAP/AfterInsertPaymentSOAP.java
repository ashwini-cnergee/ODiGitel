package com.cnergee.mypage.SOAP;


import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.Utils;

public class AfterInsertPaymentSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public PaymentsObj paymentdata;
	private Map<String, ComplaintObj> mapComplaintNo;
	private boolean isAllData;
	private String responseMsg;
	private String serverMessage;
	
	public String authIdCode;
	public String TxId;
	public String TxStatus;
	public String pgTxnNo;
	public String issuerRefNo;
	public String TxMsg;
	
	
	
	public AfterInsertPaymentSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallComplaintNoSOAP()throws SocketException,SocketTimeoutException,Exception {

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" searchTxt ", searchTxt);
		//Log.i(" #	#####################  ", " START ");

		//Log.i(" username ", username);
		//Log.i(" password ", password);
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i(" searchTxt ", searchTxt);*/
		//Log.i("#####################", "");
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName("authIdCode");
		pi.setValue(getPaymentdata().getAuthIdCode());
		pi.setType(long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TxId");
		pi.setValue(getPaymentdata().getTxId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TxStatus");
		pi.setValue(getPaymentdata().getTxStatus());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("pgTxnNo");
		pi.setValue(getPaymentdata().getPgTxnNo());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("issuerRefNo");
		pi.setValue(getPaymentdata().getIssuerRefNo());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TxMsg");
		pi.setValue(getPaymentdata().getTxMsg());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Log.i(">>>>REquest>>>>>" , request.toString());
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log(this.getClass().getSimpleName(), " Request: "+androidHttpTransport.requestDump);
			Utils.log(this.getClass().getSimpleName(), " Response: "+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				//Log.i(">>>>Response<<<<<",response.toString());
				setServerMessage(response.toString());
				
				/*String ss = androidHttpTransport.responseDump;
				Log.i("#####################", ss);
				*/
				
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
			//Log.i("#####################", getServerMessage());
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		
	}
	
	
	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
	/**
	 * @return the serverMessage
	 */
	public String getServerMessage() {
		return serverMessage;
	}

	/**
	 * @param serverMessage
	 *            the serverMessage to set
	 */
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}


	public String getAuthIdCode() {
		return authIdCode;
	}


	public void setAuthIdCode(String authIdCode) {
		this.authIdCode = authIdCode;
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


	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}


	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}


	


}
