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
import com.cnergee.mypage.utils.Utils;

public class AuthenticationSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private static final String TAG = "MyPage"; 
	private String mobilenumber;
	private String userid;
	private String address;
	private String membername;
	private boolean isvalid;
	private String Value;


	
	public AuthenticationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		//Log.i(" #	#####################  ", " START ");
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallAuthenticationSOAP(String mobilenumber,String userid
			)throws SocketException,SocketTimeoutException,Exception {
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" #	#####################  ", mobilenumber);

		/*Log.i(TAG+" username ", username);
		Log.i(TAG+" password ", password);
		Log.i(TAG+" IMEI No ", Authobj.getIMEINo());
		Log.i(TAG+" SIM SR.No ", Authobj.getMobileNumber());
		Log.i(TAG+" Mobile User ", Authobj.getMobLoginId());
		Log.i(TAG+" Mobile Password ", Authobj.getMobUserPass());
		Log.i(TAG+" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(TAG+" METHOD_NAME ", METHOD_NAME);
		Log.i(TAG+" SOAP_ACTION ", SOAP_URL + METHOD_NAME);
*/
		

		/*Authentication aut = new Authentication();
		aut.setVendorCode(Authenticat_VendorCode);
		aut.setUserName(Authenticat_user);
		aut.setPassword(Authenticat_pass);*/

		PropertyInfo pi = new PropertyInfo();
		pi.setName("primaryphone");
		pi.setValue(mobilenumber);
		pi.setType(String.class);
		request.addProperty(pi);
		
		
		pi = new PropertyInfo();
		pi.setName("memberid");
		pi.setValue(userid);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		// envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>Request<<<<<",request.toString());
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		// envelope.setOutputSoapObject(request);
		envelope.implicitTypes = true;
		
		//envelope.addMapping(WSDL_TARGET_NAMESPACE,"",this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
		
		
		Object response = envelope.getResponse();
		
		Utils.log("Response Authentication",""+response);
		
		/*JSONArray arr = new JSONArray(response.toString());
		
		JSONObject jObj = arr.getJSONObject(1);
		Log.i("MyCheckPoint",jObj.toString());
		//JSONObject jObj1 = jObj.getJSONObject("Table");
		String Value = jObj.getString("myCheckPoint");*/
		
		//JSONObject jsonObject= new JSONObject(response.toString());
		//JSONObject jsonObject1 = jsonObject.getJSONObject("NewDataSet");
		//JSONArray jsonObject2 = jsonObject1.getJSONArray("Table");
		//JSONArray jsonObject3 = jsonObject2.getJSONArray(0);
		
		//JSONObject NewDataSet = new JSONObject(jsonObject.getString("NewDataSet"));
		//JSONObject Table = new JSONObject(NewDataSet.getString("Table"));
		//JSONObject myCheckPoint = new JSONObject(jsonObject.getString("myCheckPoint"));
		
		JSONObject Obj = new JSONObject(response.toString());
		
		JSONArray  Obj1 = Obj.getJSONArray("NewDataSet");
		//Log.i("MyCheckPoint",Obj.toString());
		//JSONArray Obj2 = Obj1.JSONArray("Table");
		
		for(int i=0; i<Obj1.length(); i++)
		{
		        JSONObject obj=Obj1.getJSONObject(i);
		        JSONArray Obj2 = obj.getJSONArray("Table");
		        
		        for(int j=0; j< Obj2.length(); j++)
		        {
		        	JSONObject obj1 =Obj2.getJSONObject(j);
		        	
		        	Value = obj1.getString("myCheckPoint");
		        	
		        }
		        //String value = obj.getString("FieldName"); 
		        
		}
		
		String AuthCode = Value;
		//Log.i("MyCheckPoint",AuthCode);
		
		try{
			if(Integer.parseInt(AuthCode) == 1 )
			{	
				//Log.i("MyCheckPoint","true");
				setIsValidUser(true);	
				setMobilenumber(AuthCode);
				
			}
			else
			{
				//Log.i("MyCheckPoint","false");
				setIsValidUser(false);
			}
		}catch(NumberFormatException n){
			//Log.i(TAG+" LOGIN RESPONSE ",n.getMessage());
		}
		return "OK";
	}
	
	
	
	
	
	

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	
	

}
