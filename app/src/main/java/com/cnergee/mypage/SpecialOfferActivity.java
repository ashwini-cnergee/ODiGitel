package com.cnergee.mypage;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cnergee.mypage.caller.OffersDetailsCaller;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;

public class SpecialOfferActivity extends BaseActivity {
	Utils utils;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	ListView lvSpecialOffer;
	public static ArrayList<String> alOffersLink;
	public static ArrayList<String> alOffersName;
	public static String rslt="";
	String username="",password="";
	ArrayAdapter<String> aaOffers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_specialoffer);
		 utils= new Utils();
		 
		 alOffersLink= new ArrayList<String>();
		 alOffersName= new ArrayList<String>();
		
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			
			utils.setSharedPreferences(sharedPreferences);
		// alOffersName.add("PDF Download");
		 //"http://docs.google.com/gview?embedded=true&url="+
		// alOffersLink.add("http://i-on.in/UserDownloads/ION.apk");
		 lvSpecialOffer=(ListView) findViewById(R.id.lvSpecialOffers);
		 
			ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
			ivMenuDrawer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
				}
			});
		 
		// aaOffers= new ArrayAdapter<String>(SpecialOfferActivity.this, android.R.layout.simple_list_item_1, alOffersName);
		 new GetOfferDetailsWebService().execute();
		linnhome  = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferActivity.this.finish();
				//Intent i = new Intent(SpecialOfferActivity.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferActivity.this.finish();
				Intent i = new Intent(SpecialOfferActivity.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferActivity.this.finish();
				Intent i = new Intent(SpecialOfferActivity.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialOfferActivity.this.finish();
				Intent i = new Intent(SpecialOfferActivity.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		 //lvSpecialOffer.setAdapter(aaOffers);
		lvSpecialOffer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(alOffersLink.get(arg2).equalsIgnoreCase("no offers")){
					
				}
				else{					
					AlertsBoxFactory.showAlert(alOffersLink.get(arg2), SpecialOfferActivity.this);
					
				}
			}
		});
	}
	
	
	private class GetOfferDetailsWebService extends
	AsyncTask<String, Void, Void>  implements OnCancelListener{
		//private ProgressDialog Dialog = new ProgressDialog(SpecialOfferActivity.this);
		ProgressHUD	mProgressHUD ;
		
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SpecialOfferActivity.this,getString(R.string.app_please_wait_label), true,true,this);
		}
		
		protected void onPostExecute(Void unused) {
			
			mProgressHUD.dismiss();
			
			try{
				if (rslt.trim().equalsIgnoreCase("ok")) {	
					 aaOffers= new ArrayAdapter<String>(SpecialOfferActivity.this, android.R.layout.simple_list_item_1, alOffersName);
					lvSpecialOffer.setAdapter(aaOffers);
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					 alOffersLink= new ArrayList<String>();
					 alOffersName= new ArrayList<String>();
					alOffersName.add("No Special Offers!!!");
					alOffersLink.add("no offers");
					 aaOffers= new ArrayAdapter<String>(SpecialOfferActivity.this, android.R.layout.simple_list_item_1, alOffersName);
					lvSpecialOffer.setAdapter(aaOffers);
				}
			}catch(Exception e){
				//Utils.log("Error ","onPostExecute: "+e);
				 alOffersLink= new ArrayList<String>();
				 alOffersName= new ArrayList<String>();
				alOffersName.add("No Special Offers!!!");
				alOffersLink.add("no offers");
				 aaOffers= new ArrayAdapter<String>(SpecialOfferActivity.this, android.R.layout.simple_list_item_1, alOffersName);
				lvSpecialOffer.setAdapter(aaOffers);
				}	
				
		}
		
		@Override
		protected Void doInBackground(String... arg0) {
			try {
				
				OffersDetailsCaller offersDetailsCaller= new OffersDetailsCaller(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_OFFER_DETAILS));
				
				offersDetailsCaller.setMemberId(utils.getMemberId());
				offersDetailsCaller.join();
				offersDetailsCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;	
		}
		
		 @Override
			protected void onCancelled() {
			 mProgressHUD.dismiss();
				/*collectBtn.setClickable(true);
				getTodaysCollectionWebService = null;*/
			}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			 mProgressHUD.dismiss();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		//Intent i = new Intent(SpecialOfferActivity.this,IONHome.class);
	//	startActivity(i);
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
	
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences( getString(R.string.shared_preferences_name), 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);
	}
}
