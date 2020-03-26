package com.cnergee.mypage.SOAP;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by Jyoti on 10/4/2016.
 */

public class GetPayUMoneySignatureSoap {


    private String SOAP_URL;
    private String WSDL_TARGET_NAMESPACE;
    private String METHOD_NAME;
    public static String TAG = "GetAtomSignatureSoap";
    public String response;



    public GetPayUMoneySignatureSoap(String WSDL_TARGET_NAMESPACE,String SOAP_URL,String METHOD_NAME){
        this.METHOD_NAME = METHOD_NAME;
        this.SOAP_URL = SOAP_URL;
        this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;

    }

    public String GetPayUMoneySignatureResult(long MemberId,String BankName,String TrackId,String Amount)throws Exception,SocketException,SocketTimeoutException {

        String Result = "OK";

        try{
            Utils.log(""+this.getClass().getSimpleName(),":"+SOAP_URL);
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("MemberId");
            Utils.log(TAG+"member:",""+MemberId);
            pi.setValue(MemberId);//78846
            pi.setType(long.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("BankCode");
            pi.setValue(BankName);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("TrackId");
            pi.setValue(TrackId);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Amount");
            pi.setValue(Amount);
            Utils.log("Amount ", "" + Amount);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName(AuthenticationMobile.CliectAccessName);
            pi.setValue(AuthenticationMobile.CliectAccessId);
            pi.setType(String.class);
            request.addProperty(pi);




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.encodingStyle =  SoapSerializationEnvelope.ENC;
            envelope.implicitTypes = true;
            envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
                    this.getClass());

            HttpTransportSE transport = new HttpTransportSE(SOAP_URL);
            transport.debug=true;


            transport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME, envelope);
            Utils.log("GEPAYUMONEY request",""+transport.requestDump);
            Utils.log("GEPAYUMONEY response",""+transport.responseDump);

            Utils.log("GetPayUMoneySignatureSoap",""+request.toString());


            response = envelope.getResponse().toString();
            Utils.log("GetPayUMoneySignatureSoap", "" + response.toString());
        }catch(Exception e){
            Utils.log("Error",":"+e);
        }



        return Result;

    }

    public String getResponse() {
        // TODO Auto-generated method stub
        return response;
    }


}
