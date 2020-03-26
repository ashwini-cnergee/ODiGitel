package com.cnergee.mypage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings.Secure;
import android.telephony.SmsMessage;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.caller.InsertPhoneDetailsCaller1;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.PhoneDetailsOBJ;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import cnergee.krishna.ookla.odigitel.R;

public class SMSAuthenticationActivity extends Activity {
	String MemberId="",MobileNum="",MemberLoginId="";
	Button btnSubmit,btnRegenerate;
	EditText etPassword;
	public static String rslt;
	public static boolean isVaildUser = false;
	ValidUserWebService validUserWebService;
	//ProgressDialog mainDialog;
	ProgressHUD	mProgressHUD ;
	InsertPhoneDetailsWebService InsertPhoneDetailsWebService;
	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    IntentFilter filter;
    TextView tvTipsMessage;
    CountDownTimer countDown;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_sms);
	
	//mainDialog= new ProgressDialog(SMSAuthenticationActivity.this);
	//mainDialog.setMessage(getString(R.string.app_please_wait_label));
	//mainDialog.setCancelable(false);
	
	Intent i= getIntent();
	MobileNum=i.getStringExtra("mobilenumber");
	MemberId=i.getStringExtra("MemberId");
	MemberLoginId=i.getStringExtra("MemberLoginId");
	//Utils.log("MobileNum SMS",""+MobileNum);
	//Utils.log("MemberId SMS",""+MemberId);
	btnSubmit=(Button) findViewById(R.id.btnsubmit);
	btnRegenerate=(Button) findViewById(R.id.btnRegenerate);
	btnRegenerate.setVisibility(View.GONE);
	btnSubmit.setVisibility(View.GONE);
	etPassword=(EditText) findViewById(R.id.etSmsPwd);
	tvTipsMessage=(TextView) findViewById(R.id.tvTipsMessage);
	
	tvTipsMessage.setTextSize(18);
	countDown=	new CountDownTimer(10000, 1000) {

	     public void onTick(long millisUntilFinished) {
	    	 tvTipsMessage.setText("seconds remaining: " + millisUntilFinished / 1000);
	     }

	     public void onFinish() {
	    	 
	    	 btnRegenerate.setVisibility(View.VISIBLE);
 			btnSubmit.setVisibility(View.VISIBLE);
	    	 tvTipsMessage.setText(Html.fromHtml("Please Check SMS for <br/> the password !!").toString());
	     }
	  }.start();

	btnSubmit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(etPassword.getText().length()>0){
			validUserWebService= new ValidUserWebService();
			validUserWebService.execute();
			}
			else{
				Toast.makeText(getApplicationContext(), "Please enter password you have received!!", Toast.LENGTH_SHORT).show();
			}
		}
	});
	
	btnRegenerate.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InsertPhoneDetailsWebService= new InsertPhoneDetailsWebService();
			InsertPhoneDetailsWebService.execute();
		}
	});
	
	filter = new IntentFilter(SMS_RECEIVED);
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	registerReceiver(receiver_SMS, filter);
}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	this.unregisterReceiver(receiver_SMS);
}

protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

	// final AlertDialog alert =new
	// AlertDialog.Builder(Login.this).create();

	//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
	String versionName="";
	
	protected void onPreExecute() {
		mProgressHUD = ProgressHUD.show(SMSAuthenticationActivity.this,getString(R.string.app_please_wait_label), true,true,this);
		
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
				/*if(getAuthcount.equals("1"))
				{
				Intent intent = new Intent(LoginActivity.this,
						HomeActivity.class);
					Login.this.finish();
				Intent intent = new Intent(Login.this,
						Confirmation.class);
				intent.putExtra("mobilenumber",mobilenumber.getText().toString());
				//bundle.putString("password",password.getText().toString());
				//intent.putExtra("com.cnergee.service.home.screen.INTENT", bundle);
				startActivity(intent);*/
				
				SMSAuthenticationActivity.this.finish();
				Intent intent = new Intent(SMSAuthenticationActivity.this, Confirmation.class);
				intent.putExtra("otp_sms", etPassword.getText().toString());
				intent.putExtra("MemberLoginId", MemberLoginId);
				intent.putExtra("MobileNum", MobileNum);

				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				else
				{
					Toast.makeText(SMSAuthenticationActivity.this,"Wrong Password!!",
							Toast.LENGTH_LONG).show();
					
				}
			} else {
				Toast.makeText(SMSAuthenticationActivity.this,getString(R.string.login_invalid),
						Toast.LENGTH_LONG).show();
				return;
			}
		
		} 
		
	

	@Override
	protected Void doInBackground(String... params) {
		try {
			
		//	Log.i("START",">>>>>>>START<<<<<<<<");
			SMSAuthenticationCaller smsCaller = new SMSAuthenticationCaller(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_SMS_AUTHENTICATION)
									);
			smsCaller.PhoneUniqueId=Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			smsCaller.MemberId = MemberId;
			smsCaller.OneTimePwd=etPassword.getText().toString();
			
			
			smsCaller.setAllData(false);
			smsCaller.setCallData("sms");
			smsCaller.join();
			smsCaller.start();
			rslt = "START";

			while (rslt == "START") {
				try {
					Thread.sleep(10);
				} catch (Exception ex) {
				}
			}

		} catch (Exception e) {
			/*AlertsBoxFactory.showErrorAlert(e.toString(),SMSAuthenticationActivity.this );*/
		}
		return null;
	}
	@Override
	protected void onCancelled() {
		mProgressHUD.dismiss();
		
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

//*******starts here*********************Insert PhoneDetails object web service called here************************ 
	 private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		 String versionName="";
			//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
			PhoneDetailsOBJ phonedetailsobj = new PhoneDetailsOBJ();

			protected void onPreExecute() {
				//Dialog.setMessage(getString(R.string.app_please_wait_label));
				mProgressHUD = ProgressHUD.show(SMSAuthenticationActivity.this,getString(R.string.app_please_wait_label), true,true,this);
				 try {
					 versionName = SMSAuthenticationActivity.this.getPackageManager()
							    .getPackageInfo(SMSAuthenticationActivity.this.getPackageName(), 0).versionName;
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
					
					Toast.makeText(getApplicationContext(), "Please check your SMS Inbox", Toast.LENGTH_SHORT).show();
					
				} else {
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
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
					phonedetailsobj.setPhoneNumber(MobileNum);
					phonedetailsobj.setPhoneVersion(android.os.Build.VERSION.RELEASE.toString());
					phonedetailsobj.setPhoneUniqueId(Secure.getString(getContentResolver(), Secure.ANDROID_ID));
					phonedetailsobj.setPhoneplatform("Android");
					phonedetailsobj.setPhonepackage("");
					phonedetailsobj.setAppVersion(versionName);
					
					InsertPhoneDetailsCaller1 caller = new InsertPhoneDetailsCaller1(
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
//					AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
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
	 public void onBackPressed() {
	   // Utils.log("CDA", "onBackPressed Called");
	    Intent setIntent = new Intent(Intent.ACTION_MAIN);
	    setIntent.addCategory(Intent.CATEGORY_HOME);
	    setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(setIntent);
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	 }
	 
 /* ************************Broadcast receiver when sms comes******************STARTS HERE***************************** */	
	  BroadcastReceiver receiver_SMS = new BroadcastReceiver()
	    {
		  
	        public void onReceive(Context context, Intent intent)
	        {
	        	try{
	        
	        	Utils.log("Message","Received");
	             if (intent.getAction().equals(SMS_RECEIVED))
	             {
	                    Bundle bundle = intent.getExtras();
	                    if (bundle != null)
	                    {
	                      Object[] pdus = (Object[]) bundle.get("pdus");
	                      SmsMessage[] messages = new SmsMessage[pdus.length];
	 
	                      for (int i = 0; i < pdus.length; i++)
	                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
	 
	                      for (SmsMessage message : messages)
	                      {
	                    	  Utils.log("Message","Received Number :"+message.getDisplayOriginatingAddress());
	                            if(message.getDisplayOriginatingAddress().contains("FSTNET")&& message.getMessageBody().contains("Pintwire Infomatics")&& message.getMessageBody().contains("OTP")){
							  //etOtp.setText(message.getDisplayMessageBody());
							  String otp_number=message.getDisplayMessageBody();
							  String message_split[]=otp_number.split(" ");
							  if(message_split.length>0){
								  for(int i=0 ;i<message_split.length;i++){
									  Utils.log(""+i,""+message_split[i]);
								  }
								  if(message_split.length>11){
									  countDown.cancel();
									  etPassword.setText(message_split[10]);
									  if(etPassword.getText().length()>0){
										  validUserWebService= new ValidUserWebService();
										  validUserWebService.execute();
									  }
									  // tvTipsMessage.setText(Html.fromHtml("Please Check SMS for<br/>the password !!").toString());
								  }
								  else{
									  btnRegenerate.setVisibility(View.VISIBLE);
									  btnSubmit.setVisibility(View.VISIBLE);
									  tvTipsMessage.setText("Please Click Submit!!");
								  }
							   }
						     }
	                       }
	                     }
	                  }
	        	}
	        	catch(Exception e){
	        		
	        	}
	        }
		  
		  
	    };
/* ************************Broadcast receiver when sms comes******************ENDS HERE***************************** */	    
}
