package com.cnergee.mypage;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cnergee.fragments.NewConnFragment;
import com.cnergee.mypage.utils.Utils;
import com.special.ResideMenu.ResideMenu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cnergee.krishna.ookla.odigitel.R;


public class Refer_FrndActivity extends BaseActivity{
	RelativeLayout rl;
	LinearLayout linnhome, linnprofile, linnnotification, linnhelp;
	ImageView ivMenuDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_frnd);
		rl=(RelativeLayout) findViewById(R.id.relativeLayout1);
		
		FragmentManager fragMan = getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragMan.beginTransaction();

		
		// add rowLayout to the root layout somewhere here

		Fragment myFrag = new NewConnFragment();
		fragTransaction.add(rl.getId(), myFrag , "fragment" + 1);
		fragTransaction.commit();
		
		linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);
		ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Refer_FrndActivity.this.finish();
				//Intent i = new Intent(Refer_FrndActivity.this, IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Refer_FrndActivity.this.finish();
				Intent i = new Intent(Refer_FrndActivity.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Refer_FrndActivity.this.finish();
				Intent i = new Intent(Refer_FrndActivity.this,
						NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Refer_FrndActivity.this.finish();
				Intent i = new Intent(Refer_FrndActivity.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
