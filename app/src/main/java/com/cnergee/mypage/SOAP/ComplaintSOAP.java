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

public class ComplaintSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public ComplaintObj memberdetailsData;
	private Map<String, ComplaintObj> mapComplaintNo;
	private boolean isAllData;
	private String responseMsg;
	private String serverMessage;
	private String memberid;
	
	
	public ComplaintSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallComplaintNoSOAP(String memberid )throws SocketException,SocketTimeoutException,Exception {

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
		pi.setName("MemberId");
		pi.setValue(memberid);
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
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
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
		
		
		
		
		
		
		
		
		
		
		/*String str_msg = "ok";
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			 Object response2 = envelope.getResponse();
			 Log.i(" RESPONSE ",response2.toString());
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					
					setResponseMsg(response.toString());
					
				} 
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
		return str_msg;*/
	}
	
	
	
	/**
	 * @param mapMemberData
	 *            the mapMemberData to set
	 */
	public void setMapComplaintNo(Map<String, ComplaintObj> mapComplaintNo) {
		this.mapComplaintNo = mapComplaintNo;
	}

	public Map<String, ComplaintObj> getMapComplaintNo() {
		return this.mapComplaintNo;
	}
	
	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
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


	public String getMemberid() {
		return memberid;
	}


	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}


}
