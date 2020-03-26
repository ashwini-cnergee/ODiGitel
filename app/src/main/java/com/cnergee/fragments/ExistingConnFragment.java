package com.cnergee.fragments;



import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.Authentication;
import com.cnergee.mypage.IONHome;

import com.cnergee.mypage.LoginFragmentActivity;
import com.cnergee.mypage.SMSAuthenticationActivity;
import com.cnergee.mypage.caller.ConfirmationCaller;
import com.cnergee.mypage.caller.InsertPhoneDetailsCaller;
import com.cnergee.mypage.caller.LoginCaller;
import com.cnergee.mypage.obj.ConfirmationObj;
import com.cnergee.mypage.obj.LoginObj;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import androidx.fragment.app.Fragment;
import cnergee.krishna.ookla.odigitel.R;

public class ExistingConnFragment extends Fragment {
	
	Utils utils = new Utils();
	public static String rslt = "";
	Button btnsubmit,btncancel = null;
	EditText mobilenumber;
	public static boolean isVaildUser = false;
	public static String getAuthcount;
	public static String MobileNumber;
	public static String userId="";
	public static String Count;
	public static Context context;
	private ValidUserWebService validUserWebService = null;
	//FontTypefaceHelper fontTypeface = new FontTypefaceHelper();
	private String sharedPreferences_name;
	private boolean isFinish = false;
	public static String responseMsg ="";
	LoginObj Login = new LoginObj();
	
	private InsertPhoneDetailsWebService InsertPhoneDetailsWebService = null;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	boolean isLogout = false;
	public static String MemberLoginId="";
	public static String MemberId="";
	public static Map<String, ConfirmationObj> mapConfirmationDetails;
	//ProgressDialog mainDialog;
	TextView tvReg;
	ProgressHUD	mProgressHUD ;
	LoginFragmentActivity mActivity;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity =(LoginFragmentActivity) activity;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view= inflater.inflate(R.layout.fragment_existing, container, false);
		
		context = getActivity();
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
	//	mainDialog= new ProgressDialog(getActivity());
	//	mainDialog.setMessage(getString(R.string.app_please_wait_label));
		//Utils.log("check","Login"+utils.getMemberLoginID());
	
		if(LoginFragmentActivity.check.equals("2")){
			
		}
		else{
		if (utils.getMemberLoginID().equals("")	||
				 utils.getMobileNoPrimary().equals("")) {
				//finish();
		}
		
		else
		{
			getActivity().finish();
			//System.out.println(utils.getMemberId());
			Intent intent = new Intent(getActivity(), IONHome.class);
			//startActivityForResult(intent, 0);
			startActivity(intent);
		}
		}
		
		tvReg=(TextView)view.findViewById(R.id.TextView02);
		//tvReg.setText(Html.fromHtml("text before break<br/>text after break").toString());
	
		mobilenumber = (EditText)view.findViewById(R.id.txtmobilenumber);
		btnsubmit = (Button)view.findViewById(R.id.btnsubmit);
		btncancel = (Button)view.findViewById(R.id.btncancel);
		btnsubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (TextUtils.isEmpty(mobilenumber.getText().toString().trim())) {
					AlertsBoxFactory.showAlert(getString(R.string.app_please_enter_valid_label),context );
					return;
				} else {
					
					if(mobilenumber.getText().toString().startsWith("0"))
					{	
						AlertsBoxFactory.showAlert("Please enter valid mobile no.",context );
						return;
					}
					else
					{
						if(mobilenumber.getText().toString().length() > 10 || mobilenumber.getText().toString().length() < 10)
						{
							AlertsBoxFactory.showAlert("Please enter 10 digit mobile no.",context );
							return;
							
						}
						else
						{
							if(utils.isOnline(getActivity())){
							validUserWebService = new ValidUserWebService();
							validUserWebService.execute((String) null);
							}
							else{
								Toast.makeText(getActivity(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
							}
						}
					}
				}
				
				
			}
		});
		
		btncancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		         return;
			}
		});
		return view;
	}
	
	protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

		// final AlertDialog alert =new
		// AlertDialog.Builder(getActivity()).create();

		//private ProgressDialog Dialog = new ProgressDialog(getActivity());
		
		protected void onPreExecute() {
			
			/*Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);*/
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true,this);
		
		}

		protected void onPostExecute(Void unused) {
			mProgressHUD.dismiss();
			//btnsubmit.setClickable(true);
			validUserWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				//isVaildUser = true;
				if (isVaildUser) {
					//isFinish = true;
					//System.out.println("Count :" + getAuthcount);
					//finish();
					if(getAuthcount.equals("1"))
					{
					/*Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);*/
						/*getActivity().finish();
					Intent intent = new Intent(getActivity(),
							Confirmation.class);
					intent.putExtra("mobilenumber",mobilenumber.getText().toString());
					//bundle.putString("password",password.getText().toString());
					//intent.putExtra("com.cnergee.service.home.screen.INTENT", bundle);
					startActivity(intent);*/
						getMemberDetailWebService = new GetMemberDetailWebService();
						getMemberDetailWebService.execute();
					}
					else
					{
						
						getActivity().finish();
						Intent intent = new Intent(getActivity(), Authentication.class);
						intent.putExtra("mobilenumber",mobilenumber.getText().toString());
						//bundle.putString("password",password.getText().toString());
						//intent.putExtra("com.cnergee.service.home.screen.INTENT", bundle);
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						
					}
				} else {
				
					Toast.makeText(getActivity(),getString(R.string.login_invalid),
							Toast.LENGTH_LONG).show();
					return;
				}
			} else {
			
				AlertsBoxFactory.showAlert(rslt,context );
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				
			//	Log.i("START",">>>>>>>START<<<<<<<<");
				LoginCaller loginCaller = new LoginCaller(
						getActivity().getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								getActivity().getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getActivity().getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_LOGIN_BY_NUMBER)
										);
				loginCaller.mobilenumber = mobilenumber.getText().toString().trim();
				
				loginCaller.join();
				loginCaller.start();
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

	public static String getMobileNumber() {
		return MobileNumber;
	}

	public static void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	
	
	//***********starts here********************************GetMemeberDetails starts here********************************	
		 private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> {
				//private ProgressDialog Dialog = new ProgressDialog(
				//		getActivity());

				protected void onPreExecute() {
					//Dialog.setMessage(getString(R.string.app_please_wait_label));
					//Dialog.show();
				}

				protected void onPostExecute(Void unused) {
					getMemberDetailWebService = null;
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
							//Utils.log("Memeber Id  User","is:"+MemberId);
							/* For Insertuing Phone Details */
							InsertPhoneDetailsWebService= new InsertPhoneDetailsWebService();
							InsertPhoneDetailsWebService.execute();
							
							
						}
					}else if (rslt.trim().equalsIgnoreCase("not")) {
							mProgressHUD .dismiss();
						AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
					}else{
						mProgressHUD.dismiss();
						AlertsBoxFactory.showAlert(rslt,context );
					}
					}catch(Exception e){mProgressHUD.dismiss();AlertsBoxFactory.showAlert(rslt,context );}	
				}
				@Override
				protected Void doInBackground(String... params) {
					try {
						ConfirmationCaller confirmationcaller = new ConfirmationCaller(
								getActivity().getApplicationContext().getResources().getString(
										R.string.WSDL_TARGET_NAMESPACE),
										getActivity().getApplicationContext().getResources().getString(
										R.string.SOAP_URL), getActivity().getApplicationContext()
										.getResources().getString(
												R.string.METHOD_GET_MEMBER_DETAILS)
												);

						confirmationcaller.MemberLoginId = "";
						confirmationcaller.Mobilenumber = mobilenumber.getText().toString();
						confirmationcaller.setAllData(false);
						
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
					//DiaUtils.dismiss();
						getMemberDetailWebService = null;
					}
				}	 
	//***********ends here********************************GetMemeberDetails ends here********************************
		 
	//*******starts here*********************Insert PhoneDetails object web service called here************************ 
		 private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> {

				//private ProgressDialog Dialog = new ProgressDialog(getActivity());
				PhoneDetailsOBJ phonedetailsobj = new PhoneDetailsOBJ();
				String versionName="";
				protected void onPreExecute() {
					//Dialog.setMessage(getString(R.string.app_please_wait_label));
					//Dialog.show();
				}

				@Override
				protected void onCancelled() {
					//DiaUtils.dismiss();
					InsertPhoneDetailsWebService = null;
					//submit.setClickable(true);
					 try {
						 versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				
				protected void onPostExecute(Void unused) {

					mProgressHUD.dismiss();
					//submit.setClickable(true);
					InsertPhoneDetailsWebService = null;

					if (rslt.trim().equalsIgnoreCase("ok")) {
						
						getActivity().finish();
						Intent intent = new Intent(getActivity(), SMSAuthenticationActivity.class);
						intent.putExtra("mobilenumber",mobilenumber.getText().toString());
						intent.putExtra("MemberId",MemberId);
						intent.putExtra("MemberLoginId",MemberLoginId);
						startActivity(intent);
						
					} else {
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
						System.out.println(Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID));
						System.out.println("Android");
						System.out.println("");*/
						
						phonedetailsobj= new PhoneDetailsOBJ();
						phonedetailsobj.setMemberId(MemberId);
						phonedetailsobj.setPhoneName(android.os.Build.MODEL.toString());
						phonedetailsobj.setPhoneNumber(mobilenumber.getText().toString());
						phonedetailsobj.setPhoneVersion(android.os.Build.VERSION.RELEASE.toString());
						phonedetailsobj.setPhoneUniqueId(Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID));
						phonedetailsobj.setPhoneplatform("Android");
						phonedetailsobj.setPhonepackage("");
						phonedetailsobj.setAppVersion(versionName);
						
						InsertPhoneDetailsCaller caller = new InsertPhoneDetailsCaller(
								getActivity().getApplicationContext().getResources().getString(
										R.string.WSDL_TARGET_NAMESPACE),
										getActivity().getApplicationContext().getResources().getString(
										R.string.SOAP_URL), getActivity().getApplicationContext()
										.getResources().getString(
												R.string.METHOD_INSERT_PHONE_DETAILS_WITH_APPVERSION));
						caller.setAllData(true);
						
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
					/*	AlertsBoxFactory.showAlert(rslt,context );*/
					}
					return null;
				}
			}

}
