package common.SOAP;



import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.Authentication;
import com.cnergee.mypage.Marshals.MarshalDouble;
import com.cnergee.mypage.utils.Utils;



public class CommonSOAP {

	String SOAP_URL;
	String WSDL_TARGET_NAMESPACE;
	String METHOD_NAME;
	String response="";
	SoapObject request;
	
	public CommonSOAP(String SOAP_URL,String WSDL_TARGET_NAMESPACE,String METHOD_NAME) {
		this.SOAP_URL = SOAP_URL;
		this.WSDL_TARGET_NAMESPACE=WSDL_TARGET_NAMESPACE;
		this.METHOD_NAME=METHOD_NAME;
	}

	public String executeMethod()throws SocketException,SocketTimeoutException,Exception {
		
		Utils.log("Method","executed");
		Utils.log("URL"," is:"+SOAP_URL);
		String result="OK";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		
		MarshalDouble mdouble = new MarshalDouble();
		mdouble.register(envelope);
	
		 envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
					this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
		
		androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
				envelope);
		
		Utils.log(""+METHOD_NAME," request:"+androidHttpTransport.requestDump);
		Utils.log(""+METHOD_NAME," response:"+androidHttpTransport.responseDump);
		
		response=envelope.getResponse().toString();
		
		
		
		return result;
	}

	public SoapObject getRequest() {
		return request;
	}

	public void setRequest(SoapObject request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

		
}
