package com.cnergee.mypage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.AdjustmentSOAPCalci;
import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.GetFinalPackageSOAP;
import com.cnergee.mypage.SOAP.GetPlanParameterSOAP;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.InternalStorage;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cnergee.plan.calc.DataLimit;
import cnergee.plan.calc.DayRate_Plan;
import cnergee.plan.calc.Days;
import cnergee.plan.calc.Speed;
import cnergee.plan.calc.Speed_After_Vol;
import cnergee.krishna.ookla.odigitel.R;


public class PlanCalculatorActivity extends Activity  {

	TextView tvDays, tvData, tvSpeed, tvFinalPrice, tvPlanName,tvPostSpeed;
	String days = "";
	SeekBar seek;
	
	Button btn_Fup,btn_Ul;
	LinearLayout btn1, btn2, btn3, btn4;
	GridView gvSpeed, gvData,gvPostSpeed;

	GridSpeedAdapter gridSpeedAdapter;
	GridSpeedPostAdapter gridSpeedPostAdapter;
	GridDataLimit gridDataLimit;
	
	LinearLayout ll_sec4_c, ll_sec4_cut, ll_sec4_renew;
	
	//MatchTextView tvHelp;
	TextView tvHelp;
	Animation animBlink;
	Days plan_days;
	public static String KEY="PLAN";
	SharedPreferences sharedPreferences;
	HashMap<String, DayRate_Plan> hashMapStored;
	int multiplier;
	Double pergb_rate=0.0;
	String DiscountFinalAmount="",DiscountPercentage="0.0";
	Double speed_rate=0.0,data_limit_rate=0.0,speed_post_rate=0.0,final_package_rate=0.0;
	
	LinearLayout ll_selectPlantType,ll_Paynow;
	public  static String selected_gv_speed_position="",selected_gv_data_position="",selected_gv_postspeed_position="";

	public boolean flag_plan_type,flag_days,flag_speed,flag_data_limit,flag_speed_post;
//	Double sel_speed_price=0.0,sel_data_limit_price=0.0,sel_post_speed=0.0;
	public String current_type="";
	
	String name_sel_speed="",name_sel_data_limit="",name_sel_post_speed="";
	ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
	ArrayList<DataLimit> alDataLimits= new ArrayList<DataLimit>(); 
	ArrayList<Speed_After_Vol> alSpeed_After_Vols=new ArrayList<Speed_After_Vol>(); 
	String MemberId;
	Utils utils = new Utils();
	TextView tvDialogPrice;
	public static Map<String, PackageDetails> mapPackageDetails;
	String checkValue="";
	String memberloginid="";
	 String updateFrom="";
	 String CalciVersion="0.0";
	public static int gv_speed_row=0,gv_data_row=0,gv_post_speed_row=0;
	public boolean is_version_checked=false;
	String PackageRate,AdditionalAmount,AdditionalAmountType, DaysFineAmount,DiscountAmount,finalcharges,FineAmount;
	LinearLayout ll_addtional_details,llClickDetails;
	boolean is_payemnt_detail=false;
	boolean isDetailShow=false;
	TextView TextView02;
	AdditionalAmount additionalAmount; 
	LinearLayout ll_update_from;
	public boolean compulsory_immediate=false;

	boolean is_guide_shown=false;
	LinearLayout ll_select_days;
	RadioButton rb_immediate,rb_next;
	
	LinearLayout ll_data_transfer_box,ll_post_speed_box,ll_speed_box;
	String datafrom="changepack";
	Dialog pg_dialog;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_plan_calculator);

		initializeControls();
	//	guideUser("Plan Type","Select Plan Type FUP or Unlimited",ll_selectPlantType,1);
		showInstructions();
		 sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		MemberId = utils.getMemberId();
		memberloginid = utils.getMemberLoginID();
		
		 animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
	                R.anim.blink_anim);
		  tvHelp.startAnimation(animBlink);
		  
		 
		  
			BaseApplication.getEventBus().register(this);
		//  new GetPlanDetailsAsyncTask().execute();
		  
		  sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
		  CalciVersion=sharedPreferences.getString("Calc_Version", "0");
		  //new GetPlanDetailsAsyncTask().execute();
		  if(sharedPreferences.getBoolean("local_plan", false)){
		  try {
			plan_days=(Days)InternalStorage.readObject(PlanCalculatorActivity.this, KEY);
			
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			 }
			    else{
			    	 new GetCalciVersion().execute();
			    }
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		if(plan_days!=null){
			hashMapStored=plan_days.getHashMapDays();
			//set30Days();
		//	set_Number_Days("30");
		}
		else{
			//new GetPlanDetailsAsyncTask().execute();
			if(Utils.isOnline(PlanCalculatorActivity.this)){
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			 }
			    else{
			    	 new GetCalciVersion().execute();
			    }
			}
			else{
				showEndDialog("Please connect to internet !!");
			}
		}
		  }
		  else{
			//  new GetPlanDetailsAsyncTask().execute();
			  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
					 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				 }
				    else{
				    	 new GetCalciVersion().execute();
				    }
		  }
		
	//	gvSpeed.setNumColumns(3);
		

		seek.incrementProgressBy(3);
		seek.setMax(11);
		seek.setProgress(1);
		Typeface tf = Typeface.createFromAsset(this.getAssets(),
				"digital_7.ttf");
		tvDays.setTypeface(tf);
		tvData.setTypeface(tf);
		tvSpeed.setTypeface(tf);
		tvPostSpeed.setTypeface(tf);
		tvFinalPrice.setTypeface(tf);
		
		
		//blinkView(tvHelp);
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Utils.log("Progrees", "is" + progress);
				if (fromUser) {
					if ((progress == 0) || (progress == 1) || (progress == 2)) {
						seek.setProgress(1);
						if(flag_plan_type){
						days = "30";
						//set30Days();
						initialsetup();
						if(current_type.equalsIgnoreCase("fup"))
						set_Number_Days("30");
						if(current_type.equalsIgnoreCase("ul"))
							set_Number_Days("ul_30");
						flag_days=true;
						showInstructions();
						}
						

					}
					if ((progress == 3) || (progress == 4) || (progress == 5)) {
						seek.setProgress(4);
						if(flag_plan_type){
							
						days = "90";
						
						//set90Days();
						initialsetup();
						if(current_type.equalsIgnoreCase("fup"))
						set_Number_Days("90");
						if(current_type.equalsIgnoreCase("ul"))
							set_Number_Days("ul_90");
						flag_days=true;
						showInstructions();
						}
					}
					if ((progress == 6) || (progress == 7) || (progress == 8)) {
						seek.setProgress(7);
						if(flag_plan_type){
							days = "180";
							//set30Days();
							initialsetup();
							if(current_type.equalsIgnoreCase("fup"))
							set_Number_Days("180");
							if(current_type.equalsIgnoreCase("ul"))
								set_Number_Days("ul_180");
							flag_days=true;
							showInstructions();
							}
					}
					if ((progress == 9) || (progress == 10) || (progress == 11)) {
						seek.setProgress(10);
						if(flag_plan_type){
						days = "365";
						initialsetup();
						if(current_type.equalsIgnoreCase("fup"))
						set_Number_Days("365");
						if(current_type.equalsIgnoreCase("ul"))
							set_Number_Days("ul_365");
						flag_days=true;
						showInstructions();
						}
					}
				}
			}
		});

		gvSpeed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*sel_speed = (String) parent.getItemAtPosition(position);
				tvSpeed.setText(sel_speed);
				getDataPlan();*/
			//	LinearLayout ll=(LinearLayout) view.findViewById(R.id.ll_gv);
				
				//ll.setBackgroundColor(R.color.kesari_color);
				Utils.log("Flage Days","is:"+flag_days);
				if(flag_days){
					flag_speed=true;
				showInstructions();
				
				

				for(int visiblePosition = gvSpeed.getFirstVisiblePosition(); visiblePosition <= gvSpeed.getLastVisiblePosition(); visiblePosition++) {
					  
					   if(visiblePosition==position){
						   View view1 = gvSpeed.getChildAt(visiblePosition);
						   if(view1!=null){
							LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
							c.setBackgroundResource(R.drawable.num_box_new_selected);
						   }
						}
						else{
							  
							 View view1 = gvSpeed.getChildAt(visiblePosition);
							 if(view1!=null){
								 LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
								 c.setBackgroundResource(R.drawable.num_box_new);
							   }
						}
					   // make something
					}
				
				/*for(int i=0;i<gridSpeedAdapter.getCount();i++){
					if(i==position){
						View v1 = parent.getChildAt(position);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new_selected);
					}
					else{
						View v1 = parent.getChildAt(i);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new);
					}
				}
				*/
				
				
				Speed speed= (Speed)parent.getItemAtPosition(position);
				name_sel_speed=speed.getSpeed_Value();
				speed_rate=speed.getSpeed_Rate()*multiplier;
				Utils.log("speed_calculation",":"+speed.getSpeed_Rate()+"*"+multiplier);
				getDataPlan();
				getPlanName();
				if(flag_speed_post){
					getFinalPrice();
				}
				if(current_type.equalsIgnoreCase("ul")){
					getFinalPrice();
				}
				
				}
			}
		});

		gvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*sel_data = (String) parent.getItemAtPosition(position);
				tvData.setText(sel_data);
				getDataPlan();*/
				if(flag_speed){
					flag_data_limit=true;
					showInstructions();
				
					for(int visiblePosition = gvData.getFirstVisiblePosition(); visiblePosition <= gvData.getLastVisiblePosition(); visiblePosition++) {
						  
						
						   if(visiblePosition==position){
							   View view1 = gvData.getChildAt(visiblePosition);
								if(view1!=null){
								LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.num_box_new_selected);
								}
							}
							else{
								 View view1 = gvData.getChildAt(visiblePosition);
								 if(view1!=null){
									 LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
									 c.setBackgroundResource(R.drawable.num_box_new);
								 }
							}
						   // make something
						}	
					
				/*for(int i=0;i<gridDataLimit.getCount();i++){
					if(i==position){
						View v1 = parent.getChildAt(position);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new_selected);
					}
					else{
						View v1 = parent.getChildAt(i);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new);
					}
				}*/
				DataLimit dataLimit=(DataLimit) parent.getItemAtPosition(position);
				data_limit_rate=Double.valueOf(dataLimit.getData_Gb_Value())*dataLimit.getData_Gb_Rate();
				name_sel_data_limit=dataLimit.getData_Gb_Value();
				Utils.log("data_limit_rate",":"+data_limit_rate);
			
				getDataPlan();
				getPlanName();
				if(flag_speed_post){
					getFinalPrice();
				}
				}
			}
		});
		
	
		gvPostSpeed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*sel_speed = (String) parent.getItemAtPosition(position);
				tvSpeed.setText(sel_speed);
				getDataPlan();*/
				if(flag_data_limit){
					flag_speed_post =true;
					showInstructions();
					
					for(int visiblePosition = gvPostSpeed.getFirstVisiblePosition(); visiblePosition <= gvPostSpeed.getLastVisiblePosition(); visiblePosition++) {
						  
						   if(visiblePosition==position){
							   View view1 = gvPostSpeed.getChildAt(visiblePosition);
								if(view1!=null){
								LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.num_box_new_selected);
								}
							}
							else{
								 View view1 = gvPostSpeed.getChildAt(visiblePosition);
								 if(view1!=null){
									 LinearLayout c=(LinearLayout)view1.findViewById(R.id.ll_gv) ;
									 c.setBackgroundResource(R.drawable.num_box_new);
								 }
							}
						   // make something
						}	
					
					
				/*for(int i=0;i<gridSpeedPostAdapter.getCount();i++){
					if(i==position){
						View v1 = parent.getChildAt(position);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new_selected);
					}
					else{
						View v1 = parent.getChildAt(i);
						LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
						c.setBackgroundResource(R.drawable.num_box_new);
					}
				}*/
				
				Speed_After_Vol speed_After_Vol= (Speed_After_Vol)parent.getItemAtPosition(position);
				speed_post_rate=speed_After_Vol.getSpeed_AV_Rate()*multiplier;
				Utils.log("speed_rate",":"+speed_post_rate);
				name_sel_post_speed=speed_After_Vol.getSpeed_AV_Value();
				getDataPlan();
				getPlanName();
				getFinalPrice();
				}
			}
		});

		 BaseApplication.getEventBus().register(this);
	}
	
	protected void onResume() {
		super.onResume();
		
	};

	OnClickListener clk = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btn1) {
				seek.setProgress(1);
				if(flag_plan_type){
				
				days = "30";
				//set30Days();
				initialsetup();
				if(current_type.equalsIgnoreCase("fup"))
				set_Number_Days("30");
				if(current_type.equalsIgnoreCase("ul"))
				set_Number_Days("ul_30");
				flag_days=true;
				showInstructions();
				}
				
				
			}
			if (v == btn2) {
				seek.setProgress(4);
				if(flag_plan_type){
				days = "90";
			
				//set90Days();
				initialsetup();
				if(current_type.equalsIgnoreCase("fup"))
					set_Number_Days("90");
				if(current_type.equalsIgnoreCase("ul"))
					set_Number_Days("ul_90");
					flag_days=true;
					showInstructions();
					initialsetup();
				}
				
			}
			if (v == btn3) {
				seek.setProgress(7);
				if(flag_plan_type){
				days = "180";
				//set180Days();
				
				showInstructions();
				if(current_type.equalsIgnoreCase("fup"))
				set_Number_Days("180");
				if(current_type.equalsIgnoreCase("ul"))
					set_Number_Days("ul_180");
				flag_days=true;
				initialsetup();
				}
			}
			if (v == btn4) {
				seek.setProgress(10);
				days = "365";
				if(flag_plan_type){
					if(current_type.equalsIgnoreCase("fup"))	
				set_Number_Days("365");
					if(current_type.equalsIgnoreCase("ul"))
						set_Number_Days("ul_365");
				initialsetup();
				flag_days=true;
				showInstructions();
				}
			}
			if(v==btn_Fup){
				if(current_type.length()>0){
				if(current_type.equalsIgnoreCase("ul")){
					
					  LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
					    lp.weight =(float) 0.5;
					    ll_speed_box.setLayoutParams(lp);
					
					ll_data_transfer_box.setVisibility(View.VISIBLE);
					ll_post_speed_box.setVisibility(View.VISIBLE);
				resetFlags();
				flag_plan_type=true;
				showInstructions();
				btn_Fup.setBackgroundResource(R.drawable.fup_selected);
				btn_Ul.setBackgroundResource(R.drawable.ul);
				initialsetup();
				current_type="fup";
				set_Number_Days("unlimited");
				/*days="30";
				set_Number_Days("30");
				seek.setProgress(1);*/
				}
				}
				else{
					  LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
					    lp.weight =(float) 0.5;
					    ll_speed_box.setLayoutParams(lp);
					ll_data_transfer_box.setVisibility(View.VISIBLE);
					ll_post_speed_box.setVisibility(View.VISIBLE);
					resetFlags();
					
					
					flag_plan_type=true;
					showInstructions();
					btn_Fup.setBackgroundResource(R.drawable.fup_selected);
					btn_Ul.setBackgroundResource(R.drawable.ul);
					initialsetup();
					current_type="fup";
					set_Number_Days("unlimited");
					/*days="30";
					set_Number_Days("30");
					seek.setProgress(1);*/
				}
			}
			if(v==btn_Ul){
				if (current_type.length() > 0) {
					if(current_type.equalsIgnoreCase("fup")){
						
						   LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
						    lp.weight = 1;
						    ll_speed_box.setLayoutParams(lp);
						
						ll_data_transfer_box.setVisibility(View.GONE);
						ll_post_speed_box.setVisibility(View.GONE);
					resetFlags();
					flag_plan_type = true;
					showInstructions();
					btn_Fup.setBackgroundResource(R.drawable.fup);
					btn_Ul.setBackgroundResource(R.drawable.ul_selected);
					set_Number_Days("unlimited");
					initialsetup();
					current_type = "ul";
					/*days="30";
					set_Number_Days("ul_30");
					seek.setProgress(1);*/
					}
				} else {
					resetFlags();
					 
					   LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
					    lp.weight = 1;
					    ll_speed_box.setLayoutParams(lp);
					ll_data_transfer_box.setVisibility(View.GONE);
					ll_post_speed_box.setVisibility(View.GONE);
					flag_plan_type = true;
					showInstructions();
					btn_Fup.setBackgroundResource(R.drawable.fup);
					btn_Ul.setBackgroundResource(R.drawable.ul_selected);
					set_Number_Days("unlimited");
					initialsetup();
					current_type = "ul";
					/*days="30";
					set_Number_Days("ul_30");
					seek.setProgress(1);*/
				}
				
				
			}

		}
	};

	OnClickListener section3_clk = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*if (v == ll_sec3_512) {
				sel_speed_after_vol = "512";
				getDataPlan();

			}
			if (v == ll_sec3_1) {
				sel_speed_after_vol = "1";
				getDataPlan();
			}
			if (v == ll_sec3_2) {
				sel_speed_after_vol = "2";
				getDataPlan();
			}
			if (v == ll_sec3_4) {
				sel_speed_after_vol = "4";
				getDataPlan();
			}*/

		}
	};

	OnClickListener section4_clk = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == ll_sec4_c) {
				flag_speed_post=false;flag_data_limit=false;flag_speed=false;
				showInstructions();
				gridSpeedPostAdapter = new GridSpeedPostAdapter(
						PlanCalculatorActivity.this,
						R.layout.gv_row_item,
						alSpeed_After_Vols,
						"speed");
				gvPostSpeed.setAdapter(gridSpeedPostAdapter);
				gridSpeedPostAdapter.notifyDataSetChanged();
				speed_post_rate=0.0;
				getFinalPrice();
				getDataPlan();
				name_sel_post_speed="";
				getPlanName();
				
				gridDataLimit = new GridDataLimit(
						PlanCalculatorActivity.this,
						R.layout.gv_data_row_item,
						alDataLimits, "");
				gvData.setAdapter(gridDataLimit);
				gridDataLimit.notifyDataSetChanged();
				data_limit_rate=0.0;
				getFinalPrice();
				getDataPlan();
				name_sel_data_limit="";
				getPlanName();
				
				gridSpeedAdapter = new GridSpeedAdapter(
						PlanCalculatorActivity.this, R.layout.gv_data_row_item,
						alSpeeds, "");
				gvSpeed.setAdapter(gridSpeedAdapter);

				gridSpeedAdapter.notifyDataSetChanged();
				speed_rate=0.0;
				getFinalPrice();
				getDataPlan();
				name_sel_speed="";
				getPlanName();
			}
			if (v == ll_sec4_cut) {
				if(current_type.equalsIgnoreCase("fup")){
					if(flag_speed_post){
						Utils.log("Post Speed","Cut");
						//tvPostSpeed.setVisibility(View.GONE);
						flag_speed_post=false;
						showInstructions();
						gridSpeedPostAdapter = new GridSpeedPostAdapter(
								PlanCalculatorActivity.this,
								R.layout.gv_row_item,
								alSpeed_After_Vols,
								"speed");
						gvPostSpeed.setAdapter(gridSpeedPostAdapter);
						gridSpeedPostAdapter.notifyDataSetChanged();
						speed_post_rate=0.0;
						getFinalPrice();
						getDataPlan();
						name_sel_post_speed="";
						getPlanName();
					}
					else{
						
						if(flag_data_limit){
							Utils.log("Data Limit","Cut");
							//tvPostSpeed.setVisibility(View.GONE);
							flag_data_limit=false;
							showInstructions();
							gridDataLimit = new GridDataLimit(
									PlanCalculatorActivity.this,
									R.layout.gv_data_row_item,
									alDataLimits, "");
							gvData.setAdapter(gridDataLimit);
							gridDataLimit.notifyDataSetChanged();
							data_limit_rate=0.0;
							getFinalPrice();
							getDataPlan();
							name_sel_data_limit="";
							getPlanName();
						}
						else{
							if(flag_speed){
								Utils.log("Speed","Cut");
								//tvPostSpeed.setVisibility(View.GONE);
								flag_speed=false;
								showInstructions();
								gridSpeedAdapter = new GridSpeedAdapter(
										PlanCalculatorActivity.this, R.layout.gv_data_row_item,
										alSpeeds, "");
								gvSpeed.setAdapter(gridSpeedAdapter);

								gridSpeedAdapter.notifyDataSetChanged();
								speed_rate=0.0;
								getFinalPrice();
								getDataPlan();
								name_sel_speed="";
								getPlanName();
							}
						}
						
					}
					
				}
				if(current_type.equalsIgnoreCase("ul")){
					if(flag_speed){
						Utils.log("Speed","Cut");
						//tvPostSpeed.setVisibility(View.GONE);
						flag_speed=false;
						showInstructions();
						gridSpeedAdapter = new GridSpeedAdapter(
								PlanCalculatorActivity.this, R.layout.gv_data_row_item,
								alSpeeds, "");
						gvSpeed.setAdapter(gridSpeedAdapter);

						gridSpeedAdapter.notifyDataSetChanged();
						speed_rate=0.0;
						getFinalPrice();
						getDataPlan();
						name_sel_speed="";
						getPlanName();
					}
				}
			}
			if (v == ll_Paynow) {
				String	sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
				SharedPreferences sharedPreferences2 = getApplicationContext()
						.getSharedPreferences(sharedPreferences_renewal, 0); 
				if(!tvPlanName.getText().toString().equalsIgnoreCase(sharedPreferences2.getString("PackageName", "-"))){
				if(is_version_checked){
				if(current_type.equalsIgnoreCase("fup")){
					if(flag_speed_post){
						getFinalPrice();
						getPlanName();
						//showFinalDialog();
						//new GetFinalPriceAsynctask().execute();
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							 new GetFinalDeductedAmtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
						 else
							 new GetFinalDeductedAmtAsyncTask().execute();
					}
				}
				if(current_type.equalsIgnoreCase("ul")){
					if(flag_speed){
						getFinalPrice();
						getPlanName();
						//showFinalDialog();
						//new GetFinalPriceAsynctask().execute();
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							 new GetFinalDeductedAmtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
						 else
							 new GetFinalDeductedAmtAsyncTask().execute();
						
					}
				}
				
				}
				else{
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
					 else
						 new GetCalciVersion().execute();
					
				}
				}
				else{
					AlertsBoxFactory.showAlert("You have selected same package as your last plan. \n Please Create either different plan or use 'Pay Now' option from Home Screen.", PlanCalculatorActivity.this);
				}
			}
			
			

		}
	};

	public void initializeControls() {
		tvDays = (TextView) findViewById(R.id.tvDays);
		tvData = (TextView) findViewById(R.id.tvData);
		tvSpeed = (TextView) findViewById(R.id.tvSpeed);
		tvFinalPrice = (TextView) findViewById(R.id.tvFinalPrice);
		tvPlanName = (TextView) findViewById(R.id.tvPlanName);
		tvPostSpeed= (TextView) findViewById(R.id.tvPostSpeed);
		seek = (SeekBar) findViewById(R.id.seekBar1);
		btn1 = (LinearLayout) findViewById(R.id.btn1);
		btn2 = (LinearLayout) findViewById(R.id.btn2);
		btn3 = (LinearLayout) findViewById(R.id.btn3);
		btn4 = (LinearLayout) findViewById(R.id.btn4);
		ll_data_transfer_box= (LinearLayout) findViewById(R.id.ll_data_transfer_box);
		ll_post_speed_box= (LinearLayout) findViewById(R.id.ll_post_speed_box);
		ll_speed_box= (LinearLayout) findViewById(R.id.ll_speed_box);
		ll_select_days= (LinearLayout) findViewById(R.id.ll_select_days);
		btn_Fup=(Button)findViewById(R.id.btnFup);
		btn_Ul=(Button)findViewById(R.id.btnUnlimited);
		ll_addtional_details= (LinearLayout) findViewById(R.id.ll_addtional_details);
		
		ll_Paynow=(LinearLayout) findViewById(R.id.ll_section4_3);
		btn1.setOnClickListener(clk);
		btn2.setOnClickListener(clk);
		btn3.setOnClickListener(clk);
		btn4.setOnClickListener(clk);
		btn_Fup.setOnClickListener(clk);
		btn_Ul.setOnClickListener(clk);
		gvSpeed = (GridView) findViewById(R.id.gvSpeed);
		gvData = (GridView) findViewById(R.id.gvData);
		gvPostSpeed=(GridView) findViewById(R.id.gvPostSpeed);
		
		ll_selectPlantType=(LinearLayout) findViewById(R.id.ll_selectplan_type);
		
		/*ll_sec3_512 = (LinearLayout) findViewById(R.id.ll_section3_512);
		ll_sec3_1 = (LinearLayout) findViewById(R.id.ll_section3_1);
		ll_sec3_2 = (LinearLayout) findViewById(R.id.ll_section3_2);
		ll_sec3_4 = (LinearLayout) findViewById(R.id.ll_section3_4);*/
		ll_sec4_c = (LinearLayout) findViewById(R.id.ll_section4_1);
		ll_sec4_cut = (LinearLayout) findViewById(R.id.ll_section4_2);
		
		//ll_sec4_renew = (LinearLayout) findViewById(R.id.ll_section4_4);
		
		tvHelp=(TextView) findViewById(R.id.tvHelp);
		
		/*ll_sec3_512.setOnClickListener(section3_clk);
		ll_sec3_1.setOnClickListener(section3_clk);
		ll_sec3_2.setOnClickListener(section3_clk);
		ll_sec3_4.setOnClickListener(section3_clk);
*/
		ll_sec4_c.setOnClickListener(section4_clk);
		ll_sec4_cut.setOnClickListener(section4_clk);
		ll_Paynow.setOnClickListener(section4_clk);
	//	ll_sec4_renew.setOnClickListener(section4_clk);

		tvDays.setVisibility(View.GONE);
		tvData.setVisibility(View.GONE);
		tvSpeed.setVisibility(View.GONE);
		tvPostSpeed.setVisibility(View.GONE);
		tvFinalPrice.setVisibility(View.GONE);
	
	}

	public void getDataPlan() {
		if(current_type.equalsIgnoreCase("fup")){
		tvDays.setVisibility(View.VISIBLE);
		tvData.setVisibility(View.VISIBLE);
		tvSpeed.setVisibility(View.VISIBLE);
		tvPostSpeed.setVisibility(View.VISIBLE);
		tvFinalPrice.setVisibility(View.GONE);
		
		tvDays.setText(""+speed_rate.intValue()+"+");
		tvData.setText(""+data_limit_rate.intValue()+"+");
		tvPostSpeed.setText(""+speed_post_rate.intValue());
		}
		if(current_type.equalsIgnoreCase("ul")){
			tvPostSpeed.setVisibility(View.VISIBLE);
			tvFinalPrice.setVisibility(View.GONE);
			tvPostSpeed.setText(""+speed_post_rate.intValue());
		}
	
	}

	public void getFinalPrice() {
		if(current_type.equalsIgnoreCase("fup")){
		tvFinalPrice.setVisibility(View.VISIBLE);
		Utils.log("speed_rate", ""+speed_rate);
		Utils.log("data_limit_rate", ""+data_limit_rate);
		Utils.log("speed_post_rate", ""+speed_post_rate);
		final_package_rate = speed_rate	 + data_limit_rate+ speed_post_rate;
		tvFinalPrice.setText("=" + final_package_rate.intValue());
		}
		else{
			if(current_type.equalsIgnoreCase("ul")){
				tvFinalPrice.setVisibility(View.VISIBLE);
				tvDays.setVisibility(View.GONE);
				tvPostSpeed.setVisibility(View.GONE);
				tvData.setVisibility(View.GONE);
				tvSpeed.setVisibility(View.GONE);
				
				Utils.log("speed_rate", ""+speed_rate);
				Utils.log("data_limit_rate", ""+data_limit_rate);
				Utils.log("speed_post_rate", ""+speed_post_rate);
				
				final_package_rate=speed_rate;
				tvFinalPrice.setText(""+final_package_rate.intValue());
			}
		}
		
	}

	public void getPlanName() {
		if(current_type.equalsIgnoreCase("fup")){
		String speed_unit = "Mb";
		String speed_unit_after_vol = "Mb";
		if (name_sel_speed.equalsIgnoreCase("512"))
			speed_unit = "KB";
		if (name_sel_post_speed.equalsIgnoreCase("512"))
			speed_unit_after_vol = "Kb";
		tvPlanName.setText("ION-" + name_sel_speed + speed_unit + " " + name_sel_data_limit
				+ "GB " + name_sel_post_speed + speed_unit_after_vol + " ("
				+ days + ")");
		}
		else{
			if(current_type.equalsIgnoreCase("ul")){
				String speed_unit = "M";			
				if (name_sel_speed.equalsIgnoreCase("512"))
					speed_unit = "";
				
				tvPlanName.setText("ION " + name_sel_speed + speed_unit + "-"
						+ days + "UL");
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub	
			//Intent i = new Intent(PlanCalculatorActivity.this, IONHome.class);
			//startActivity(i);
			PlanCalculatorActivity.this.finish();
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			
			
	
	}

	public class GetPlanDetailsAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		
		ProgressHUD mProgressHUD;
		GetPlanParameterSOAP getPlanParameterSOAP;
		String getPlanResult,getPlanResponse;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(PlanCalculatorActivity.this,getString(R.string.app_please_wait_label),true,true,this);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			getPlanParameterSOAP= new GetPlanParameterSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_PLAN_PARAMETER));
			try {
				getPlanResult=getPlanParameterSOAP.getParameter();
				getPlanResponse=getPlanParameterSOAP.getResponse();
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				getPlanResult="Internet connection is too slow";
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				getPlanResult="Please try again!!";
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getPlanResult="Please try again!!";
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			if(getPlanResult.length()>0){
				if(getPlanResult.equalsIgnoreCase("ok")){
					if(getPlanResponse.length()>0){
						Utils.log("Response",":"+getPlanResponse);
						parsePlanResponse(getPlanResponse);
					}
				}
				else{
					if(PlanCalculatorActivity.this.isRestricted()){
					}
					else
						AlertsBoxFactory.showAlert(getPlanResult, PlanCalculatorActivity.this);
				}
			}
			else{
				if(PlanCalculatorActivity.this.isRestricted()){
				}
				else
					AlertsBoxFactory.showAlert(getPlanResult, PlanCalculatorActivity.this);
			}
		}

		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public void parsePlanResponse(String json){
		Days days= new Days();
		HashMap<String, DayRate_Plan> hashMapDaysPlan= new HashMap<String, DayRate_Plan>();
		if(json!=null){
			if(json.length()>0){
				try {
					JSONObject mainJson= new JSONObject(json);
					JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
					
					//30 Day Parameter 
					if(NewDataSetJson.has("_30Days")){
						JSONObject json_30=NewDataSetJson.getJSONObject("_30Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						ArrayList<DataLimit> alDataLimits= new ArrayList<DataLimit>();
						ArrayList<Speed_After_Vol> alSpeed_After_Vols= new ArrayList<Speed_After_Vol>();
						
						// Speed Data inserted.
						String str_Speed=json_30.optString("Speed","");
						String str_Speed_Rate=json_30.optString("BasicRate","");
						Utils.log("Speed Values",":"+str_Speed);
						Utils.log("Speed Rate",":"+str_Speed_Rate);
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						// DataLimit Data inserted.
						String str_DataLimit=json_30.optString("DataLimit","");
						String str_Gb_Rate=json_30.optString("PerGB","");
						Utils.log("DataLimit Values",":"+str_DataLimit);
						Utils.log("DataLimit Rate",":"+str_Gb_Rate);
						if(str_DataLimit.length()>0&&str_Gb_Rate.length()>0){
							String[] arr_data_limit=str_DataLimit.split(",");
							
							if(arr_data_limit.length>0){
								for(int i=0;i<arr_data_limit.length;i++){
									DataLimit dataLimit= new DataLimit();
									dataLimit.setData_Gb_Value(arr_data_limit[i]);
									dataLimit.setData_Gb_Rate(Double.valueOf(str_Gb_Rate));
									alDataLimits.add(dataLimit);
								}
							}
						}
						
						//Post Speed Data inserted.
						String str_PostSpeed=json_30.optString("PostSpeed","");
						String str_PostRate=json_30.optString("PostRate","");
						Utils.log("PostSpeed Values",":"+str_PostSpeed);
						Utils.log("PostSpeed Rate",":"+str_PostRate);
						if(str_PostSpeed.length()>0&&str_PostRate.length()>0){
							String[] arr_post_speed=str_PostSpeed.split(",");
							String[] arr_post_speed_rate=str_PostRate.split(",");
							if(arr_post_speed.length>0&&arr_post_speed_rate.length>0){
								for(int i=0;i<arr_post_speed.length;i++){
									Speed_After_Vol speed_After_Vol=new Speed_After_Vol();
									speed_After_Vol.setSpeed_AV_Value(arr_post_speed[i]);
									speed_After_Vol.setSpeed_AV_Rate(Double.valueOf(arr_post_speed_rate[i]));
									alSpeed_After_Vols.add(speed_After_Vol);
								}
							}
						}
						if(alSpeeds.size()>0&&alSpeed_After_Vols.size()>0&&alDataLimits.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
							dayRate_Plan.setAlSpeed_After_Vols(alSpeed_After_Vols);
							dayRate_Plan.setAlDataLimits(alDataLimits);
							dayRate_Plan.setMultiple(1);
							hashMapDaysPlan.put("30", dayRate_Plan);
						}
						
					
					}
					
					
					if(NewDataSetJson.has("_90Days")){
						JSONObject json_90=NewDataSetJson.getJSONObject("_90Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						ArrayList<DataLimit> alDataLimits= new ArrayList<DataLimit>();
						ArrayList<Speed_After_Vol> alSpeed_After_Vols= new ArrayList<Speed_After_Vol>();
						
						// Speed Data inserted.
						String str_Speed=json_90.optString("Speed","");
						String str_Speed_Rate=json_90.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						// DataLimit Data inserted.
						String str_DataLimit=json_90.optString("DataLimit","");
						String str_Gb_Rate=json_90.optString("PerGB","");
						if(str_DataLimit.length()>0&&str_Gb_Rate.length()>0){
							String[] arr_data_limit=str_DataLimit.split(",");
							
							if(arr_data_limit.length>0){
								for(int i=0;i<arr_data_limit.length;i++){
									DataLimit dataLimit= new DataLimit();
									dataLimit.setData_Gb_Value(arr_data_limit[i]);
									dataLimit.setData_Gb_Rate(Double.valueOf(str_Gb_Rate));
									alDataLimits.add(dataLimit);
								}
							}
						}
						
						//Post Speed Data inserted.
						String str_PostSpeed=json_90.optString("PostSpeed","");
						String str_PostRate=json_90.optString("PostRate","");
						if(str_PostSpeed.length()>0&&str_PostRate.length()>0){
							String[] arr_post_speed=str_PostSpeed.split(",");
							String[] arr_post_speed_rate=str_PostRate.split(",");
							if(arr_post_speed.length>0&&arr_post_speed_rate.length>0){
								for(int i=0;i<arr_post_speed.length;i++){
									Speed_After_Vol speed_After_Vol=new Speed_After_Vol();
									speed_After_Vol.setSpeed_AV_Value(arr_post_speed[i]);
									speed_After_Vol.setSpeed_AV_Rate(Double.valueOf(arr_post_speed_rate[i]));
									alSpeed_After_Vols.add(speed_After_Vol);
								}
							}
						}
						if(alSpeeds.size()>0&&alSpeed_After_Vols.size()>0&&alDataLimits.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
							dayRate_Plan.setAlSpeed_After_Vols(alSpeed_After_Vols);
							dayRate_Plan.setAlDataLimits(alDataLimits);
							dayRate_Plan.setMultiple(3);
							hashMapDaysPlan.put("90", dayRate_Plan);
						}
					}
					
					if(NewDataSetJson.has("_180Days")){
						JSONObject json_180=NewDataSetJson.getJSONObject("_180Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						ArrayList<DataLimit> alDataLimits= new ArrayList<DataLimit>();
						ArrayList<Speed_After_Vol> alSpeed_After_Vols= new ArrayList<Speed_After_Vol>();
						
						// Speed Data inserted.
						String str_Speed=json_180.optString("Speed","");
						String str_Speed_Rate=json_180.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						// DataLimit Data inserted.
						String str_DataLimit=json_180.optString("DataLimit","");
						String str_Gb_Rate=json_180.optString("PerGB","");
						if(str_DataLimit.length()>0&&str_Gb_Rate.length()>0){
							String[] arr_data_limit=str_DataLimit.split(",");
							
							if(arr_data_limit.length>0){
								for(int i=0;i<arr_data_limit.length;i++){
									DataLimit dataLimit= new DataLimit();
									dataLimit.setData_Gb_Value(arr_data_limit[i]);
									dataLimit.setData_Gb_Rate(Double.valueOf(str_Gb_Rate));
									alDataLimits.add(dataLimit);
								}
							}
						}
						
						//Post Speed Data inserted.
						String str_PostSpeed=json_180.optString("PostSpeed","");
						String str_PostRate=json_180.optString("PostRate","");
						if(str_PostSpeed.length()>0&&str_PostRate.length()>0){
							String[] arr_post_speed=str_PostSpeed.split(",");
							String[] arr_post_speed_rate=str_PostRate.split(",");
							if(arr_post_speed.length>0&&arr_post_speed_rate.length>0){
								for(int i=0;i<arr_post_speed.length;i++){
									Speed_After_Vol speed_After_Vol=new Speed_After_Vol();
									speed_After_Vol.setSpeed_AV_Value(arr_post_speed[i]);
									speed_After_Vol.setSpeed_AV_Rate(Double.valueOf(arr_post_speed_rate[i]));
									alSpeed_After_Vols.add(speed_After_Vol);
								}
							}
						}
						if(alSpeeds.size()>0&&alSpeed_After_Vols.size()>0&&alDataLimits.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
							dayRate_Plan.setAlSpeed_After_Vols(alSpeed_After_Vols);
							dayRate_Plan.setAlDataLimits(alDataLimits);
							dayRate_Plan.setMultiple(6);
							hashMapDaysPlan.put("180", dayRate_Plan);
						}
					}
					
					if(NewDataSetJson.has("_365Days")){
						JSONObject json_365=NewDataSetJson.getJSONObject("_365Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						ArrayList<DataLimit> alDataLimits= new ArrayList<DataLimit>();
						ArrayList<Speed_After_Vol> alSpeed_After_Vols= new ArrayList<Speed_After_Vol>();
						
						// Speed Data inserted.
						String str_Speed=json_365.optString("Speed","");
						String str_Speed_Rate=json_365.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						// DataLimit Data inserted.
						String str_DataLimit=json_365.optString("DataLimit","");
						String str_Gb_Rate=json_365.optString("PerGB","");
						if(str_DataLimit.length()>0&&str_Gb_Rate.length()>0){
							String[] arr_data_limit=str_DataLimit.split(",");
							
							if(arr_data_limit.length>0){
								for(int i=0;i<arr_data_limit.length;i++){
									DataLimit dataLimit= new DataLimit();
									dataLimit.setData_Gb_Value(arr_data_limit[i]);
									dataLimit.setData_Gb_Rate(Double.valueOf(str_Gb_Rate));
									alDataLimits.add(dataLimit);
								}
							}
						}
						
						//Post Speed Data inserted.
						String str_PostSpeed=json_365.optString("PostSpeed","");
						String str_PostRate=json_365.optString("PostRate","");
						if(str_PostSpeed.length()>0&&str_PostRate.length()>0){
							String[] arr_post_speed=str_PostSpeed.split(",");
							String[] arr_post_speed_rate=str_PostRate.split(",");
							if(arr_post_speed.length>0&&arr_post_speed_rate.length>0){
								for(int i=0;i<arr_post_speed.length;i++){
									Speed_After_Vol speed_After_Vol=new Speed_After_Vol();
									speed_After_Vol.setSpeed_AV_Value(arr_post_speed[i]);
									speed_After_Vol.setSpeed_AV_Rate(Double.valueOf(arr_post_speed_rate[i]));
									alSpeed_After_Vols.add(speed_After_Vol);
								}
							}
						}
						if(alSpeeds.size()>0&&alSpeed_After_Vols.size()>0&&alDataLimits.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
							dayRate_Plan.setAlSpeed_After_Vols(alSpeed_After_Vols);
							dayRate_Plan.setAlDataLimits(alDataLimits);
							dayRate_Plan.setMultiple(12);
							hashMapDaysPlan.put("365", dayRate_Plan);
						}
					}
					
	//***************************************** FOR UNLIMITED PLAN****************************************************************//
					
					if(NewDataSetJson.has("UL_30Days")){
						if(NewDataSetJson.get("UL_30Days") instanceof JSONObject){
						JSONObject json_30=NewDataSetJson.getJSONObject("UL_30Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						
						
						// Speed Data inserted.
						String str_Speed=json_30.optString("Speed","");
						String str_Speed_Rate=json_30.optString("BasicRate","");
						Utils.log("Unlimited Speed Values",":"+str_Speed);
						Utils.log("Unlimited Speed Rate",":"+str_Speed_Rate);
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
				
						if(alSpeeds.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
						
							dayRate_Plan.setMultiple(1);
							hashMapDaysPlan.put("ul_30", dayRate_Plan);
						}
						}
					
					}
					
					
					if(NewDataSetJson.has("UL_90Days")){
						if(NewDataSetJson.get("UL_90Days") instanceof JSONObject){
						JSONObject json_90=NewDataSetJson.getJSONObject("UL_90Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						
						
						// Speed Data inserted.
						String str_Speed=json_90.optString("Speed","");
						String str_Speed_Rate=json_90.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						
						if(alSpeeds.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
						
							dayRate_Plan.setMultiple(3);
							hashMapDaysPlan.put("ul_90", dayRate_Plan);
						}
						}
					}
					
					if(NewDataSetJson.has("UL_180Days")){
						if(NewDataSetJson.get("UL_180Days") instanceof JSONObject){
						JSONObject json_180=NewDataSetJson.getJSONObject("UL_180Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						
						
						// Speed Data inserted.
						String str_Speed=json_180.optString("Speed","");
						String str_Speed_Rate=json_180.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						
						if(alSpeeds.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
						
							dayRate_Plan.setMultiple(6);
							hashMapDaysPlan.put("ul_180", dayRate_Plan);
						}
						}
					}
					
					if(NewDataSetJson.has("UL_365Days")){
						if(NewDataSetJson.get("UL_365Days") instanceof JSONObject){
						JSONObject json_365=NewDataSetJson.getJSONObject("UL_365Days");
						DayRate_Plan dayRate_Plan= new DayRate_Plan();
						ArrayList<Speed> alSpeeds= new ArrayList<Speed>();
						
						
						// Speed Data inserted.
						String str_Speed=json_365.optString("Speed","");
						String str_Speed_Rate=json_365.optString("BasicRate","");
						if(str_Speed.length()>0&&str_Speed_Rate.length()>0){
							String[] arr_speed=str_Speed.split(",");
							String[] arr_speed_rate=str_Speed_Rate.split(",");
							if(arr_speed.length>0&&arr_speed_rate.length>0){
								for(int i=0;i<arr_speed.length;i++){
									Speed speed=new Speed();
									speed.setSpeed_Value(arr_speed[i]);
									speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[i]));
									alSpeeds.add(speed);
								}
							}
						}
						
						
						if(alSpeeds.size()>0){
							dayRate_Plan.setAlSpeeds(alSpeeds);
							
							dayRate_Plan.setMultiple(12);
							hashMapDaysPlan.put("ul_365", dayRate_Plan);
						}
						}
					}
					
					
					Utils.log("HashMap","size"+hashMapDaysPlan.size());
					days.setHashMapDays(hashMapDaysPlan);
					
					try {
						InternalStorage.writeObject(PlanCalculatorActivity.this, KEY, days);
						SharedPreferences.Editor edit= sharedPreferences.edit();
						edit.putBoolean("local_plan", true);
						edit.commit();
						
						 try {
								plan_days=(Days)InternalStorage.readObject(PlanCalculatorActivity.this, KEY);
								
								if(plan_days!=null){
									hashMapStored=plan_days.getHashMapDays();
									//set30Days();
									//set_Number_Days("30");
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	public void set_Number_Days(String number){
		if (hashMapStored != null) {
			flag_days=false;flag_data_limit=false;flag_speed_post=false;
			name_sel_speed="";
			name_sel_data_limit="";
			name_sel_post_speed="";
			getPlanName();
			DayRate_Plan dayRate_Plan_Local = hashMapStored.get(number);
			if (dayRate_Plan_Local != null) {
				gvSpeed.setVisibility(View.VISIBLE);
				gvData.setVisibility(View.VISIBLE);
				gvPostSpeed.setVisibility(View.VISIBLE);
				ll_Paynow.setVisibility(View.VISIBLE);
				if (dayRate_Plan_Local.getAlSpeeds() != null) {
					if (dayRate_Plan_Local.getAlSpeeds().size() < 6) {
						gvSpeed.setNumColumns(2);
					} else {
						gvSpeed.setNumColumns(3);
						if (dayRate_Plan_Local.getAlSpeeds().size() > 9) {
							gvSpeed.setNumColumns(4);
						}
					}
					alSpeeds=dayRate_Plan_Local.getAlSpeeds();
					gridSpeedAdapter = new GridSpeedAdapter(
							PlanCalculatorActivity.this, R.layout.gv_data_row_item,
							alSpeeds, "");
					gvSpeed.setAdapter(gridSpeedAdapter);

				}

				if (current_type.equalsIgnoreCase("fup")) {
					if (dayRate_Plan_Local.getAlDataLimits() != null) {
						if (dayRate_Plan_Local.getAlDataLimits().size() < 6) {
							gvData.setNumColumns(2);
						} else {
							gvData.setNumColumns(3);
							if (dayRate_Plan_Local.getAlDataLimits().size() > 9) {
								gvData.setNumColumns(4);
							}
						}
						alDataLimits=dayRate_Plan_Local.getAlDataLimits();
						gridDataLimit = new GridDataLimit(
								PlanCalculatorActivity.this,
								R.layout.gv_data_row_item,
								alDataLimits, "");
						gvData.setAdapter(gridDataLimit);
					}

					if (dayRate_Plan_Local.getAlDataLimits().size() > 0) {
						pergb_rate = dayRate_Plan_Local.getAlDataLimits()
								.get(0).getData_Gb_Rate();

					}

					if (dayRate_Plan_Local.getAlSpeed_After_Vols() != null) {
						if (dayRate_Plan_Local.getAlSpeed_After_Vols().size() < 6) {
							gvPostSpeed.setNumColumns(2);
						} else {
							gvPostSpeed.setNumColumns(3);
							if (dayRate_Plan_Local.getAlSpeed_After_Vols()
									.size() > 9) {
								gvPostSpeed.setNumColumns(4);
							}
						}
						alSpeed_After_Vols=dayRate_Plan_Local.getAlSpeed_After_Vols();
						
						gridSpeedPostAdapter = new GridSpeedPostAdapter(
								PlanCalculatorActivity.this,
								R.layout.gv_row_item,
								alSpeed_After_Vols,
								"speed");
						gvPostSpeed.setAdapter(gridSpeedPostAdapter);
					}

					multiplier = dayRate_Plan_Local.getMultiple();

					gridSpeedAdapter.notifyDataSetChanged();
					gridDataLimit.notifyDataSetChanged();
					gridSpeedPostAdapter.notifyDataSetChanged();
				} else {
					//gvSpeed.setVisibility(View.GONE);
					gvData.setVisibility(View.GONE);
					gvPostSpeed.setVisibility(View.GONE);
					if(current_type.equalsIgnoreCase("fup")){
					if(days.equalsIgnoreCase("30"))
						multiplier=1;
					if(days.equalsIgnoreCase("90"))
						multiplier=3;
					if(days.equalsIgnoreCase("180"))
						multiplier=6;
					if(days.equalsIgnoreCase("365"))
						multiplier=12;
					}
					
					if(current_type.equalsIgnoreCase("ul")){
						if(days.equalsIgnoreCase("30"))
							multiplier=1;
						if(days.equalsIgnoreCase("90"))
							multiplier=1;
						if(days.equalsIgnoreCase("180"))
							multiplier=1;
						if(days.equalsIgnoreCase("365"))
							multiplier=1;
						}
					
					//ll_Paynow.setVisibility(View.GONE);
				}
			} else {
				gvSpeed.setVisibility(View.GONE);
				gvData.setVisibility(View.GONE);
				gvPostSpeed.setVisibility(View.GONE);
				ll_Paynow.setVisibility(View.GONE);
			}
		}
	}
	
	public void initialsetup(){
		
		speed_rate=0.0;data_limit_rate=0.0;speed_post_rate=0.0;final_package_rate=0.0;
		flag_speed=false;flag_data_limit=false;flag_speed_post=false;
		tvDays.setVisibility(View.GONE);
		tvData.setVisibility(View.GONE);
		tvSpeed.setVisibility(View.GONE);
		tvPostSpeed.setVisibility(View.GONE);
		tvFinalPrice.setVisibility(View.GONE);
	}
	
	/* private PointerPopupWindow create() {
	        //warning: you must specify the window width explicitly(do not use WRAP_CONTENT or MATCH_PARENT)
	        PointerPopupWindow p = new PointerPopupWindow(this, getResources().getDimensionPixelSize(R.dimen.popup_width));
	        TextView textView = new TextView(this);
	        textView.setGravity(Gravity.CENTER);
	        textView.setText("Popup");
	        textView.setTextSize(40);
	        textView.setTextColor(Color.WHITE);
	        p.setContentView(textView);
	        p.setBackgroundDrawable(new ColorDrawable(0xb3111213));
	        p.setPointerImageRes(R.drawable.ic_popup_pointer);
	        return p;
	    }*/
	
	public void showInstructions(){
		if(flag_plan_type){
			
			if(flag_days){
				
				if(flag_speed){
					if(current_type.equalsIgnoreCase("fup")){
					if(flag_data_limit){
						
						if(flag_speed_post){
							
							tvHelp.setText("6. Please select Pay Now.");
							
						}
						else{
							tvHelp.setText("5. Please select post speed.");
							
						}
					}
					else{
						tvHelp.setText("4. Please select Data Transfer.");
						
					}
					}
					else{
						if(current_type.equalsIgnoreCase("ul")){
							tvHelp.setText("4. Please select Pay Now.");
							
						}
					}
				}
				else{
					tvHelp.setText("3. Please select Speed.");
					
				}
			}
			else{
				//guideUser("Validity", "Please select Validity", gvPostSpeed, 2);
				tvHelp.setText("2. Please select Validity.");
				
			}
		}
		else{
			tvHelp.setText("1. Please select Plan Type FUP or Unlimited.");
			
		}
	}

	public void resetFlags(){
		flag_plan_type=false;flag_days=false;flag_speed=false;flag_data_limit=false;flag_speed_post=false;
	}
	
	public void showFinalDialog(){
		final Dialog dialog = new Dialog(PlanCalculatorActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.dialog_plan_calc);
		int width = 0;
		int height =0;
		
		
		    Point size = new Point();
		    WindowManager w =getWindowManager();

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		        w.getDefaultDisplay().getSize(size);
		        width = size.x;
		        height = size.y;
		    } else {
		        Display d = w.getDefaultDisplay();
		        width = d.getWidth();
		        height   = d.getHeight();;
		    }
		
		
		TextView tvDialogPlanName=(TextView) dialog.findViewById(R.id.tvDialogPlanName);
		tvDialogPrice	=(TextView) dialog.findViewById(R.id.tvDialogPlanPrice);
		TextView tvPlanLabelPrice=(TextView) dialog.findViewById(R.id.tvPlanLabelPrice);
		RelativeLayout rl_Netbanking=(RelativeLayout)dialog.findViewById(R.id.rl_dialog_netbanking);
		RelativeLayout rl_DebitCredit=(RelativeLayout)dialog.findViewById(R.id.rl_dialog_creditdebit);
		RelativeLayout rl_og_price=(RelativeLayout)dialog.findViewById(R.id.rl_og_price);
		TextView tv_og_price=(TextView)dialog.findViewById(R.id.tvOgDialogPlanPrice);
		TextView tv_discountLabel=(TextView)dialog.findViewById(R.id.tvDiscountLabel);
		LinearLayout ll_update_from=(LinearLayout)dialog.findViewById(R.id.ll_update_from);
		ll_addtional_details=(LinearLayout)dialog.findViewById(R.id.ll_addtional_details);
		llClickDetails=(LinearLayout)dialog.findViewById(R.id.llClickDetails);
		TextView tv_click_here=(TextView)dialog.findViewById(R.id.tvClickDetails);
		final RadioGroup rg_check=(RadioGroup)dialog.findViewById(R.id.rg_dialog);
		rb_immediate =(RadioButton)dialog.findViewById(R.id.rbimmediate);
		
		String	sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
		SharedPreferences sharedPreferences2 = getApplicationContext()
				.getSharedPreferences(sharedPreferences_renewal, 0); 
		if(!sharedPreferences2.getString("PackageName", "-").equalsIgnoreCase(tvPlanName.getText().toString())){
			rb_immediate.setVisibility(View.VISIBLE);
			datafrom="changepack";
		}
		else{
			rb_immediate.setVisibility(View.GONE);
			datafrom="";
		}
		rb_next =(RadioButton)dialog.findViewById(R.id.rbnext);
		if(DiscountPercentage.length()>0){
		if(Double.valueOf(DiscountPercentage)>0.0){
			
			rl_og_price.setVisibility(View.VISIBLE);
			tv_discountLabel.setVisibility(View.VISIBLE);
			//tv_discountLabel.setText("You have got "+DiscountPercentage+"% discount for online payment.");
			tv_discountLabel.setText("Avail of a "+DiscountPercentage+"% discount by paying online.");
			tvDialogPrice.setText(String.valueOf(DiscountFinalAmount));
			tv_og_price.setText(String.valueOf(final_package_rate));
			
		}
		else{
			tvPlanLabelPrice.setText(getString(R.string.package_plan_price));
			rl_og_price.setVisibility(View.GONE);
			tv_discountLabel.setVisibility(View.GONE);
			tvDialogPrice.setText(String.valueOf(final_package_rate));
		}
		
		}
		else{
			tvPlanLabelPrice.setText(getString(R.string.package_plan_price));
			rl_og_price.setVisibility(View.GONE);
			tv_discountLabel.setVisibility(View.GONE);
			tvDialogPrice.setText(String.valueOf(final_package_rate));
		}
		if(AdditionalAmountType.length()>0){
		/*	renewOption.setVisibility(View.GONE);
			adjOption.setVisibility(View.GONE);
			TextView02.setVisibility(View.GONE);*/
			ll_update_from.setVisibility(View.GONE);
			compulsory_immediate=true;
		}
		else{
			/*renewOption.setVisibility(View.VISIBLE);
			adjOption.setVisibility(View.VISIBLE);
			TextView02.setVisibility(View.VISIBLE);*/
			ll_update_from.setVisibility(View.VISIBLE);
			compulsory_immediate=false;
		}
		
		if(Double.valueOf(additionalAmount.getFinalcharges())>0){
			tvDialogPrice.setText(additionalAmount.getFinalcharges());
			
		}
		if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
			is_payemnt_detail=true;
		}
		if(Double.valueOf(additionalAmount.getFineAmount())>0){
			is_payemnt_detail=true;
		}
		if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
			is_payemnt_detail=true;
		}
		if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
			is_payemnt_detail=true;
		}
		if(is_payemnt_detail){
			tvDialogPrice.setText(additionalAmount.getFinalcharges());
			llClickDetails.setVisibility(View.VISIBLE);
		}
		else{
			llClickDetails.setVisibility(View.GONE);
		}
		
		if(Double.valueOf(finalcharges)>0.0){
			
		}
		else{
			AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
		}
		
		tvDialogPlanName.setText(tvPlanName.getText().toString());
		
		llClickDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(is_payemnt_detail){
					if(isDetailShow){
						ll_addtional_details.setVisibility(View.GONE);
						isDetailShow=false;
					}
					else{
						ll_addtional_details.setVisibility(View.VISIBLE);
						isDetailShow=true;
					}
					
					showPaymentDetails(additionalAmount,dialog);
				}
				else{
					ll_addtional_details.setVisibility(View.GONE);
				}
			}
		});
		rb_immediate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					AlertsBoxFactory.showAlert("This Feature will <b>Discontinue</b> The Current Package and New Package will be Applied from Today.<br/> Are you Sure? ", PlanCalculatorActivity.this);
					
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						 new AdjustmentWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
					 else
						 new AdjustmentWebService().execute();
				
					updateFrom="I";
				}
				else{
					
				}
			}
		});
		
		rb_next.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					updateFrom="S";
					tvDialogPrice.setText(String.valueOf(finalcharges));
					additionalAmount.setFinalcharges(finalcharges);
				}
				else{
					
				}
			}
		});
		
		rl_DebitCredit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.isOnline(PlanCalculatorActivity.this)){
					checkValue="creditdebit";
					//new GetMemberDetailWebService().execute();
					
					if(!compulsory_immediate){
							if(updateFrom.length()>0){
								show_pg_dialog(checkValue);
							}else{
								Toast.makeText(PlanCalculatorActivity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
							}
						}
						else{
							show_pg_dialog(checkValue);
						}
				}
			}
		});
		
		rl_Netbanking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.isOnline(PlanCalculatorActivity.this)){
					
					checkValue="netbanking";
					//new GetMemberDetailWebService().execute();
					if(!compulsory_immediate){
						if(updateFrom.length()>0){
							show_pg_dialog(checkValue);
						}
						else{
							Toast.makeText(PlanCalculatorActivity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
						}
					}
					else{
						show_pg_dialog(checkValue);
					}
					
				}
			}
		});
		
		dialog.show();
		//(width/2)+((width/2)/2)
		//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	private class AdjustmentWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		ProgressHUD mProgressHUD;

		String adjustmentResult="",adjustmentResponse="";

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PlanCalculatorActivity.this,getString(R.string.app_please_wait_label), true,true, this);
			
		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
		
			
		}
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			
			
			if (adjustmentResult.trim().equalsIgnoreCase("ok")) {
				try {
				Double	NewPlanRate = Double.parseDouble(adjustmentResponse);
				tvDialogPrice.setText(Double.toString(NewPlanRate));
				additionalAmount.setFinalcharges(String.valueOf(NewPlanRate));
				//	validity.setText(PackageValidity);
				//	servicetax.setText(ServcieTax);
					
				} catch (NumberFormatException nue) {
					//RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
					//radioGroup.clearCheck();
					/*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/
					if(adjustmentResponse.equalsIgnoreCase("anyType{}")){
						rb_immediate.setChecked(false);
						updateFrom="";
						rb_immediate.setChecked(false);
						updateFrom="";
						AlertsBoxFactory.showAlert("Conversion is not possible.", PlanCalculatorActivity.this);
					}else{
						rb_immediate.setChecked(false);
						updateFrom="";
						AlertsBoxFactory.showAlert(adjustmentResponse, PlanCalculatorActivity.this);
					}
				}

			} else {
				rb_immediate.setChecked(false);
				updateFrom="";
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ adjustmentResult, PlanCalculatorActivity.this);
			}
			
			//this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				AdjustmentSOAPCalci adj_SOAP = new AdjustmentSOAPCalci(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.METHOD_PACKAGEADJUSTMENT_FOR_CALCI));
				adj_SOAP.setMemberId(Long.parseLong(MemberId));
				//setPlanDetails();
				adj_SOAP.setNewPlan(tvPlanName.getText().toString());
				//adjCaller.setAreaCode(AreaCode);
				//adjCaller.setAreaCodeFilter(AreaCodeFilter);
				
				adjustmentResult=	adj_SOAP.CallAdjustmentAmountSOAP(DiscountFinalAmount);
				adjustmentResponse=adj_SOAP.getResponse();
			

			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
			}
			return null;
		}
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}

	}
	
public class GetFinalPriceAsynctask extends AsyncTask<String, Void, Void> implements OnCancelListener{
	ProgressHUD mProgressHUD;
	String getFinalPriceResult="",getFinalPriceResponse="";
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressHUD = ProgressHUD.show(PlanCalculatorActivity.this,getString(R.string.app_please_wait_label), true,true, this);
	}
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		GetFinalPackageSOAP getFinalPackageSOAP= new GetFinalPackageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_CALCULATOR_FINAL_PRICE));
		try {
			getFinalPriceResult=getFinalPackageSOAP.getFinalPrice(tvPlanName.getText().toString(), String.valueOf(final_package_rate));
			getFinalPriceResponse=getFinalPackageSOAP.getResponse();
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressHUD.dismiss();
		if(getFinalPriceResult.length()>0){
			if(getFinalPriceResult.equalsIgnoreCase("ok")){
				if(getFinalPriceResponse.length()>0){
					String[] final_price_discount=getFinalPriceResponse.split("#");
					if(final_price_discount.length>1){
						DiscountFinalAmount=final_price_discount[0];
						DiscountPercentage=final_price_discount[1];
						if(Double.valueOf(DiscountFinalAmount)>0.0)
						showFinalDialog();
						else{
							AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
						}
						
					}
					else{
						AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
				}
			}
			else{
				AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
			}
		}
		else{
			AlertsBoxFactory.showAlert(getString(R.string.fail_response), PlanCalculatorActivity.this);
		}
	}
	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onCancelled() {
		mProgressHUD.dismiss();
	
		
	}
}

public class GetCalciVersion extends AsyncTask<String, Void, Void> implements OnCancelListener{
	ProgressHUD mProgressHUD;
	String calc_version_result="",calc_version_response="";
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressHUD = ProgressHUD.show(PlanCalculatorActivity.this,getString(R.string.app_please_wait_label), true,false, this);
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		GetCurrentVersionSOAP getCurrentVersionsoap= new GetCurrentVersionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CURRENT_VERSION));
		try {
			calc_version_result=getCurrentVersionsoap.getCurrentVersion("C", CalciVersion,MemberId);
			calc_version_response=getCurrentVersionsoap.getResponse();
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			calc_version_result="Internet is too slow";
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			calc_version_result="Internet is too slow";
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			calc_version_result="Please try again!!";
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressHUD.dismiss();
		if(calc_version_result.length()>0){
			if(calc_version_result.equalsIgnoreCase("ok")){
				Utils.log("Version",":"+calc_version_response);
				String version_response[]=calc_version_response.split("#");
				if(version_response.length>2&&version_response.length>0){
					if(version_response[2].equalsIgnoreCase("0")){
						showEndDialog("Coming Soon!");
					}
					else{
														
						if(version_response.length>1){
							is_version_checked=true;
					
					if(version_response[0].equalsIgnoreCase(CalciVersion)){
						SharedPreferences.Editor edit=sharedPreferences.edit();
						edit.putString("Calc_Version", version_response[0]);
						edit.commit();
					}
					else{
						SharedPreferences.Editor edit=sharedPreferences.edit();
						edit.putString("Calc_Version", version_response[0]);
						edit.commit();
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
							 new GetPlanDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						 }
						 else{
							 new GetPlanDetailsAsyncTask().execute();
						 }
					}
					
					
					
					/*if(version_response[0].equalsIgnoreCase("True")){
						SharedPreferences.Editor edit=sharedPreferences.edit();
						edit.putString("Calc_Version", version_response[1]);
						edit.commit();
					}
					if(version_response[0].equalsIgnoreCase("False")){
						SharedPreferences.Editor edit=sharedPreferences.edit();
						edit.putString("Calc_Version", version_response[1]);
						edit.commit();
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
							 new GetPlanDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						 }
						 else{
							 new GetPlanDetailsAsyncTask().execute();
						 }
					}*/
				}
					}
				}
			}
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}
	
	}

@Subscribe
public void	onFinishEvent(FinishEvent event){
	if(PlanCalculatorActivity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
		PlanCalculatorActivity.this.finish();
	}
	
}

public class GetFinalDeductedAmtAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{

	String getResponse="",getResult="";
	ProgressHUD mProgressHUD;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		//ll_addtional_details.setVisibility(View.GONE);
		//llClickDetails.setVisibility(View.GONE);
		Utils.log("GetFinalDeductedAmtAsyncTask","started");
		mProgressHUD=ProgressHUD.show(PlanCalculatorActivity.this, getString(R.string.app_please_wait_label), true, false, this);
	}
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		GetAdditionalAmountSOAP  getAdditionalAmountSOAP= new GetAdditionalAmountSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ADDITIONAL_AMT));
		try {
			getResult=getAdditionalAmountSOAP.getAddtionalAmount(memberloginid, "0",String.valueOf(final_package_rate));
			
			getResponse=getAdditionalAmountSOAP.getResponse();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressHUD.dismiss();
		if(getResult.length()>0){
			if(getResult.equalsIgnoreCase("ok")){
				if(getResponse.length()>0){
					Utils.log("Get Additional",":"+getResponse);
					
					parseAdditionalAmtData(getResponse);
				}
			}
		}
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}
	
}

public void parseAdditionalAmtData(String json){
	JSONObject mainJson;
	try {
		mainJson = new JSONObject(json);
		JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
		if(NewDataSetJson.has("AdditionAmount")){
			
			JSONObject json_add_amt=NewDataSetJson.getJSONObject("AdditionAmount");
			 PackageRate=json_add_amt.optString("PackageRate","0");
			// final_package_rate=Double.valueOf(json_add_amt.optString("finalcharges","0"));
			 DiscountFinalAmount=(json_add_amt.optString("finalcharges","0"));
			 AdditionalAmount=json_add_amt.optString("AdditionalAmount","0");
			 AdditionalAmountType=json_add_amt.optString("AdditionalAmountType","");
			 DaysFineAmount=json_add_amt.optString("DaysFineAmount","0");
			 DiscountAmount=json_add_amt.optString("DiscountAmount","0");
			 finalcharges=json_add_amt.optString("finalcharges","0");
			 FineAmount=json_add_amt.optString("FineAmount","0");
			 DiscountPercentage=json_add_amt.optString("OnlineDiscountInPer","0");
			 boolean is_cheque_bounced=json_add_amt.optBoolean("IsChequeBounce",false);
			 additionalAmount= new AdditionalAmount(PackageRate,AdditionalAmount, AdditionalAmountType, DaysFineAmount, DiscountAmount, finalcharges, FineAmount, DiscountPercentage,is_cheque_bounced);
			if(DiscountPercentage.length()>0){
				
			}
			if(is_cheque_bounced){
				AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), PlanCalculatorActivity.this);
			}
			else{
				showFinalDialog();
			}
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public void showPaymentDetails(AdditionalAmount additionalAmount ,Dialog v){
	if(additionalAmount!=null){
	//ll_addtional_details.setVisibility(View.VISIBLE);
	LinearLayout ll_package_rate,ll_add_amt,ll_add_reason,ll_discount_amt,ll_fine_amount,ll_days_fine_amt,ll_discount_per,ll_final_amt;
	
	TextView tv_package_rate,tv_add_amt,tv_add_reason,tv_discount_amt,tv_fine_amount,tv_days_fine_amt,tv_discount_per,tv_final_amt;
	
	ll_package_rate=(LinearLayout) v.findViewById(R.id.ll_package_rate);
	ll_add_amt=(LinearLayout) v.findViewById(R.id.ll_add_amt);
	ll_add_reason=(LinearLayout) v.findViewById(R.id.ll_add_reason);
	ll_discount_amt=(LinearLayout) v.findViewById(R.id.ll_discount_amt);
	ll_fine_amount=(LinearLayout) v.findViewById(R.id.ll_fine_amt);
	ll_days_fine_amt=(LinearLayout) v.findViewById(R.id.ll_days_fine_amt);
	ll_discount_per=(LinearLayout) v.findViewById(R.id.ll_discount_per);
	ll_final_amt=(LinearLayout) v.findViewById(R.id.ll_final_amount);
	
	tv_package_rate=(TextView) v.findViewById(R.id.tv_package_rate);
	tv_add_amt=(TextView) v.findViewById(R.id.tv_add_amt);
	tv_add_reason=(TextView) v.findViewById(R.id.tv_add_reason);
	tv_discount_amt=(TextView) v.findViewById(R.id.tv_discount_amt);
	tv_fine_amount=(TextView) v.findViewById(R.id.tv_fine_amt);
	tv_days_fine_amt=(TextView) v.findViewById(R.id.tv_days_fine_amt);
	tv_discount_per=(TextView) v.findViewById(R.id.tv_discount_per);
	tv_final_amt=(TextView) v.findViewById(R.id.tv_final_amount);
	
	if(Double.valueOf(additionalAmount.getPackageRate())>0){
		ll_package_rate.setVisibility(View.VISIBLE);
		tv_package_rate.setText(additionalAmount.getPackageRate());
	}
	else{
	
		ll_package_rate.setVisibility(View.GONE);
		
	}
		    
	if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
		ll_add_amt.setVisibility(View.VISIBLE);
		tv_add_amt.setText(additionalAmount.getAdditionalAmount());
	}
	else{
	
		ll_add_amt.setVisibility(View.GONE);
		
	}
	
	if(additionalAmount.getAdditionalAmountType().length()>0){
		ll_add_reason.setVisibility(View.GONE);
		tv_add_reason.setText(additionalAmount.getAdditionalAmountType());
	}
	else{
		
		ll_add_reason.setVisibility(View.GONE);
		
	}
	
	if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
		ll_discount_amt.setVisibility(View.VISIBLE);
		tv_discount_amt.setText(additionalAmount.getDiscountAmount());
	}
	else{
		
		ll_discount_amt.setVisibility(View.GONE);
		
	}
	
	if(Double.valueOf(additionalAmount.getFineAmount())>0){
		ll_fine_amount.setVisibility(View.VISIBLE);
		tv_fine_amount.setText(additionalAmount.getFineAmount());
	}
	else{
		
		ll_fine_amount.setVisibility(View.GONE);
		
	}
	
	if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
		ll_days_fine_amt.setVisibility(View.VISIBLE);
		tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
	}
	else{
		
		ll_days_fine_amt.setVisibility(View.GONE);
		
	}
	if(additionalAmount.getDiscountPercentage().length()>0){
	if(Double.valueOf(additionalAmount.getDiscountPercentage())>0){
		ll_discount_per.setVisibility(View.VISIBLE);
		tv_discount_per.setText(additionalAmount.getDiscountPercentage());
	}
	else{			
		ll_discount_per.setVisibility(View.GONE);			
	}
	}
	else{
		ll_discount_per.setVisibility(View.GONE);	
	}
	Utils.log("Final Price",":"+additionalAmount.getFinalcharges());
	if(Double.valueOf(additionalAmount.getFinalcharges())>0){
		Utils.log("Visible","true");
		ll_final_amt.setVisibility(View.VISIBLE);
		
		tv_final_amt.setText(additionalAmount.getFinalcharges());
	}
	else{
		Utils.log("Visible","false");
		ll_final_amt.setVisibility(View.GONE);			
	}
	
	
	}
	else{
		ll_addtional_details.setVisibility(View.GONE);
	}
}

/*	public void guideUser(String title,String message,View v ,int id){
		showtips = new ShowTipsBuilder(this)
	    .setTarget(ll_selectPlantType)
	    .setTitle(title)
	    .setDescription(message)
	    .setDelay(500)	  
	    .build();		
		showtips.show(this);
				
	}

	@Override
	public void gotItClicked() {
		// TODO Auto-generated method stub
		
	}*/

public void show_pg_dialog(String check) {
	pg_dialog = new Dialog(PlanCalculatorActivity.this);
	pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	pg_dialog.setContentView(R.layout.dialog_payment_gateway);
	
	int width = 0;
	int height =0;
	
	
	    Point size = new Point();
	    WindowManager w =((Activity)PlanCalculatorActivity.this).getWindowManager();

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	        w.getDefaultDisplay().getSize(size);
	        width = size.x;
	        height = size.y;
	    } else {
	        Display d = w.getDefaultDisplay();
	        width = d.getWidth();
	        height   = d.getHeight();;
	    }
	    RelativeLayout rl_payment_gateway_1=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_1);
	    RelativeLayout rl_payment_gateway_2=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_2);
	    
	    rl_payment_gateway_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.is_CCAvenue=false;
				proceed_to_pay();
			}
		});
	    
	    rl_payment_gateway_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPreferences1 = getApplicationContext()
						.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
			int	CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);
			if(CCAvenue>0){	
				Utils.is_CCAvenue=true;
				proceed_to_pay();
			}
			else{
				AlertsBoxFactory.showAlert("Coming soon!", PlanCalculatorActivity.this);
			}
			}

			
		});
	    
	    pg_dialog.show();
	    pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    pg_dialog.getWindow().setLayout((width/2)+(width/2), LayoutParams.WRAP_CONTENT);	
}

private void proceed_to_pay() {
			// TODO Auto-generated method stub
			if(compulsory_immediate){
				Intent i;
				if(Utils.is_CCAvenue)
					i = new Intent(PlanCalculatorActivity.this, MakeMyPayments_CCAvenue.class);
				else
				 i = new Intent(PlanCalculatorActivity.this, MakeMyPayments.class);
				i.putExtra("PackageName", tvPlanName.getText().toString());
				i.putExtra("PackageAmount", tvDialogPrice.getText().toString());
				i.putExtra("PackageValidity", days);
				i.putExtra("updateFrom", "I");
				i.putExtra("ServiceTax", "0");
				i.putExtra("datafrom", datafrom);
				i.putExtra("ClassName", PlanCalculatorActivity.this.getClass().getSimpleName());
				i.putExtra("addtional_amount", additionalAmount);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			else
			{
				if(updateFrom.length()>0){
					//PlanCalculatorActivity.this.finish();	
					Intent i;
					if(Utils.is_CCAvenue)
						i = new Intent(PlanCalculatorActivity.this, MakeMyPayments_CCAvenue.class);
					else
					 i = new Intent(PlanCalculatorActivity.this, MakeMyPayments.class);
					i.putExtra("PackageName", tvPlanName.getText().toString());
					i.putExtra("PackageAmount", tvDialogPrice.getText().toString());
					i.putExtra("PackageValidity", days);
					i.putExtra("updateFrom", updateFrom);
					i.putExtra("ServiceTax", "0");
					i.putExtra("datafrom", datafrom);
					i.putExtra("ClassName", PlanCalculatorActivity.this.getClass().getSimpleName());
					i.putExtra("addtional_amount", additionalAmount);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					}
				else{
					Toast.makeText(PlanCalculatorActivity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
				}
		}
		}	

public void showEndDialog(String Message){
	final Dialog dialog = new Dialog(PlanCalculatorActivity.this);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setCancelable(false);
	//tell the Dialog to use the dialog.xml as it's layout description
	dialog.setContentView(R.layout.dialog_box);
	int width = 0;
	int height =0;
	
	
	    Point size = new Point();
	    WindowManager w =getWindowManager();

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	        w.getDefaultDisplay().getSize(size);
	        width = size.x;
	        height = size.y;
	    } else {
	        Display d = w.getDefaultDisplay();
	        width = d.getWidth();
	        height   = d.getHeight();;
	    }
	
	
	
TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);
dtv.setText("Confirmation");

	TextView txt = (TextView) dialog.findViewById(R.id.tv);

	txt.setText(Html.fromHtml(Message));

	Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

	
	dialogButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			dialog.dismiss();
			PlanCalculatorActivity.this.finish();
			/*Intent i = new Intent(PlanCalculatorActivity,IONHome.class);
			startActivity(i);*/
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	});
	
	dialog.show();
	//(width/2)+((width/2)/2)
	//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);	
}
}

