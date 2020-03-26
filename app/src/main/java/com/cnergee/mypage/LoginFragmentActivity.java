package com.cnergee.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabWidget;


import com.cnergee.fragments.ExistingConnFragment;
import com.cnergee.fragments.NewConnFragment;
import com.cnergee.mypage.adapter.ViewPagerAdapter;
import com.cnergee.mypage.utils.Utils;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.ViewPager;
import cnergee.krishna.ookla.odigitel.R;

public class LoginFragmentActivity extends FragmentActivity {
	
	ViewPager mPager;
	ActionBar.Tab tab;
	public static String check="";
	 private FragmentTabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_activity);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//getSupportActionBar().hide();
		//mActionBar=getSupportActionBar();
		//getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mPager = (ViewPager) findViewById(R.id.pager);
		
		Intent i= getIntent();
	
		check=i.getStringExtra("from");
		
		FragmentManager fm = getSupportFragmentManager();

		// Capture ViewPager page swipes
		ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				// Find the ViewPager Position
				Utils.log("selected"," position"+position);
				
				mTabHost.setCurrentTab(position);
				
			}
			
		};

		mPager.setOnPageChangeListener(ViewPagerListener);
		// Locate the adapter class called ViewPagerAdapter.java
		ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm,LoginFragmentActivity.this);
		// Set the View Pager Adapter into ViewPager
		mPager.setAdapter(viewpageradapter);
		
		 mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
	        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
	       // if(Build.VERSION.SDK_INT>11){
	      /*  mTabHost.addTab(mTabHost.newTabSpec("appl").setIndicator("",
					getResources().getDrawable(R.drawable.active)),
					MyAppliancesFragment.class, null);*/
	        
	       /* mTabHost.addTab(mTabHost.newTabSpec("appl").setIndicator(forImage(R.drawable.ic_action_undo)),
					ExistingConnFragment.class, null);
	        
	        mTabHost.addTab(mTabHost.newTabSpec("stat").setIndicator(forImage(R.drawable.ic_action_undo)),
					NewConnFragment.class, null);*/
	        
	        mTabHost.addTab(mTabHost.newTabSpec("existing").setIndicator(forImage(R.drawable.ext_cust)),
					ExistingConnFragment.class, null);
	        
	        mTabHost.addTab(mTabHost.newTabSpec("new").setIndicator(forImage(R.drawable.new_cust)),
					NewConnFragment.class, null);
	        
	        

	        final TabWidget  widget= mTabHost.getTabWidget();
	        if(Build.VERSION.SDK_INT>11){
	        for(int j = 0; j < widget.getChildCount(); j++) {
	            View v = widget.getChildAt(j);

	         
	            v.setBackgroundResource(R.drawable.tabstrip_theme_tab_indicator_holo);
	            
	        }
	        }
	        else{
	        	 DisplayMetrics displayMetrics = new DisplayMetrics();
	       	  	WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
	       	  	wm.getDefaultDisplay().getMetrics(displayMetrics);
	       	  int screenWidth = displayMetrics.widthPixels;
	       	  Utils.log("ScreenWidth","is:"+screenWidth);
	       	  int llTabWidth= (screenWidth/2);
		        for(int k = 0; k < widget.getChildCount(); k++) {
		        	
		            View v = widget.getChildAt(k);
		          
		           v.setBackgroundResource(R.drawable.tabstrip_theme_tab_indicator_holo);
		        }
		        }
	}
	public View forImage(int resource){
		 View view =LayoutInflater.from(this).inflate(R.layout.tab_image, null);
		    ImageView iv = (ImageView) view.findViewById(R.id.TabImageView);
		    iv.setImageResource(resource);
		   
		    return view;
	}
}
