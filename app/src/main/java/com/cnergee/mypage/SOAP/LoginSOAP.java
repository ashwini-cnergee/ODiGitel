package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.LoginObj;
import com.cnergee.mypage.utils.Utils;

public class LoginSOAP {
	
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "MyPage"; 
	private String mobilenumber;
	private boolean isvalid;
	private String Authcount;
	private String Value;
	private String responseMsg;
	
	LoginObj Login = new LoginObj();
	
	
	public LoginSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		//Log.i(" #	#####################  ", " START ");
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallLoginSOAP(String mobilenumber
			)throws SocketException,SocketTimeoutException,Exception {
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" #	#####################  ", mobilenumber);
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName("Primaryphoneno");
		pi.setValue(mobilenumber);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		Utils.log("Mobile Number",":"+mobilenumber);
		Utils.log("Authentication Object",":"+AuthenticationMobile.CliectAccessId);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		// envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		// envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE,"",this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		Object response = envelope.getResponse();
		Utils.log(TAG+" LOGIN REQUEST ",request.toString());
		Utils.log(TAG+" LOGIN RESPONSE ",response.toString());
		
		JSONObject Obj = new JSONObject(response.toString());
		
		JSONArray  Obj1 = Obj.getJSONArray("NewDataSet");
		
		for(int i=0; i<Obj1.length(); i++)
		{
		        JSONObject obj=Obj1.getJSONObject(i);
		        JSONArray Obj2 = obj.getJSONArray("Table");
		        
		        for(int j=0; j< Obj2.length(); j++)
		        {
		        	JSONObject obj1 =Obj2.getJSONObject(j);
		        	
		        	Value = obj1.getString("myCheckPoint");
		        	//Utils.log("Item name: ", Value);
		        }
		        //String value = obj.getString("FieldName"); 
		        
		}
		
		String AuthCode = Value;
		//Log.i("MyCheckPoint",AuthCode);
		
		try{
			if(Integer.parseInt(AuthCode) == 1 ){
				setIsValidUser(true);
				setAuthcount(AuthCode);
				
				
			}else if(Integer.parseInt(AuthCode) > 1){
				setIsValidUser(true);
				setAuthcount(AuthCode);
				
			}else
			{
				setIsValidUser(false);
			}
		}catch(NumberFormatException n){
			//Log.i(TAG+" LOGIN RESPONSE ",n.getMessage());
		}
		return "OK";
	}

	
	public void setIsValidUser(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public boolean isValidUser() {
		return isvalid;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getAuthcount() {
		return Authcount;
	}

	public void setAuthcount(String authcount) {
		Authcount = authcount;
	}

	

	
	
	

}
