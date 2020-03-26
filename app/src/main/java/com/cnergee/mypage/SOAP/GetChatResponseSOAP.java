package com.cnergee.mypage.SOAP;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;

public class GetChatResponseSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	String MainResponse;
	Boolean chat_status,chat_response,wait_status;
	
	public GetChatResponseSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;

		}
	
	public String getServerMessage(String Profile_Id,Context ctx){
		String result="OK";
		
		
		try{
			SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
			
			PropertyInfo pi;
			pi= new PropertyInfo();
			pi.setName("MemberId");
			pi.setValue(Profile_Id);
			pi.setType(String.class);
			request.addProperty(pi);
			
			pi= new PropertyInfo();
			pi.setName(AuthenticationMobile.CliectAccessName);
			pi.setValue(AuthenticationMobile.CliectAccessId);
			pi.setType(String.class);
			request.addProperty(pi);
			
			SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			envelope.encodingStyle=SoapSerializationEnvelope.ENC;
			
			envelope.implicitTypes=true;
			envelope.addMapping(WSDL_TARGET_NAMESPACE, "", this.getClass());
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
			androidHttpTransport.debug = true;
			try{
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);
			Utils.log("GetChatResponseSOAP SOAP request","is: "+androidHttpTransport.requestDump);
			Utils.log("GetChatResponseSOAP SOAP response","is: "+androidHttpTransport.responseDump);
			
			
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				
				SoapObject response = (SoapObject) envelope.getResponse();
				if (response != null) {
					response = (SoapObject) response.getProperty("NewDataSet");
					
					
					
					if (response.getPropertyCount() > 0) {
						Utils.log("GetChatResponseSOAP SOAP ","Response count: "+response.getPropertyCount());
						
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
							
							wait_status= Boolean.valueOf(tableObj.getPropertyAsString("ChatRequest"));
							chat_response=Boolean.valueOf(tableObj.getPropertyAsString("ChatResponse"));
							chat_status=Boolean.valueOf(tableObj.getPropertyAsString("ChatStatus"));
							MainResponse="1";
							
							/*if(Boolean.valueOf(tableObj.getPropertyAsString("ChatResponse"))){
								
								String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
								SharedPreferences sharedPreferences = ctx.getApplicationContext()
										.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
								
								SharedPreferences.Editor editor= sharedPreferences.edit();
								editor.putBoolean("chat_status", true);
								editor.commit();
								MainResponse="1";
							}
							else{
								
								String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
								SharedPreferences sharedPreferences = ctx.getApplicationContext()
										.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
								
								SharedPreferences.Editor editor= sharedPreferences.edit();
								editor.putBoolean("chat_status", false);
								editor.commit();
								MainResponse="0";
								
							}*/
							
							/*if(Boolean.valueOf(tableObj.getPropertyAsString("ChatStatus"))){
								
								String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
								SharedPreferences sharedPreferences = ctx.getApplicationContext()
										.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
								
								SharedPreferences.Editor editor= sharedPreferences.edit();
								wait_status= Boolean.valueOf(tableObj.getPropertyAsString("ChatRequest"));
								chat_response=Boolean.valueOf(tableObj.getPropertyAsString("ChatResponse"));
								chat_status=Boolean.valueOf(tableObj.getPropertyAsString("ChatStatus"));
								
								editor.putBoolean("request_status", chat_status);
								editor.putBoolean("chat_status", chat_response);
								editor.putBoolean("wait_status",wait_status);
								editor.commit();
								MainResponse="1";
							}
							else{
								
								if(Boolean.valueOf(tableObj.getPropertyAsString("ChatRequest"))){
									
									
								}
								else{
									
								}
								String sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
								SharedPreferences sharedPreferences = ctx.getApplicationContext()
										.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
								
								SharedPreferences.Editor editor= sharedPreferences.edit();
								editor.putBoolean("chat_status", false);
								editor.commit();
								MainResponse="0";
								
							}*/
						}
						
						
					} else {
						
					}
				} else {
				//	Utils.log("GetServerMessage SOAP ","no Response: ");
				}

			}
				else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
				// FAILURE
					
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				result="error";
	// throw new Exception(soapFault.getMessage());
				return result;
				}
			
			
			}
			catch(Exception e){
				
				return result;
			}
			return result;
			}
			catch(Exception e){
				
				return result;
			}
		
		
	}
	public String getResponse(){
		
		return MainResponse;
	}

	public Boolean getChat_status() {
		return chat_status;
	}

	public void setChat_status(Boolean chat_status) {
		this.chat_status = chat_status;
	}

	public Boolean getChat_response() {
		return chat_response;
	}

	public void setChat_response(Boolean chat_response) {
		this.chat_response = chat_response;
	}

	public Boolean getWait_status() {
		return wait_status;
	}

	public void setWait_status(Boolean wait_status) {
		this.wait_status = wait_status;
	}

}
