package com.cnergee.mypage;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cnergee.mypage.caller.ConfirmationCaller;
import com.cnergee.mypage.caller.InsertPhoneDetailsCaller;
import com.cnergee.mypage.obj.ConfirmationObj;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;

public class Confirmation extends Activity{
	public static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	public long memberid;
	public String Mobilenumber,MemberLoginId;
	TextView txtloginId,txtmobileno,
	txtusername,txtaddress,txtmemid,txtemialid;
	
	Button btnconfirm;
	
	private String sharedPreferences_name;
	public static Map<String, ConfirmationObj> mapConfirmationDetails;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	private String otp_password="";
	boolean isLogout = false;
	private InsertPhoneDetailsWebService InsertPhoneDetailsWebService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmation);
		
		txtloginId = (TextView)findViewById(R.id.txtloginId); 
		txtmobileno = (TextView)findViewById(R.id.txtmobileno);
		txtusername = (TextView)findViewById(R.id.txtusername);
		txtaddress = (TextView)findViewById(R.id.txtaddress);
		txtmemid = (TextView)findViewById(R.id.txtmemid);
		txtemialid = (TextView)findViewById(R.id.txtemail);

		context = this;
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		Intent intent = getIntent();
		
		otp_password=intent.getStringExtra("otp_sms");
		MemberLoginId=intent.getStringExtra("MemberLoginId");
		Mobilenumber=intent.getStringExtra("MobileNum");								
		getMemberDetailWebService = new GetMemberDetailWebService();
		getMemberDetailWebService.execute((String) null);
		//Utils.log("Confirmation SMS","s"+otp_password);
		btnconfirm = (Button)findViewById(R.id.btnconfirm);
		btnconfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//InsertPhoneDetailsWebService = new InsertPhoneDetailsWebService();
				//InsertPhoneDetailsWebService.execute((String) null);
				SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); 
				Utils.log("Shared preferences", "confirmation Excecuted");

				// 0 - for
														// private
														// mode
		//utils.clearSharedPreferences(sharedPreferences);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("MobileNoPrimary", txtmobileno.getText().toString());
		editor.putString("MemberLoginID", txtloginId.getText().toString());
		editor.putString("MemberId", txtmemid.getText().toString());
		editor.putString("otp_password", otp_password);
		editor.putString("MemberName", txtusername.getText().toString());
		editor.putString("getCustomerNumber",txtusername.getText().toString());
		editor.putString("EmailId",txtemialid.getText().toString());
		
		editor.commit();
				Confirmation.this.finish();
				Intent i = new Intent(Confirmation.this,IONHome.class);
				startActivity(i);
				Utils.log("in Confirmation intent", "confirmation intent Executed");
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
	
	
	private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				Confirmation.this);

		protected void onPreExecute() {
			Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);
			Dialog.show();
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			Dialog.dismiss();
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapConfirmationDetails != null) {
					
					Set<String> keys = mapConfirmationDetails.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					ConfirmationObj ConfirmationDetails = mapConfirmationDetails.get(selItem);
					Utils.log("getText in Confirmation","confirmation Executed");
					
					txtaddress.setText(ConfirmationDetails.getInstallationAddress());
					txtloginId.setText(ConfirmationDetails.getMemberLoginId());
					txtmobileno.setText(ConfirmationDetails.getMobileNoprimary());
					txtusername.setText(ConfirmationDetails.getCustomerName());
					txtmemid.setText(ConfirmationDetails.getMemberId());
					txtemialid.setText(ConfirmationDetails.getEmailId());
					SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
					SharedPreferences.Editor edit=sharedPreferences.edit();
					edit.putString("showLogo", ConfirmationDetails.getShowLogo());
					edit.putString("CustomerCareNo", ConfirmationDetails.getCustomerCareNumber());
					edit.commit();
					/* For Inserting Phone Details */
					Utils.log("doInBackGround getCustomerCareNumber","is:"+ConfirmationDetails.getCustomerCareNumber());
					
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){AlertsBoxFactory.showAlert(rslt,context );}	
		}
		@Override
		protected Void doInBackground(String... params) {
			try {
				ConfirmationCaller confirmationcaller = new ConfirmationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_MEMBER_DETAILS)
										);
				confirmationcaller.setAllData(true);
				confirmationcaller.MemberLoginId = MemberLoginId;
				confirmationcaller.Mobilenumber = Mobilenumber;
				
				confirmationcaller.join();
				confirmationcaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		 @Override
			protected void onCancelled() {
				Dialog.dismiss();
				getMemberDetailWebService = null;
			}
		}
	
	private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(Confirmation.this);
		PhoneDetailsOBJ phonedetailsobj = new PhoneDetailsOBJ();
		String versionName="";

		protected void onPreExecute() {
			Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.show();
			 try {
				 versionName = context.getPackageManager()
						    .getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {
			Dialog.dismiss();
			InsertPhoneDetailsWebService = null;
			//submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
			//submit.setClickable(true);
			InsertPhoneDetailsWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				
				
				
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				
				
				/*System.out.println(utils.getMemberId());
				System.out.println(android.os.Build.MODEL.toString());
				System.out.println(android.os.Build.VERSION.SDK_INT);
				System.out.println(Secure.getString(getContentResolver(), Secure.ANDROID_ID));
				System.out.println("Android");
				System.out.println("");*/
				
				
				phonedetailsobj.setMemberId(txtmemid.getText().toString());
				phonedetailsobj.setPhoneName(android.os.Build.MODEL.toString());
				
				phonedetailsobj.setPhoneVersion(android.os.Build.VERSION.RELEASE.toString());
				phonedetailsobj.setPhoneUniqueId(Secure.getString(getContentResolver(), Secure.ANDROID_ID));
				phonedetailsobj.setPhoneplatform("Android");
				phonedetailsobj.setPhonepackage("");
				phonedetailsobj.setAppVersion(versionName);
				
				InsertPhoneDetailsCaller caller = new InsertPhoneDetailsCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_INSERT_PHONE_DETAILS_WITH_APPVERSION));

				caller.setPhonedetailobj(phonedetailsobj);
				
				//caller.setMemberId(Long.parseLong(utils.getMemberId()));
				
	
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				/*AlertsBoxFactory.showAlert(rslt,context );*/
			}
			return null;
		}
	}

	
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
this.finish();
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();

}
}
