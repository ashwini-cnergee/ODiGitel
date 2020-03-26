package com.cnergee.mypage;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citruspay.citruspaylib.service.CitrusGetWebClientJSResponse;
import com.cnergee.mypage.utils.Utils;

import cnergee.krishna.ookla.odigitel.R;

public class TransResponse extends Activity{
	
	TextView mTranscationIdOrderId, mTextMessage, mTxRefNo, mPgTxnNo,
	mTxStatus,txrefno;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private String sharedPreferences_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transresponse);
		initWidget();
		
		linnhome =(LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile =(LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp =(LinearLayout)findViewById(R.id.inn_banner_help);
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponse.this.finish();
				Intent i = new Intent(TransResponse.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponse.this.finish();
				Intent i = new Intent(TransResponse.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponse.this.finish();
				Intent i = new Intent(TransResponse.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponse.this.finish();
				Intent i = new Intent(TransResponse.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub

		mTranscationIdOrderId = (TextView) findViewById(R.id.txid_orderid);
		mTextMessage = (TextView) findViewById(R.id.txmsg);
		mTxRefNo = (TextView) findViewById(R.id.pgtxnno);
		mTxStatus = (TextView) findViewById(R.id.txstatus);
		txrefno = (TextView)findViewById(R.id.txrefno);
		setJsonValue();
	}

	private void setJsonValue()
	{
		String jsonResponse=getIntent().getStringExtra("jsvalue");
			CitrusGetWebClientJSResponse citrusGetWebClientJSResponse=new CitrusGetWebClientJSResponse(jsonResponse);//optional
			
			//Log.i("TxId", citrusGetWebClientJSResponse.getTxMsg());
			/*Log.i("TxMsg", citrusGetWebClientJSResponse.getTxMsg());
			Log.i("TxRefNo", citrusGetWebClientJSResponse.getTxRefNo());
			Log.i("TxStatus", citrusGetWebClientJSResponse.getTxStatus());
			Log.i("addressCity", citrusGetWebClientJSResponse.getAddressCity());
			Log.i("amount:", citrusGetWebClientJSResponse.getAmount());
			Log.i("authIdCode", citrusGetWebClientJSResponse.getAuthIdCode());
			Log.i("issuerRefNo", citrusGetWebClientJSResponse.getIssuerRefNo());
			Log.i("pgTxnNo", citrusGetWebClientJSResponse.getPgTxnNo());
			Log.i(">>>>Get Transa Status<<<<",citrusGetWebClientJSResponse.getTxStatus());*/
			mTranscationIdOrderId.setText("Transaction Number: "+citrusGetWebClientJSResponse.getTranssactionId()+" |  Order Amount: "+ citrusGetWebClientJSResponse.getAmount()+" "+citrusGetWebClientJSResponse.getCurrency());
			mTextMessage.setText(citrusGetWebClientJSResponse.getTxMsg());
			txrefno.setText(citrusGetWebClientJSResponse.getTxId());
			mTxRefNo.setText(citrusGetWebClientJSResponse.getTxRefNo());
			mTxStatus.setText(citrusGetWebClientJSResponse.getTxStatus());
			
			
			if(citrusGetWebClientJSResponse.getTxStatus().equalsIgnoreCase("SUCCESS")){
			
			sharedPreferences_name = getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = getApplicationContext()
						.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
				
				Utils.log("second time", ":"+sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true));
				if(sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true)){
					if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").length()>0){
						
						Utils.log("App rate status", ""+sharedPreferences.getString(Utils.APP_RATE_STATUS, ""));
						
						if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("later")){
							if(Utils.open_once){
								Utils.showAppRater(TransResponse.this);
								Utils.open_once=false;
							}
						}
						if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("no")){
							
						}
						if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("rate")){
							
						}
						
					}
					else{
						Utils.log("App rate status", ""+sharedPreferences.getString(Utils.APP_RATE_STATUS, ""));
						if(Utils.open_once){
							Utils.showAppRater(TransResponse.this);
							Utils.open_once=false;
						}
					}
					
				}
				else{
					
				}
			}
			
	//	citrusGetWebClientJSResponse.ge
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	this.finish();
	Intent i = new Intent(TransResponse.this,IONHome.class);
	startActivity(i);
	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
	
	
}
