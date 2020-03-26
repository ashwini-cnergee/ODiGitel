package com.cnergee.mypage;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cnergee.mypage.caller.AuthenticationCaller;
import com.cnergee.mypage.caller.ConfirmationCallerAuthentication;
import com.cnergee.mypage.caller.InsertPhoneDetailsCaller;
import com.cnergee.mypage.obj.ConfirmationObj;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;


public class Authentication extends Activity implements OnCancelListener{
	Utils utils = new Utils();
	public static String rslt = "";
	Button btnsubmit,btncancel = null;
	EditText txtmobilenumber,txtuserid;
	public static boolean isVaildUser = false;
	public static String userId="";
	public static String mobilenumber="";
	private InsertPhoneDetailsWebService InsertPhoneDetailsWebService = null;
	public static Context context;
	//private String logtag = getClass().getSimpleName();
	private ValidUserWebService validUserWebService = null;
	//FontTypefaceHelper fontTypeface = new FontTypefaceHelper();
	private String sharedPreferences_name;
	//private boolean isFinish = false;
	public static String MemberId;
	public static String MemberLoginId;

	public static Map<String, ConfirmationObj> mapConfirmationDetails;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	boolean isLogout = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.authentication);
	
		context = this;
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		//
		txtmobilenumber = (EditText)findViewById(R.id.txtmobilenumber);
		txtuserid = (EditText)findViewById(R.id.txtuserid);
		
		Intent intent = getIntent();
		Bundle bundle  = intent.getExtras();
		
		txtmobilenumber.setText(bundle.getString("mobilenumber"));
		
		btnsubmit = (Button)findViewById(R.id.btnsubmit);
		btncancel = (Button)findViewById(R.id.btncancel);
		btnsubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (TextUtils.isEmpty(txtmobilenumber.getText().toString().trim())) {
					AlertsBoxFactory.showAlert(getString(R.string.app_please_enter_valid_label),context );
					return;
				} 
				else 
				{
					
					if (TextUtils.isEmpty(txtuserid.getText().toString().trim())) {
						AlertsBoxFactory.showAlert(getString(R.string.app_please_enter_valid_label_loginid),context );
						return;
					} 
					else
					{
						
						if(txtmobilenumber.getText().toString().startsWith("0"))
						{	
							AlertsBoxFactory.showAlert("Please enter valid mobile no.",context );
							return;
						}
						else
						{
							if(txtmobilenumber.getText().toString().length() > 10 || txtmobilenumber.getText().toString().length() < 10)
							{
								AlertsBoxFactory.showAlert("Please enter 10 digit mobile no.",context );
								return;
								
							}
							else
							{
								if(utils.isOnline(Authentication.this)){
								validUserWebService = new ValidUserWebService();
								validUserWebService.execute((String) null);
								}
								else{
									Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
								}
							}
						}
					}
				}
				
				
			}
		});
		
		btncancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Authentication.this.finish();
				Intent i = new Intent(Authentication.this, LoginFragmentActivity.class);
				i.putExtra("from", "2");
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
	}
	
	protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();

		//private ProgressDialog Dialog = new ProgressDialog(Authentication.this);
		ProgressHUD mProgressHUD;
		protected void onPreExecute() {
			//Dialog.setMessage(getString(R.string.app_please_wait_label));
			
			mProgressHUD = ProgressHUD.show(Authentication.this,getString(R.string.app_please_wait_label), true,true,this);
		//	mainDialog.setCancelable(false);
		//	mainDialog.show();
			
			//fontTypeface.dialogFontOverride(context,Dialog);
		
		}

		protected void onPostExecute(Void unused) {
			//DiaUtils.dismiss();
			mProgressHUD.dismiss();
			btnsubmit.setClickable(true);
			validUserWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				//isVaildUser = true;
				if (isVaildUser) {
					
					/*SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																	// private
																	// mode
					//utils.clearSharedPreferences(sharedPreferences);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("MobileNoPrimary", txtmobilenumber.getText().toString());
					editor.putString("MemberLoginID", txtuserid.getText().toString());*/
					//editor.commit();
					
					//isFinish = true;
					
					//finish();
							      
					/*Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);*/
				/*	Intent intent = new Intent(Authentication.this,
							Confirmation.class);
					
					intent.putExtra("mobilenumber",txtmobilenumber.getText().toString());
					intent.putExtra("userid",txtuserid.getText().toString());*/
					
					//bundle.putString("username");
					//intent.putExtra("com.cnergee.service.home.screen.INTENT", bundle);
					//startActivity(intent);
					//Authentication.this.finish();
					//Utils.log("GetMemberDetailWebService","called");
					getMemberDetailWebService= new GetMemberDetailWebService();
					getMemberDetailWebService.execute();
				} else {
					//Utils.log("GetMemberDetailWebService"," not called");
					//mainDiaUtils.dismiss();
					Toast.makeText(Authentication.this,getString(R.string.login_invalid),
							Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				mProgressHUD.dismiss();
				//mainDiaUtils.dismiss();
				AlertsBoxFactory.showAlert(rslt,context );
			}
			//isFinish=true;
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				
				AuthenticationCaller authenticationcaller = new AuthenticationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_LOGIN_BY_MEMBERID)
										);
				authenticationcaller.mobilenumber = txtmobilenumber.getText().toString().trim();
				authenticationcaller.userid = txtuserid.getText().toString().trim();
				
				authenticationcaller.join();
				authenticationcaller.start();
			//	Utils.log("valid","user");
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert(e.toString(),context );*/
			}
			return null;
		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			//DiaUtils.dismiss();
			btnsubmit.setClickable(true);
			validUserWebService = null;
		}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}

	//***********starts here********************************GetMemeberDetails starts here********************************	
	 private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{
			private ProgressHUD mProgressHUD;

			protected void onPreExecute() {
				//Dialog.setMessage(getString(R.string.app_please_wait_label));
				mProgressHUD = ProgressHUD.show(Authentication.this,getString(R.string.app_please_wait_label), true,true,this);
			}

			protected void onPostExecute(Void unused) {
				getMemberDetailWebService = null;
				mProgressHUD.dismiss();
				//DiaUtils.dismiss();
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
						
						
						MemberLoginId=ConfirmationDetails.getMemberLoginId();
						MemberId=ConfirmationDetails.getMemberId();
						//Utils.log("MemeberLogin Id  User","is:"+MemberLoginId);
						//Utils.log("Memeber Id  User Authentication","is:"+MemberId);
						/* For Insertuing Phone Details */
						InsertPhoneDetailsWebService= new InsertPhoneDetailsWebService();
						InsertPhoneDetailsWebService.execute();
						
						
					}
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
				}else{
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert(rslt,context );
				}
				}catch(Exception e){
					mProgressHUD.dismiss();AlertsBoxFactory.showAlert(rslt,context );}	
			}
			@Override
			protected Void doInBackground(String... params) {
				try {
					ConfirmationCallerAuthentication ConfirmationCallerAuthentication = new ConfirmationCallerAuthentication(
							getApplicationContext().getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),
							getApplicationContext().getResources().getString(
									R.string.SOAP_URL), getApplicationContext()
									.getResources().getString(
											R.string.METHOD_GET_MEMBER_DETAILS)
											);

					ConfirmationCallerAuthentication.MemberLoginId = txtuserid.getText().toString();
					ConfirmationCallerAuthentication.Mobilenumber = txtmobilenumber.getText().toString();
					ConfirmationCallerAuthentication.setAllData(true);
					
					ConfirmationCallerAuthentication.join();
					ConfirmationCallerAuthentication.start();
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
				 mProgressHUD.dismiss();
					getMemberDetailWebService = null;
				}

			/* (non-Javadoc)
			 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
			}
			}	 
//***********ends here********************************GetMemeberDetails ends here********************************

//*******starts here*********************Insert PhoneDetails object web service called here************************ 
	 private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

		 ProgressHUD	 mProgressHUD ;
			PhoneDetailsOBJ phonedetailsobj = new PhoneDetailsOBJ();
			String versionName="";
			protected void onPreExecute() {
				//Dialog.setMessage(getString(R.string.app_please_wait_label));
				 mProgressHUD = ProgressHUD.show(Authentication.this,getString(R.string.app_please_wait_label), true,true,this);
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
				mProgressHUD.dismiss();
				InsertPhoneDetailsWebService = null;
				//submit.setClickable(true);
			}

			
			protected void onPostExecute(Void unused) {

				mProgressHUD.dismiss();
				//submit.setClickable(true);
				InsertPhoneDetailsWebService = null;

				if (rslt.trim().equalsIgnoreCase("ok")) {
					
					Authentication.this.finish();
					Intent intent = new Intent(Authentication.this, SMSAuthenticationActivity.class);
					intent.putExtra("mobilenumber",txtmobilenumber.getText().toString());
					intent.putExtra("MemberId",MemberId);
					Utils.log("Member ID Auth",":"+MemberId);
					intent.putExtra("MemberLoginId",MemberLoginId);
					
					startActivity(intent);
					//Utils.log("Insert Phone","postExecute");
					
				} else {
					//Utils.log("Insert Phone","postExecute not called");
					mProgressHUD.dismiss();
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
					
					
					phonedetailsobj.setMemberId(MemberId);
					phonedetailsobj.setPhoneName(android.os.Build.MODEL.toString());
					phonedetailsobj.setPhoneNumber(txtmobilenumber.getText().toString());
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
					
					caller.setAllData(false);
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

			/* (non-Javadoc)
			 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
			}
		}
	//*********ends here*******************Insert PhoneDetails object web service called here************************		
	
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	/*this.finish();
	if(!isFinish||!validUserWebService.isCancelled())
	validUserWebService.cancel(true);
*/}

/* (non-Javadoc)
 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
 */
@Override
public void onCancel(DialogInterface dialog) {
	// TODO Auto-generated method stub
	
}
}
