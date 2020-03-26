package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.ComplaintCategoryList;

public class ComplaintCategorySOAP <E>{
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private Map<String, ComplaintCategoryList> mapComplaintCategoryList;
	private ArrayList<ComplaintCategoryList> complaintcategorylist = new ArrayList<ComplaintCategoryList>();
	
	
	public ComplaintCategorySOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallPackageSOAP() throws SocketException,SocketTimeoutException,Exception {

		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);*/
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" AreaCode ",getAreaCode());*/
		//Log.i("#####################", "");

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<", request.toString());
		
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;

/*		try{
			Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			SoapObject soapObject = (SoapObject) envelope.getResponse();
			int tot_count = soapObject.getAttributeCount();
		
			Log.i("##################### tot_count ", " "+tot_count);
			Log.i("#####################", " END ");
			return "ok";
		}catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}*/
		try {
			//Log.i("#####################", " CALL ");
			
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			String ss = androidHttpTransport.responseDump;
			//Log.i(">>>>>>>Data Package <<<<<<", ss);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
							ComplaintCategoryList obj = new ComplaintCategoryList();
							obj.setComplaintId(tableObj.getPropertyAsString("ComplaintId").toString());
							obj.setComnplaintName(tableObj.getPropertyAsString("ComplaintName").toString());
							
							complaintcategorylist.add(obj);						
						}
						
					} else {
						complaintcategorylist.add(new ComplaintCategoryList());	
					}
				} else {
					complaintcategorylist.add(new ComplaintCategoryList());
				}

			}
				
			/*String ss = androidHttpTransport.responseDump;
			Log.i(">>>>>>>Data Package <<<<<<", ss);
			setXMLData(ss);*/
			//Log.i("#####################", " DONE ");
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	private String strXML;

	public void setXMLData(String strXML) {
		this.strXML = strXML;
	}

	public String getXMLData() {
		return strXML;
	}

	public ArrayList<ComplaintCategoryList> GetComplaintList()
	{
		return complaintcategorylist;
		
	}
}
