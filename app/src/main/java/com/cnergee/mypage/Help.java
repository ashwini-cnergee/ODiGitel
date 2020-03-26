package com.cnergee.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cnergee.krishna.ookla.odigitel.R;

public class Help extends BaseActivity{
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private String sharedPreferences_name;
	private boolean flag=true;
	String file_name="";
	String heading="";
	TextView titleheader;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.help); 
        Intent i= getIntent();
        file_name= i.getStringExtra("file_name");
        heading= i.getStringExtra("heading");
        WebView wv;  
        wv = (WebView) findViewById(R.id.webView1);  
        titleheader= (TextView) findViewById(R.id.titleheader);
        titleheader.setText(heading);
        if(file_name!=null){
        	wv.loadUrl("file:///android_asset/"+file_name);   // now it will not fail here
        }
        else{
        	wv.loadUrl("file:///android_asset/help.htm");
        }
        
        linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Help.this.finish();
				Intent i= new Intent(Help.this, HelpActivity.class);
				i.putExtra("file_name", file_name);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Help.this.finish();
				Intent i = new Intent(Help.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Help.this.finish();
				Intent i = new Intent(Help.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Help.this.finish();
				Intent i = new Intent(Help.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Help.this.finish();
				Intent i = new Intent(Help.this,Help.class);
				startActivity(i);
			
			}
		});
    }  
@Override
public void onBackPressed() {
	
	Help.this.finish();
	Intent i= new Intent(Help.this, HelpActivity.class);
	i.putExtra("file_name", file_name);
	startActivity(i);
	overridePendingTransition(R.anim.slide_in_right,
			R.anim.slide_out_left);
	overridePendingTransition(R.anim.slide_in_left,
			R.anim.slide_out_right);
	
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
