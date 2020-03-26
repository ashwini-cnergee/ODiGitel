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
import com.cnergee.mypage.obj.DataUsageObj;
import com.cnergee.mypage.utils.Utils;

public class DataUsageSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public DataUsageObj datausageData;
	private Map<String, DataUsageObj> mapdatausagedetails=new Hashtable<String, DataUsageObj>();
	private boolean isAllData;
	
	
	public DataUsageSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	
	public String CallSearchMemberSOAP(long memberid )throws SocketException,SocketTimeoutException,Exception {
try{
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
		pi.setValue(memberid);//370
		pi.setType(Long.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId); //CM000001DB
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
			Utils.log(" REQUEST ", androidHttpTransport.requestDump);
			Utils.log(" RESPONSE ", androidHttpTransport.responseDump);
			// Object response2 = envelope.getResponse();
			 //Log.i(" RESPONSE ",response2.toString());
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, DataUsageObj> mapdatausage = new Hashtable<String, DataUsageObj>();
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i(" RESPONSE ", response.toString());
				if (response != null) {
					response = (SoapObject) response.getProperty("table");
					if (response.getPropertyCount() > 0 ) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response
									.getProperty(i);
							//Log.i("-------This is Test--------","-----Condition Failed-----");
							try
							{
								
							setUsageDetails(tableObj);
							}
							catch(Exception e) {
								e.printStackTrace();
								str_msg = "Data usage not found";
								return str_msg;
							}
						}
						setMapdatausagedetails(mapdatausagedetails);
						
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
catch(Exception e){
	Utils.log("Datausage",":"+e);
	return "error";
	
}
	}
	
	
	public void setUsageDetails(SoapObject response) 
	{
		/*Utils.log("Datausage"," TotalDataTransfer:"+response.getPropertyAsString("TotalDataTransfer").toString());
		Utils.log("Datausage","AllotedData:"+response.getPropertyAsString("AllotedData").toString());
		
		Utils.log("Datausage","RemainData:"+response.getPropertyAsString("RemainData").toString());*/
		DataUsageObj datausage = new DataUsageObj();
		
		if(response.hasProperty("SessionTime"))
		datausage.setSessionTime(response.getPropertyAsString("SessionTime").toString());
		else
		datausage.setSessionTime("0");	
		
		if(response.hasProperty("ActiveTime"))
		datausage.setActiveTime(response.getPropertyAsString("ActiveTime").toString());
		else
		datausage.setActiveTime("0");	
		if(response.hasProperty("DownloadData"))
		datausage.setDownLoadData(response.getPropertyAsString("DownloadData").toString());
		else
		datausage.setDownLoadData("0");
		
		if(response.hasProperty("UploadData"))
		datausage.setUpLoadData(response.getPropertyAsString("UploadData").toString());
		else
		datausage.setUpLoadData("0");
		
		if(response.hasProperty("TotalDataTransfer"))
		datausage.setTotalDataTransfer(response.getPropertyAsString("TotalDataTransfer").toString());
		else
		datausage.setTotalDataTransfer("0");
		
		if(response.hasProperty("AllotedData"))
		datausage.setAllotedData(response.getPropertyAsString("AllotedData").toString());
		else
		datausage.setAllotedData("0");	
		
		if(response.hasProperty("RemainData"))
		datausage.setRemainData(response.getPropertyAsString("RemainData").toString());
		else
		datausage.setRemainData("0");	
		
		mapdatausagedetails.put(datausage.getActiveTime(), datausage);
		
	}


	public Map<String, DataUsageObj> getMapdatausagedetails() {
		return mapdatausagedetails;
	}


	public void setMapdatausagedetails(Map<String, DataUsageObj> mapdatausagedetails) {
		this.mapdatausagedetails = mapdatausagedetails;
	}
	
	
	/**
	 * @param mapMemberData
	 *            the mapMemberData to set
	 */
	


}
