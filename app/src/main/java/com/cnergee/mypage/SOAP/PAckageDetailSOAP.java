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

import android.util.Log;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.Utils;

public class PAckageDetailSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public PackageDetails packgedetailsData;
	private Map<String, PackageDetails> mapPackageDetails;

	public PAckageDetailSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallSearchMemberSOAP(String memberloginid )throws SocketException,SocketTimeoutException,Exception {

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" searchTxt ", searchTxt);
		//Log.i(" #	#####################  ", " START ");

		//Log.i(" username ", username);
		//Log.i(" password ", password);
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());*/
		Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		//Log.i(" searchTxt ", searchTxt);
		//Log.i("#####################", "");

		PropertyInfo pi = new PropertyInfo();
		pi.setName("SubscriberId");
		pi.setValue(memberloginid);
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


		String str_msg = "ok";
		Utils.log(""+this.getClass().getSimpleName()," Package Details Called");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log(this.getClass().getSimpleName(),"Request"+androidHttpTransport.requestDump);
			Utils.log(this.getClass().getSimpleName(),"Response"+androidHttpTransport.responseDump);
			 Object response2 = envelope.getResponse();
			// Log.i(" RESPONSE ",response2.toString());


			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, PackageDetails> mapPackageDetails = new Hashtable<String, PackageDetails>();
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					//Log.i(" RESPONSE ", response.toString());
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response
									.getProperty(i);
							setPackageDetails(tableObj, mapPackageDetails);
						}
						setMapPackageDetails(mapPackageDetails);
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


	public void setPackageDetails(SoapObject response,
			Map<String, PackageDetails> mapPackageDetails) {

		//Log.i("#####################",
		//		response.getPropertyAsString("SubscriberID").toString() + "");

		PackageDetails packagedetails = new PackageDetails();

		packagedetails.setPackageName(response.getPropertyAsString("CurrentPlan")
				.toString());
		packagedetails.setAmount(response.getPropertyAsString("PlanRate").toString());

		packagedetails.setPackageValidity(response.getPropertyAsString("PackageValidity")
				.toString());
		packagedetails.setExpiryDate(response.getPropertyAsString("expiryDate")
				.toString());
		packagedetails.setMemberLoginId(response.getPropertyAsString("SubscriberID")
				.toString());
		packagedetails.setMemberRegsiterDate(response.getPropertyAsString("MemberFromDt")
				.toString());

		packagedetails.setIpAddress(response.getPropertyAsString("IPAddress")
				.toString());

		packagedetails.setServiceTax(response.getPropertyAsString("ServiceTax")
				.toString());

		packagedetails.setAreaCode(response.getPropertyAsString("AreaCode")
				.toString());

		packagedetails.setIsFreePackage(response.getPropertyAsString("IsFreePackage")
				.toString());

		packagedetails.setCheckForRenewal(response.getPropertyAsString("CheckForRenewal")
				.toString());


		if(response.getPropertyAsString("Is24Online").equalsIgnoreCase("1")){
			packagedetails.setIs_24ol(true);
		}
		else{
			packagedetails.setIs_24ol(false);
		}

		if(response.hasProperty("Atom")){
			Utils.log("Atom from Web", ""+response.getPropertyAsString("Atom").toString());
			packagedetails.setIsAtom(Integer.parseInt(response.getPropertyAsString("Atom").toString()));
		}
		else{
			packagedetails.setIsAtom(0);
		}
		if(response.hasProperty("Atom_Message"))
			Utils.Atom_Message=response.getPropertyAsString("Atom_Message").toString();

		packagedetails.setSubscriberName(response.getPropertyAsString("SubscriberName").toString());
		packagedetails.setConnectionTypeId(response.getPropertyAsString("ConnectionTypeId").toString());

		if(response.hasProperty("CCAvenue"))
			packagedetails.setIsCC_Avenue(Integer.parseInt(response.getPropertyAsString("CCAvenue").toString()));
		else
		packagedetails.setIsCC_Avenue(0);

		if(response.hasProperty("PAYUMONEY")){
			packagedetails.setPayuMoney(Integer.parseInt(response.getPropertyAsString("PAYUMONEY").toString()));
		}else{
			packagedetails.setPayuMoney(0);
		}

		if(response.hasProperty("AggrePay")){
			packagedetails.setAggrePay(Integer.parseInt(response.getPropertyAsString("AggrePay").toString()));
		}else{
			packagedetails.setAggrePay(0);
		}

		if(response.hasProperty("CCAvenue_Message"))
			Utils.CCAvenue_Message=response.getPropertyAsString("CCAvenue_Message").toString();

		if(response.hasProperty("Citrus"))
			packagedetails.setIs_citrus(Integer.parseInt(response.getPropertyAsString("Citrus").toString()));
			else
			packagedetails.setIs_citrus(0);

		if(response.hasProperty("Citrus_Message"))
			Utils.CITRUS_MESSAGE=response.getPropertyAsString("Citrus_Message").toString();

		mapPackageDetails.put(packagedetails.getPackageValidity(), packagedetails);

	}

	public void setMapPackageDetails(Map<String, PackageDetails> mapPackageDetails) {
		this.mapPackageDetails = mapPackageDetails;
	}

	public Map<String, PackageDetails> getMapPackageDetails() {
		return this.mapPackageDetails;
	}


}