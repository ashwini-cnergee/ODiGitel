package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.database.Cursor;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;


public class SendMessageSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	DatabaseAdapter dbAdapter;
	
	private String METHOD_NAME;
	public static int messageCount=0;
	public SendMessageSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}
	
	public String sendServerMessage(String Sender_Id,Context ctx,String GroupId)throws SocketException,SocketTimeoutException,Exception{
		try{
		String str_msg="ok";
		dbAdapter= new DatabaseAdapter(ctx);
		dbAdapter.open();
		Cursor mCur=dbAdapter.getMessagesToSend();
		//dbAdapter.close();
		if (mCur.getCount() > 0) {
			while (mCur.moveToNext()) {
				Utils.log("SendMessageSOAP In SOAP","Cursor rows :"+mCur.getCount());
				try {
					SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
							METHOD_NAME);

					PropertyInfo pi;
					pi = new PropertyInfo();
					pi.setName("MemberId");
					pi.setValue(Sender_Id);
					pi.setType(String.class);
					request.addProperty(pi);
					
					pi = new PropertyInfo();
					pi.setName(AuthenticationMobile.CliectAccessName);
					pi.setValue(AuthenticationMobile.CliectAccessId);
					pi.setType(String.class);
					request.addProperty(pi);
					
					int id=mCur.getInt(mCur
							.getColumnIndex(TableConstants.ROW_ID));
					Utils.log("SEND ID","is: "+id);
					/*String message=mCur.getString(mCur
							.getColumnIndex(TableConstants.MESSAGE));
									
					String type=mCur.getString(mCur
							.getColumnIndex(TableConstants.MESSAGE_TYPE));
					
					String path=mCur.getString(mCur
							.getColumnIndex(TableConstants.PATH));
					String url=mCur.getString(mCur
							.getColumnIndex(TableConstants.URL));
					
					String source_id=mCur.getString(mCur
							.getColumnIndex(TableConstants.SOURCE_ID));*/
					
					pi = new PropertyInfo();
					pi.setName("Message");
					pi.setValue(mCur.getString(mCur
							.getColumnIndex(TableConstants.MESSAGE)));
					pi.setType(String.class);
					request.addProperty(pi);
					
					pi = new PropertyInfo();
					pi.setName("MessageType");
					pi.setValue("0");
					pi.setType(String.class);
					request.addProperty(pi);
					
					/*pi = new PropertyInfo();
					pi.setName("MessageType");
					pi.setValue(mCur.getString(mCur
							.getColumnIndex(TableConstants.MESSAGE_TYPE)));
					pi.setType(String.class);
					request.addProperty(pi);
					*/
					/*pi = new PropertyInfo();
					pi.setName("MessagePath");
					pi.setValue(mCur.getString(mCur
							.getColumnIndex(TableConstants.PATH)));
					pi.setType(String.class);
					request.addProperty(pi);*/
					
					pi = new PropertyInfo();
					pi.setName("Fileurl");
					pi.setValue(mCur.getString(mCur
							.getColumnIndex(TableConstants.URL)));
					pi.setType(String.class);
					request.addProperty(pi);
					
				/*	pi = new PropertyInfo();
					pi.setName("Receiverid");
					pi.setValue(mCur.getString(mCur
							.getColumnIndex(TableConstants.SOURCE_ID)));
					pi.setType(String.class);
					request.addProperty(pi);
					*/
					Utils.log("send clicked","receiver: "+mCur.getString(mCur
							.getColumnIndex(TableConstants.SOURCE_ID)));
				/*	pi = new PropertyInfo();
					pi.setName("Receiverid");
					pi.setValue("2");
					pi.setType(String.class);
					request.addProperty(pi);*/
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					envelope.encodingStyle = SoapSerializationEnvelope.ENC;

					envelope.implicitTypes = true;
					envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
							this.getClass());

					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							SOAP_URL);
					androidHttpTransport.debug = true;
					try {
						androidHttpTransport.call(WSDL_TARGET_NAMESPACE
								+ METHOD_NAME, envelope);
						Utils.log("SendMessageSOAP request", "is: "
								+ androidHttpTransport.requestDump);
						Utils.log("SendMessageSOAP response", "is: "
								+ androidHttpTransport.responseDump);
						String response=envelope.getResponse().toString();
						Utils.log("UPDATE RESPONSE ","ROW :"+response);
						if(Integer.valueOf(response)>0){
							Utils.log("UPDATE ID","is: "+id);
							dbAdapter.UpdateSyncStatus(String.valueOf(id));
						}else{
							str_msg = "error";
							Utils.log("SendMessageSOAP no response ", "error: " +response );
							return str_msg;
						}

					} catch (Exception e) {
						dbAdapter.close();
						str_msg = "error";
						Utils.log("SendMessageSOAP inner try", "error: " + e);
						return str_msg;
					}
					
					//return str_msg;
				} catch (Exception e) {
					dbAdapter.close();
					str_msg = "error";
					Utils.log("SendMessageSOAP main try", "error: " + e);
					return str_msg;
				}
			}
		}
		dbAdapter.close();
		return "ok";
	}
	catch(Exception e){
		dbAdapter.close();
		return null;
	}
	}
}
