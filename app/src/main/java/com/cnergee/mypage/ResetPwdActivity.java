package com.cnergee.mypage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cnergee.mypage.caller.ResetPwdCaller;
import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;

public class ResetPwdActivity extends BaseActivity {
	private String sharedPreferences_name;
	EditText etOldPwd,etNewPwd,etConfirmPwd;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	Button btnChangePwd;
	public static String rslt,statusResponse;
	ProgressDialog mainDialog;
	Utils utils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restepwd);
		linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		btnChangePwd = (Button)findViewById(R.id.btnChangePwd);
		
		etOldPwd= (EditText)findViewById(R.id.etOldPwd);
		etNewPwd= (EditText)findViewById(R.id.etNewPwd);
		etConfirmPwd= (EditText)findViewById(R.id.etConfirmPwd);
		
		utils=new Utils();
		mainDialog=new ProgressDialog(ResetPwdActivity.this);
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ResetPwdActivity.this.finish();
				Intent i = new Intent(ResetPwdActivity.this,IONHome.class);
				startActivity(i);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ResetPwdActivity.this.finish();
				Intent i = new Intent(ResetPwdActivity.this,Profile.class);
				startActivity(i);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ResetPwdActivity.this.finish();
				Intent i = new Intent(ResetPwdActivity.this,NotificationListActivity.class);
				startActivity(i);
			}
		});
		
		
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ResetPwdActivity.this.finish();
				Intent i = new Intent(ResetPwdActivity.this,HelpActivity.class);
				startActivity(i);
			}
		});
		
		btnChangePwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etOldPwd.getText().length()>0){
					if(etOldPwd.getText().length()>5){
						
					if(etNewPwd.getText().length()>0){
						if(etNewPwd.getText().length()>5){
						if(etConfirmPwd.getText().length()>0){
							if(etConfirmPwd.getText().toString().equalsIgnoreCase(etNewPwd.getText().toString())){
								new ChangePwdWebService().execute();
							}
							else{
								etConfirmPwd.requestFocus();
								etConfirmPwd.setError(Html.fromHtml("<font color='red'>Password does not match</font>"));
							}
						}
						else{
							etConfirmPwd.requestFocus();
							etConfirmPwd.setError(Html.fromHtml("<font color='red'>Please Enter Confirm Password</font>"));
						}
						}
						else{
							etNewPwd.requestFocus();
							etNewPwd.setError(Html.fromHtml("<font color='red'>Password should be more than or equal to 6 characters </font>"));
						}
						
					}
					else{
						etNewPwd.requestFocus();
						etNewPwd.setError(Html.fromHtml("<font color='red'>Please Enter New Password</font>"));
					}
				
				
					}
					else{
						etOldPwd.requestFocus();
						etOldPwd.setError(Html.fromHtml("<font color='red'>Password should be more than or equal to 6 characters </font>"));
					}
				
				}
				else{
					etOldPwd.requestFocus();
					etOldPwd.setError(Html.fromHtml("<font color='red'>Please Enter Old Password</font>"));
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
	
	private class ChangePwdWebService extends AsyncTask<String, Void, Void> {
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mainDialog.setCancelable(false);
			mainDialog.setMessage(getString(R.string.app_please_wait_label));
			mainDialog.show();
			
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				ResetPwdCaller caller = new ResetPwdCaller(getApplicationContext()
					.getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_CHANGE_PASSWORD));
			
			caller.setMemberId(utils.getMemberId());
			caller.setMemberLoginId(utils.getMemberLoginID());
			caller.setPasswords(etOldPwd.getText().toString().trim(), etNewPwd.getText().toString().trim());
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
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), ResetPwdActivity.this);*/
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//Utils.log("Post Execute called","yes");
			mainDialog.dismiss();
			//Utils.log("Response for status",""+statusResponse);
		}
	}
}
