package com.cnergee.mypage;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.utils.Utils;

import cnergee.krishna.ookla.odigitel.R;


public class TransResponseAtom extends Activity{
	
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
				TransResponseAtom.this.finish();
				Intent i = new Intent(TransResponseAtom.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseAtom.this.finish();
				Intent i = new Intent(TransResponseAtom.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseAtom.this.finish();
				Intent i = new Intent(TransResponseAtom.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseAtom.this.finish();
				Intent i = new Intent(TransResponseAtom.this,HelpActivity.class);
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
		//setCCAvenueResponse();
		atomresponse();
	}

	private void atomresponse()
	{
		Intent i= getIntent();
	
		Utils.log("order_id",":"+i.getSerializableExtra("order_id"));
		mTxStatus.setText(i.getStringExtra("transStatus"));
		mTranscationIdOrderId.setText("Transaction Number: "+i.getStringExtra("order_id")+" | Order Amount: "+ i.getStringExtra("Amount")+" INR");
		mTxRefNo.setText(i.getStringExtra("bank_ref_no"));
		//txrefno.setText(i.getStringExtra("AuthCode"));
		txrefno.setText(i.getStringExtra("Track_id"));
		//mTxRefNo.setText(i.getStringExtra("bank_ref_no"));
		mTextMessage.setText(i.getStringExtra("statusMsg"));
		

		Utils.log("TrnsResponseAmount",""+"Amount");
		Utils.log("TrnsResponseAmount",""+i.getStringExtra("Amount"));
		Utils.log("TrnsResponsemTextMessage",""+mTextMessage.getText().toString());
		Utils.log("TrnsResponsemTxRefNo",""+mTxRefNo.getText().toString());
		Utils.log("TrnsResponsetxrefno",""+txrefno.getText().toString());
		Utils.log("TrnsResponsemTranscationIdOrderId",""+mTranscationIdOrderId.getText().toString());
		Utils.log("TrnsResponsemTxStatus",""+mTxStatus.getText().toString());
		Utils.log("TrnsResponsegetting Amount",""+i.getStringExtra("Amount"));
		Utils.log("Response Amount",""+mTranscationIdOrderId);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		this.finish();
	//Intent i = new Intent(TransResponseAtom.this,IONHome.class);
	//startActivity(i);
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
