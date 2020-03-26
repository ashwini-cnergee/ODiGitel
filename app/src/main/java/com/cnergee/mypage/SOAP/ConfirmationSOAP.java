package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.ConfirmationObj;
import com.cnergee.mypage.utils.Utils;


public class ConfirmationSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public ConfirmationObj memberconfirmatinData;
	private Map<String, ConfirmationObj> mapconfirmationDetails;
	private boolean isAllData;
	
	
	public ConfirmationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallSearchMemberSOAP(String MemberLoginId, String MobileNumber )throws SocketException,SocketTimeoutException,Exception {

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
		pi.setName("Moilenumber");
		pi.setValue(MobileNumber);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("MemberLoginId");
		pi.setValue(MemberLoginId);
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
		//Log.i("This is First Test","This is First Log Test");
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		//Log.i("This is Second Test","This is Second Log Test");		
		
		String str_msg = "ok";
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			 Object response2 = envelope.getResponse();
			// Log.i(" RESPONSE ",response2.toString());
			 Utils.log(" REQUEST ",androidHttpTransport.requestDump);
			 Utils.log(" RESPONSE ",androidHttpTransport.responseDump);
			 
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, ConfirmationObj> mapConfirmationDetails = new Hashtable<String, ConfirmationObj>();
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					//Log.i(" RESPONSE ", response.toString());
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response
									.getProperty(i);
							setConfirmationDetails(tableObj, mapConfirmationDetails);
						}
						setMapconfirmationDetails(mapConfirmationDetails);
						
						str_msg = "ok";
					} else {
						str_msg = "not";
					}
				} else {
					str_msg = "not";
				}

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
		return str_msg;
	}
	
	
	public void setConfirmationDetails(SoapObject response,
			Map<String, ConfirmationObj> mapConfirmationDetails) {
		
		ConfirmationObj confirmationdetails = new ConfirmationObj();
		
		confirmationdetails.setCustomerName(response.getPropertyAsString("CustomerName")
				.toString());
		confirmationdetails.setMemberLoginId(response.getPropertyAsString(
				"MemberLoginID").toString());
		confirmationdetails.setInstallationAddress(response.getPropertyAsString("InstallationAddress")
				.toString());
		confirmationdetails.setMobileNoprimary(response.getPropertyAsString("MobileNoprimary")
				.toString());
		confirmationdetails.setMemberId(response.getPropertyAsString("MemberId").toString());
		confirmationdetails.setShowLogo(response.getPropertyAsString("showLogo").toString());
		confirmationdetails.setCustomerCareNumber(response.getPropertyAsString("CustomerCareNumber").toString());
		mapConfirmationDetails.put(confirmationdetails.getMobileNoprimary(), confirmationdetails);
		
	}
	
	
	public Map<String, ConfirmationObj> getMapconfirmationDetails() {
		return mapconfirmationDetails;
	}


	public void setMapconfirmationDetails(
			Map<String, ConfirmationObj> mapconfirmationDetails) {
		this.mapconfirmationDetails = mapconfirmationDetails;
	}


	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}


}
