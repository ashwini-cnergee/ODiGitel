package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;




public class OfferDetailsSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "OfferDetailsSOAP"; 
	private ArrayList<String> alOfferLinks; 
	private ArrayList<String> alOfferNames;
	
	public OfferDetailsSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String getOffers(String MemberId)throws SocketException,SocketTimeoutException,Exception{
		String str_msg = "ok";
		alOfferLinks= new ArrayList<String>();
		alOfferNames= new ArrayList<String>();
		try{
			//Utils.log(TAG+"id",": "+MemberId);
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(MemberId);
		pi.setType(Long.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		//Log.i(">>>>>Request<<<<<", request.toString());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		/*Utils.log(TAG,"request :"+androidHttpTransport.requestDump);
		Utils.log(TAG ,"response: "+androidHttpTransport.responseDump);*/
		Utils.log(TAG,"request:"+ androidHttpTransport.requestDump);
		Utils.log(TAG,"response:"+ androidHttpTransport.responseDump);
		str_msg="ok";
		if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
			try{
			SoapObject response = (SoapObject) envelope.getResponse();
			
			if (response != null) {
				
				response = (SoapObject) response.getProperty("NewDataSet");
				if (response.getPropertyCount() > 0) {
					for (int i = 0; i < response.getPropertyCount(); i++) 
					{
						SoapObject tableObj = (SoapObject) response.getProperty(i);
						alOfferLinks.add(tableObj.getPropertyAsString("Description"));
						alOfferNames.add(tableObj.getPropertyAsString("DownloadFile"));
						/*Utils.log("DownloadLink","s: "+tableObj.getPropertyAsString("Description"));
						Utils.log("DownloadFile","s: "+tableObj.getPropertyAsString("DownloadFile"));*/
					}
					
				} 
			}
			}
			catch(Exception e){
				//Utils.log("Soap Exception 94 ","is:"+e);
				str_msg="not";
				return str_msg;
			}
		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
															// FAILURE
			
			SoapFault soapFault = (SoapFault) envelope.bodyIn;
			//Utils.log("Soap Fault 102 ","is:"+soapFault);
			alOfferNames.add("No Special Offers!!!");
			alOfferLinks.add("no offers");
		}
		
		
		return str_msg;
		}
		catch (Exception e) {
			str_msg="not";
			return str_msg;
		}
	}
	
	public ArrayList<String> getOfferLinksArrayList(){
		return alOfferLinks;
	}
	
	public ArrayList<String> getOfferNamesArrayList(){
		return alOfferNames;
	}
}
