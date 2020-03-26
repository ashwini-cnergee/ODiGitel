package com.cnergee.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.utils.Utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import cnergee.krishna.ookla.odigitel.R;

public class GetAppVersionAsyncTask extends AsyncTask<String, Void, Void>{
	Context ctx;
	String calc_version_result="",calc_version_response="",AppVersion="";
	String	sharedPreferences_name="";
	Utils utils= new Utils();
	String MemberId="",memberloginid="";
	public GetAppVersionAsyncTask(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx=ctx;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		sharedPreferences_name = ctx.getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = ctx.getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
		
		
		utils.setSharedPreferences(sharedPreferences);
		MemberId = utils.getMemberId();
		memberloginid = utils.getMemberLoginID();
		
		PackageInfo pInfo;
		try {
			pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			AppVersion= pInfo.versionName;				
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		GetCurrentVersionSOAP getCurrentVersionsoap= new GetCurrentVersionSOAP(ctx.getString(R.string.WSDL_TARGET_NAMESPACE), ctx.getString(R.string.SOAP_URL), ctx.getString(R.string.METHOD_GET_CURRENT_VERSION));
		try {
			calc_version_result=getCurrentVersionsoap.getCurrentVersion("A", AppVersion,MemberId);
			calc_version_response=getCurrentVersionsoap.getResponse();
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			calc_version_result="Internet is too slow";
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			calc_version_result="Internet is too slow";
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			calc_version_result="Please try again!!";
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		SharedPreferences sharedPreferences = ctx.getSharedPreferences(sharedPreferences_name, 0);
		if(calc_version_result.length()>0){
			if(calc_version_result.equalsIgnoreCase("ok")){
				Utils.log("Version",":"+calc_version_response);
				String version_response[]=calc_version_response.split("#");
				if(version_response.length>1){
					
					if(version_response[1].equalsIgnoreCase(AppVersion)){
						
					}
					else{
					
						if(version_response[1].equalsIgnoreCase("True")){
						/*	SharedPreferences.Editor edit=sharedPreferences.edit();
							edit.putString("App_Version", version_response[1]);
							edit.commit();*/
						}
						if(version_response[1].equalsIgnoreCase("False")){
							
							/*SharedPreferences.Editor edit=sharedPreferences.edit();
							edit.putString("App_Version", version_response[1]);
							edit.commit();*/
							
						
							
						}
					}
				}
			}
		}
	}

}
