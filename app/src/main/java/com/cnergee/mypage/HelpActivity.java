package com.cnergee.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cnergee.mypage.adapter.HelpAdapter;
import com.cnergee.mypage.obj.HelpObject;
import com.cnergee.mypage.utils.Utils;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;

public class HelpActivity extends BaseActivity{
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private String sharedPreferences_name;
	private boolean flag=true;
	ListView lvHelp;
	ArrayList<HelpObject> alHelp= new ArrayList<HelpObject>();
	HelpAdapter helpAdapter;
	 SharedPreferences	sharedPreferences ;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.activity_help);  
       // WebView wv;  
      //  wv = (WebView) findViewById(R.id.webView1);  
       // wv.loadUrl("file:///android_asset/help.htm");   // now it will not fail here
        sharedPreferences= this.getSharedPreferences(
				this.getString(R.string.shared_preferences_renewal), 0);
        linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		lvHelp = (ListView)findViewById(R.id.lvHelp);
		helpAdapter= new HelpAdapter(HelpActivity.this, R.layout.help_item, alHelp);
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HelpActivity.this.finish();
				Intent i = new Intent(HelpActivity.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HelpActivity.this.finish();
				Intent i = new Intent(HelpActivity.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HelpActivity.this.finish();
				Intent i = new Intent(HelpActivity.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//HelpActivity.this.finish();
				//Intent i = new Intent(HelpActivity.this,HelpActivity.class);
				//startActivity(i);
			
			}
		});
		lvHelp.setAdapter(helpAdapter);
		AddHelpObject();
		lvHelp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HelpActivity.this.finish();
				HelpObject helpObject=(HelpObject)arg0.getItemAtPosition(arg2);
				String file_name=helpObject.getFile_name();
				String heading=helpObject.getText_name();
				Intent i= new Intent(HelpActivity.this, Help.class);
				i.putExtra("file_name", file_name);
				i.putExtra("heading", heading);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				
			}
		});
    }  
@Override
public void onBackPressed() {
	
	if(flag){
		flag=false;
		Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_LONG).show();
	}
	else{
		this.finish();
	}
}

public void AddHelpObject(){
	alHelp.add(new HelpObject("Renew ODiGitel Broadband Account",R.drawable.help_pay,R.color.help_renew,"Renew.htm"));
	alHelp.add(new HelpObject("Upgrade Existing Broadband Account",R.drawable.help_upgrade,R.color.help_upgrade,"Upgrade.htm"));	
	alHelp.add(new HelpObject("Request Payment Pickup",R.drawable.help_pp,R.color.help_pay_pickup,"pickup.htm"));
	alHelp.add(new HelpObject("Register a Complaint",R.drawable.help_ser_req,R.color.help_service_req,"complaint.htm"));
	if(sharedPreferences1.getBoolean("is_24ol", true)){
    	
    }
    else{
    	alHelp.add(new HelpObject("Self Resolution",R.drawable.help_self,R.color.help_self,"self_resolution.htm"));
    }
	
	alHelp.add(new HelpObject("Renewal History",R.drawable.help_ren_his,R.color.help_renew_his,"renewalhistory.htm"));
	
	if(sharedPreferences1.getBoolean("is_24ol", true)){
    	
    }
    else{
    //	alHelp.add(new HelpObject("Create Your Own Plan",R.drawable.help_calu,R.color.help_calc,"create_your_plan.htm"));
    }
	
	if(sharedPreferences1.getBoolean("is_24ol", true)){
    	
    }
    else{
    //	alHelp.add(new HelpObject("Top-Up For Your Existing Plan",R.drawable.help_top,R.color.help_topup,"topup.htm"));
    }
		
	alHelp.add(new HelpObject("Check Your Data Usage",R.drawable.help_data,R.color.help_data,"data_usage.htm"));
	alHelp.add(new HelpObject("Get Notifications",R.drawable.alerts_help,R.color.help_alerts,"notifications.htm"));
	//alHelp.add(new HelpObject("Chat with Customer Care",R.drawable.chat_2,R.color.help_chat,"chat.htm"));
	alHelp.add(new HelpObject("FAQS",R.drawable.help_faq,R.color.help_faq,"FAQ.htm"));
		
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
