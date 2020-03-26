package com.cnergee.mypage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cnergee.krishna.ookla.odigitel.R;

public class SpecialOfferWebViewActivity extends Activity {
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private String sharedPreferences_name;
	private boolean flag=true;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);  
        Intent i= getIntent();
        WebView wv;  
        wv = (WebView) findViewById(R.id.webView1);  
        wv.loadUrl(i.getStringExtra("load_url"));   // now it will not fail here
        
       
        linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
        linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
        linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);
        linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
        
        linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferWebViewActivity.this.finish();
				Intent i = new Intent(SpecialOfferWebViewActivity.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferWebViewActivity.this.finish();
				Intent i = new Intent(SpecialOfferWebViewActivity.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferWebViewActivity.this.finish();
				Intent i = new Intent(SpecialOfferWebViewActivity.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferWebViewActivity.this.finish();
				Intent i = new Intent(SpecialOfferWebViewActivity.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

			}
		});
    }  
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	if(flag){
		flag=false;
		Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_LONG).show();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

	}
	else{
		this.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

	}
}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	//this.finish();
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
sharedPreferences_name = getString(R.string.shared_preferences_name);
	SharedPreferences sharedPreferences = getApplicationContext()
			.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
	if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
		ivLogo.setVisibility(View.VISIBLE);
	else
		ivLogo.setVisibility(View.INVISIBLE);
}
}
