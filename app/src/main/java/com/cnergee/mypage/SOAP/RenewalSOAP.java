package com.cnergee.mypage.SOAP;

import com.cnergee.mypage.Marshals.MarshalDouble;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

public class RenewalSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public PaymentsObj paymentdata;
	private Map<String, ComplaintObj> mapComplaintNo;
	private boolean isAllData;
	private String responseMsg;
	private String serverMessage;
	private String RenewalType;
	
	public RenewalSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
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
		pi.setName("MobileNo");
		pi.setValue(getPaymentdata().getMobileNumber());
		pi.setType(long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("SubscriberID");
		pi.setValue(getPaymentdata().getSubscriberID());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PlanName");
		pi.setValue(getPaymentdata().getPlanName());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaidAmount");
		double payamt = getPaymentdata().getPaidAmount();
		pi.setValue(payamt);
		pi.setType(Double.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("TrackId");
		pi.setValue(getPaymentdata().getTrackId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("IsChangePlan");
		pi.setValue(getPaymentdata().getIsChangePlan());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ActionType");
		pi.setValue(getPaymentdata().getActionType());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("PaymentId");
		pi.setValue(getPaymentdata().getPaymentId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("RenewalType");
		pi.setValue(getRenewalType());
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
		Utils.log("Renewal Response",":"+request.toString());

		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		
		MarshalDouble mdouble = new MarshalDouble();
		mdouble.register(envelope);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME, envelope);
			Utils.log(this.getClass().getSimpleName(), " Request: "+androidHttpTransport.requestDump);
			Utils.log(this.getClass().getSimpleName(), " Response: "+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				//Log.i(">>>>Response<<<<<",response.toString());
				Utils.log("Renewal Response",":"+response.toString());
				setServerMessage(response.toString());

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


	public PaymentsObj getPaymentdata() {
		return paymentdata;
	}


	public void setPaymentdata(PaymentsObj paymentdata) {
		this.paymentdata = paymentdata;
	}


	public String getRenewalType() {
		return RenewalType;
	}


	public void setRenewalType(String renewalType) {
		RenewalType = renewalType;
	}


}
