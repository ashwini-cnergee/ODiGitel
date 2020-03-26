package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.ChangePackage;
import com.cnergee.mypage.PaymentPickup_New;
import com.cnergee.mypage.obj.AuthenticationMobile;

public class AreaWiseSettingSOAP {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	public AreaWiseSettingSOAP(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
		this.WSDL_TARGET_NAMESPACE=WSDL_TARGET_NAMESPACE;
		this.SOAP_URL=SOAP_URL;
		this.METHOD_NAME=METHOD_NAME;
	}

	public String getAreaWiseSetting(String AreaCode,String Parameter) throws SocketException,SocketTimeoutException,Exception{
		String result="ok";
		
		try{
			
		SoapObject request= new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		
		PropertyInfo pi= new PropertyInfo();
		pi.setName("AreaId");
		pi.setValue(AreaCode);
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi= new PropertyInfo();
		pi.setName("SettingType");
		pi.setValue(Parameter);
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
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		try{
			
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
		//	Utils.log("AreaWiseSettingSOAP","request: "+androidHttpTransport.requestDump);
		//	Utils.log("AreaWiseSettingSOAP","response: "+androidHttpTransport.responseDump);
			
			
			if(Parameter.equalsIgnoreCase("PPR")){
				PaymentPickup_New.settingResult=envelope.getResponse().toString();
			}
			if(Parameter.equalsIgnoreCase("UP")){
				ChangePackage.settingResult=envelope.getResponse().toString();
			}
			result="ok";
		}
		catch(Exception e){
		//	Utils.log("inner try",": "+e);
			result="error";
		}
		
		}
		catch(Exception e){
			//Utils.log("main try",": "+e);
			result="error";
		}
		return result;
		
	}
}
