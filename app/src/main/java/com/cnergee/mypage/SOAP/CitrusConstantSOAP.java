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

import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.CitrusConstantsObj;
import com.cnergee.mypage.utils.Utils;

public class CitrusConstantSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public CitrusConstantsObj citrusconstantData;
	private Map<String, CitrusConstantsObj> mapcitrusconstants;
	
	
	public CitrusConstantSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallSearchMemberSOAP(long memberid )throws SocketException,SocketTimeoutException,Exception {

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
		pi.setType(Long.class);
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
		
		
		String str_msg = "ok";
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			//Log.i(" REQUEST ", androidHttpTransport.requestDump);
			// Object response2 = envelope.getResponse();
			 //Log.i(" RESPONSE ",response2.toString());
			
			Utils.log("CitrusConstantSOAP ",":"+androidHttpTransport.requestDump);
			Utils.log("CitrusConstantSOAP ",":"+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, CitrusConstantsObj> mapconstants = new Hashtable<String, CitrusConstantsObj>();
				SoapObject response = (SoapObject) envelope.getResponse();
				//Log.i(" RESPONSE ", response.toString());
				if (response != null) {
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0 ) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response
									.getProperty(i);
							//Log.i("-------This is Test--------","-----Condition Failed-----");
							try
							{
								setCitrusConstants(tableObj, mapconstants);
							}
							catch(Exception e) {
								e.printStackTrace();
								str_msg = "Data usage not found";
								return str_msg;
							}
						}
						setMapcitrusconstants(mapconstants);
						
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
	
	
	public void setCitrusConstants(SoapObject response,
			Map<String, CitrusConstantsObj> mapcitrusconstant) 
	{
		
		CitrusConstantsObj citrusconstant = new CitrusConstantsObj();
		//citrusconstant.setMERCHANTTXNID(response.getPropertyAsString("TransportalPw").toString());
		citrusconstant.setMYCITRUS_SERVER_URL(response.getPropertyAsString("RedirectURL").toString());
		citrusconstant.setVANITYURL(response.getPropertyAsString("TransportalId").toString());
		MakeMyPayments.dynamic_retrunURL=response.getPropertyAsString("Returnurl").toString();
		mapcitrusconstant.put(citrusconstant.getVANITYURL(), citrusconstant);
		
	}


	public Map<String, CitrusConstantsObj> getMapcitrusconstants() {
		return mapcitrusconstants;
	}


	public void setMapcitrusconstants(
			Map<String, CitrusConstantsObj> mapcitrusconstants) {
		this.mapcitrusconstants = mapcitrusconstants;
	}

	
}
