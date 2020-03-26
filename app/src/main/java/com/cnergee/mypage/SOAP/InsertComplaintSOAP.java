package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.utils.Utils;


public class InsertComplaintSOAP {
	

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	
	private ComplaintObj complaintobj;
	private String refNo;
	private String memberid;
	private String responseMsg;
	private String membercomplaintno;
	private String comments;
	
	public InsertComplaintSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	

public String CalComplaintSOAP()throws SocketException,SocketTimeoutException,Exception {
		
		/*Log.i("#####################", " START ");
		Log.i("WSDL_TARGET_NAMESPACE :", WSDL_TARGET_NAMESPACE);
		Log.i("SOAP_URL :", SOAP_URL);
		Log.i("SOAP_ACTION :", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i("METHOD_NAME :", METHOD_NAME);
		Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" MemberId ", getCollectionObj().getMemberLoginId());
		Log.i(" PayPickUpId ", getCollectionObj().getpaypickid());
		Log.i(" TypeName ", getCollectionObj().getTypeName());
		Log.i(" CallBackDate ", getCollectionObj().getCBDate());
		Log.i(" Comment ", getCollectionObj().getComment());
		Log.i(" UserName ",getUsername() );*/
	
		
		
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName("MemberId");
		pi.setValue(getComplaintobj().getMemberId());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("MemberComplaintNo");
		pi.setValue("");
		pi.setType(String.class);
		request.addProperty(pi);
	
		pi = new PropertyInfo();
		pi.setName("Comments");
		pi.setValue(getComplaintobj().getMessage());
		pi.setType(String.class);
		request.addProperty(pi);
		
		pi = new PropertyInfo();
		pi.setName("ComplaintId");
		pi.setValue(getComplaintobj().getComplaintId());
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
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<<", request.toString());
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;
	   Utils.log("ComplaintSOAP","request:"+request.toString());
		try {
			//Log.i("#####################", " CALL ");
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log("ComplaintSOAP","request:"+androidHttpTransport.requestDump);
			Utils.log("ComplaintSOAP","response:"+androidHttpTransport.responseDump);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Object response = envelope.getResponse();
				setResponseMsg(response.toString());
				//Log.i(" >>>> " ,response.toString());
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
			//Log.i("#####################", " RESPONSE ");
		
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			Utils.log("ComplaintSOAP","Error:"+e);
			return e.toString();
		}
	}



	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}



	public ComplaintObj getComplaintobj() {
		return complaintobj;
	}



	public void setComplaintobj(ComplaintObj complaintobj) {
		this.complaintobj = complaintobj;
	}



	public String getMemberid() {
		return memberid;
	}



	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}



	public String getMembercomplaintno() {
		return membercomplaintno;
	}



	public void setMembercomplaintno(String membercomplaintno) {
		this.membercomplaintno = membercomplaintno;
	}



	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
	
}
