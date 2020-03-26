/**
 * 
 */
package com.cnergee.mypage;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import all.interface_.IError;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import cnergee.plan.calc.Days;



import com.cnergee.calculator.obj.Calci_Data_Limit;
import com.cnergee.calculator.obj.Calci_Data_Limit_Adapter;
import com.cnergee.calculator.obj.Calci_Days;
import com.cnergee.calculator.obj.Calci_Post_Speed;
import com.cnergee.calculator.obj.Calci_Post_Speed_Adapter;
import com.cnergee.calculator.obj.Calci_Speed;
import com.cnergee.calculator.obj.Calci_Speed_Adapter;
import com.cnergee.mypage.PlanCalculatorActivity.GetCalciVersion;
import com.cnergee.mypage.PlanCalculatorActivity.GetPlanDetailsAsyncTask;
import com.cnergee.mypage.SOAP.AdjustmentSOAPCalci;
import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.GetPlanParameterSOAP;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.InternalStorage;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import cnergee.krishna.ookla.odigitel.R;
import com.squareup.otto.Subscribe;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class Make_my_plan_Activity extends BaseActivity {

	SeekBar seek;
	TextView tvDays, tvData, tvSpeed, tvFinalPrice, tvPlanName,tvPostSpeed,tvHelp;
	LinearLayout btn_30, btn_90, btn_180, btn_365;
	GridView gvSpeed, gvData,gvPostSpeed;
	LinearLayout ll_sec4_c, ll_sec4_cut, ll_Paynow,ll_selectPlantType;
	LinearLayout ll_data_transfer_box,ll_post_speed_box,ll_speed_box;
	
	Button btn_Fup,btn_Ul;
	 String CalciVersion="0.0";
	String current_type="";
	String current_days="30";
	HashMap<String, ArrayList<Calci_Speed>> hs_calci_days=new HashMap<String, ArrayList<Calci_Speed>>();
	
	Calci_Speed_Adapter speed_Adapter;
	Calci_Data_Limit_Adapter data_Limit_Adapter;
	Calci_Post_Speed_Adapter post_Speed_Adapter;
	
	ArrayList<Calci_Speed> alCalci_Speeds_Global;
	ArrayList<Calci_Data_Limit>alData_Limits_Global;
	ArrayList<Calci_Post_Speed> alPost_Speeds_Global;
	
	Calci_Speed calci_Speed_Global;
	
	public boolean flag_plan_type,flag_days=true,flag_speed,flag_data_limit,flag_speed_post;
	Double speed_rate=0.0,data_limit_rate=0.0,speed_post_rate=0.0,final_package_rate=0.0;
	String name_sel_speed="",name_sel_data_limit="",name_sel_post_speed="",days="30";
	
	String memberloginid="",MemberId="";
	Utils utils = new Utils();
	SharedPreferences sharedPreferences;
	
	String PackageRate,AdditionalAmount,AdditionalAmountType, DaysFineAmount,DiscountAmount,finalcharges,FineAmount,outstandingAmount;
	AdditionalAmount additionalAmount; 
	String DiscountFinalAmount="",DiscountPercentage="0.0";
	
	boolean is_payemnt_detail=false;
	boolean isDetailShow=false;
	LinearLayout ll_update_from;
	public boolean compulsory_immediate=false;

	boolean is_guide_shown=false;
	LinearLayout ll_select_days;
	RadioButton rb_immediate,rb_next;
	String datafrom="changepack";
	Dialog pg_dialog;
	TextView tvDialogPrice;
	LinearLayout ll_addtional_details,llClickDetails;
	String checkValue="";
	 String updateFrom="";
	 public boolean is_version_checked=false;
	 Calci_Days calci_Days=new Calci_Days();
		public static String KEY="PLAN_CALCI";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.test_activity_plan_calculator);
		BaseApplication.getEventBus().register(this);
		initializeControls();
		iError=(IError)this;
		resetFlags();
		  
		sharedPreferences = getApplicationContext()
				.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		MemberId = utils.getMemberId();
		memberloginid = utils.getMemberLoginID();
		  CalciVersion=sharedPreferences.getString("Calc_Version", "0");
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			new GetPlanDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		}
		else{
			new GetPlanDetailsAsyncTask().execute();
		}
	
		if(Utils.isOnline(Make_my_plan_Activity.this)){
			
			 /* if(sharedPreferences.getBoolean("local_plan_check", false)){
		
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
						 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					 }
					 else{
						 new GetCalciVersion().execute();
					 }
				
			  }
			  else{
				  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
						 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					 }
					 else{
						 new GetCalciVersion().execute();
					 }
			  }*/
		}
		else{
			showEndDialog("Please connect to internet!");
		}
		 
		 gvSpeed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if(flag_plan_type){
					flag_speed=true;
					flag_data_limit=false;
					flag_speed_post=false;
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
				
				calci_Speed_Global=(Calci_Speed)parent.getItemAtPosition(position);
				set_data_limit_adapter(calci_Speed_Global);
				set_post_speed_adapter(calci_Speed_Global);
				speed_rate=Double.valueOf(calci_Speed_Global.getCalc_speed_rate());
				Utils.log("current","days:"+current_days);
				Utils.log("Days","days:"+days);
				Utils.log("Speed rate","is"+calci_Speed_Global.getCalc_speed_rate());
				speed_post_rate=0.0;
				data_limit_rate=0.0;
				getDataPlan();
				name_sel_speed=calci_Speed_Global.getCalc_speed_value();
				getPlanName();
				
			}
		}
		});
		 
		 gvData.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Calci_Data_Limit calci_Data_Limit=(Calci_Data_Limit)parent.getItemAtPosition(position);
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
						data_limit_rate=Double.valueOf(calci_Data_Limit.getCalc_data_limit_rate());
						getDataPlan();
						name_sel_data_limit=calci_Data_Limit.getCalc_data_limit_value();
						getPlanName();
					}
					
				}
			});
		 
		 gvPostSpeed.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Calci_Post_Speed calci_Post_Speed=(Calci_Post_Speed)parent.getItemAtPosition(position);
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
						
						speed_post_rate=Double.valueOf(calci_Post_Speed.getCalc_post_speed_rate());
						getDataPlan();
						name_sel_post_speed=calci_Post_Speed.getCalc_post_speed_value();
						getPlanName();
					}
				}
			});
	
	}
	
	public void initializeControls(){
		
		seek = (SeekBar) findViewById(R.id.seekBar1);
		seek.incrementProgressBy(3);
		seek.setMax(11);
		seek.setProgress(1);
		seek.setOnSeekBarChangeListener(onSeekBarChangeListener);
		//seek.setEnabled(false);
		tvDays = (TextView) findViewById(R.id.tvDays);
		tvData = (TextView) findViewById(R.id.tvData);
		tvSpeed = (TextView) findViewById(R.id.tvSpeed);
		tvFinalPrice = (TextView) findViewById(R.id.tvFinalPrice);
		tvPlanName = (TextView) findViewById(R.id.tvPlanName);
		tvPostSpeed= (TextView) findViewById(R.id.tvPostSpeed);
		
		Typeface tf = Typeface.createFromAsset(this.getAssets(),
				"digital_7.ttf");
		tvDays.setTypeface(tf);
		tvData.setTypeface(tf);
		tvSpeed.setTypeface(tf);
		tvPostSpeed.setTypeface(tf);
		tvFinalPrice.setTypeface(tf);
		
		tvHelp=(TextView) findViewById(R.id.tvHelp);
		
		Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink_anim);
		tvHelp.startAnimation(animBlink);
		
		btn_30 = (LinearLayout) findViewById(R.id.btn1);
		btn_90 = (LinearLayout) findViewById(R.id.btn2);
		btn_180 = (LinearLayout) findViewById(R.id.btn3);
		btn_365 = (LinearLayout) findViewById(R.id.btn4);
		
		btn_Fup=(Button)findViewById(R.id.btnFup);
		btn_Ul=(Button)findViewById(R.id.btnUnlimited);
		
		ll_data_transfer_box= (LinearLayout) findViewById(R.id.ll_data_transfer_box);
		ll_post_speed_box= (LinearLayout) findViewById(R.id.ll_post_speed_box);
		ll_speed_box= (LinearLayout) findViewById(R.id.ll_speed_box);
		
		ll_Paynow=(LinearLayout) findViewById(R.id.ll_section4_3);
		
		btn_180.setOnClickListener(clickListener);
		btn_30.setOnClickListener(clickListener);
		btn_365.setOnClickListener(clickListener);
		btn_90.setOnClickListener(clickListener);
		btn_Fup.setOnClickListener(clickListener);
		btn_Ul.setOnClickListener(clickListener);
		
		gvSpeed = (GridView) findViewById(R.id.gvSpeed);
		gvData = (GridView) findViewById(R.id.gvData);
		gvPostSpeed=(GridView) findViewById(R.id.gvPostSpeed);
		
		ll_sec4_c = (LinearLayout) findViewById(R.id.ll_section4_1);
		ll_sec4_cut = (LinearLayout) findViewById(R.id.ll_section4_2);
		ll_Paynow=(LinearLayout) findViewById(R.id.ll_section4_3);
		ll_selectPlantType=(LinearLayout) findViewById(R.id.ll_selectplan_type);
		
		ll_sec4_cut.setOnClickListener(section4_clk);
		ll_sec4_c.setOnClickListener(section4_clk);
		ll_Paynow.setOnClickListener(section4_clk);
		showInstructions();
	}
	
	OnSeekBarChangeListener onSeekBarChangeListener= new OnSeekBarChangeListener() {
		
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
			
			if(current_type.length()==0){
				Crouton.makeText(Make_my_plan_Activity.this,"1. Please select Plan Type FUP or Unlimited." , Utils.style).show();
			}
			if (fromUser) {
				if ((progress == 0) || (progress == 1) || (progress == 2)) {
					seek.setProgress(1);
					current_days="30";
					days="30";
					if(current_type.equalsIgnoreCase("fup")){
						current_days="30";
						set_speed_adapter(current_days);
						days="30";
						getPlanName();
						initialsetup();
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					}
					if(current_type.equalsIgnoreCase("ul")){
						days="30";
						current_days="30";
						set_speed_adapter("ul_"+current_days);
						initialsetup();
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
				} 
				if ((progress == 3) || (progress == 4) || (progress == 5)) {
					seek.setProgress(4);
					current_days="90";
					days="90";
					if(current_type.equalsIgnoreCase("fup")){
						current_days="90";
						set_speed_adapter(current_days);						
						initialsetup();
						days="90";
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
					if(current_type.equalsIgnoreCase("ul")){
						days="90";
						current_days="90";
						set_speed_adapter("ul_"+current_days);
						initialsetup();
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
				}
				if ((progress == 6) || (progress == 7) || (progress == 8)) {
					seek.setProgress(7);
					current_days="180";
					days="180";
					if(current_type.equalsIgnoreCase("fup")){
						current_days="180";
						set_speed_adapter(current_days);
						
						initialsetup();
						days="180";
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
					if(current_type.equalsIgnoreCase("ul")){
						current_days="180";
						days="180";
						set_speed_adapter("ul_"+current_days);
						initialsetup();
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
				}
				if ((progress == 9) || (progress == 10) || (progress == 11)) {
					seek.setProgress(10);
					current_days="365";
					days="365";
					if(current_type.equalsIgnoreCase("fup")){
						current_days="365";
						set_speed_adapter(current_days);
						initialsetup();
						days="365";
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
					if(current_type.equalsIgnoreCase("ul")){
						current_days="365";
						days="365";
						set_speed_adapter("ul_"+current_days);
						initialsetup();
						name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
						getPlanName();
						
					}
				}
			}
		
		}
	};

	public OnClickListener clickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==btn_30){
				seek.setProgress(1);
				current_days="30";
				days="30";
				if(current_type.equalsIgnoreCase("fup")){
					current_days="30";
					set_speed_adapter(current_days);
					initialsetup();
					days="30";
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
					
				}
				if(current_type.equalsIgnoreCase("ul")){
					current_days="30";
					days="30";
					set_speed_adapter("ul_"+current_days);
					initialsetup();
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
				}
			}
			if(v==btn_90){
				seek.setProgress(4);
				current_days="90";
				days="90";
				if(current_type.equalsIgnoreCase("fup")){
					current_days="90";
					set_speed_adapter(current_days);
					initialsetup();
					days="90";
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
					
				}
				if(current_type.equalsIgnoreCase("ul")){
					current_days="90";
					days="90";
					set_speed_adapter("ul_"+current_days);
					initialsetup();
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
				}
			}
			if(v==btn_180){
				seek.setProgress(7);
				current_days="180";
				days="180";
				if(current_type.equalsIgnoreCase("fup")){
					current_days="180";
					set_speed_adapter(current_days);
					initialsetup();
					days="180";
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
					
				}
				if(current_type.equalsIgnoreCase("ul")){
					current_days="180";
					days="180";
					set_speed_adapter("ul_"+current_days);
					initialsetup();
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
				}
			}
			if(v==btn_365){
				seek.setProgress(10);
				current_days="365";
				days="365";
				if(current_type.equalsIgnoreCase("fup")){
					current_days="365";
					set_speed_adapter(current_days);
					initialsetup();
					days="365";
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
										
				}
				if(current_type.equalsIgnoreCase("ul")){
					current_days="365";
					days="365";
					set_speed_adapter("ul_"+current_days);
					initialsetup();
					name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
					getPlanName();
				}
			}
			if(v==btn_Fup){
				 

				flag_plan_type=true;
				flag_days=true;
				showInstructions();
				if(current_type.length()>0){
					if(current_type.equalsIgnoreCase("ul")){
						resetFlags();
						getPlanName();
						btn_Fup.setBackgroundResource(R.drawable.fup_selected);
						btn_Ul.setBackgroundResource(R.drawable.ul);
						current_type="fup";
						
						 	LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
						    lp.weight =(float) 0.5;
						    ll_speed_box.setLayoutParams(lp);
						    ll_data_transfer_box.setVisibility(View.VISIBLE);
							ll_post_speed_box.setVisibility(View.VISIBLE);
							flag_days=true;
							set_speed_adapter(current_days);
							initialsetup();
							getDataPlan();
							seek.setEnabled(true);
							getPlanName();
					}
				}
				else{
					resetFlags();
					current_type="fup";
					btn_Fup.setBackgroundResource(R.drawable.fup_selected);
					btn_Ul.setBackgroundResource(R.drawable.ul);
					
					 	LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
					    lp.weight =(float) 0.5;
					    ll_speed_box.setLayoutParams(lp);
					    ll_data_transfer_box.setVisibility(View.VISIBLE);
						ll_post_speed_box.setVisibility(View.VISIBLE);
						flag_days=true;
						set_speed_adapter(current_days);
						initialsetup();
						getDataPlan();
						getPlanName();
						seek.setEnabled(true);
				}
			}
			if(v==btn_Ul){
				flag_plan_type=true;
				flag_days=true;
				showInstructions();
				if(current_type.length()>0){
					if(current_type.equalsIgnoreCase("fup")){
						resetFlags();
						getPlanName();
						btn_Fup.setBackgroundResource(R.drawable.fup);
						btn_Ul.setBackgroundResource(R.drawable.ul_selected);
						current_type="ul";
						
						 LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
						    lp.weight = 1;
						    ll_speed_box.setLayoutParams(lp);
						    ll_data_transfer_box.setVisibility(View.GONE);
						    ll_post_speed_box.setVisibility(View.GONE);
						    flag_days=true;
						    set_speed_adapter("ul_"+current_days);
							initialsetup();
							getDataPlan();
							getPlanName();
							seek.setEnabled(true);
					}
				}
				else{
					resetFlags();
					current_type="ul";
					btn_Fup.setBackgroundResource(R.drawable.fup);
					btn_Ul.setBackgroundResource(R.drawable.ul_selected);
					
					 LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
					    lp.weight = 1;
					    ll_speed_box.setLayoutParams(lp);
					    ll_data_transfer_box.setVisibility(View.GONE);
					    ll_post_speed_box.setVisibility(View.GONE);
					    flag_days=true;
					    set_speed_adapter("ul_"+current_days);
						initialsetup();
						getDataPlan();	getPlanName();
						seek.setEnabled(true);
				}
			}

		}
	};
	
	public OnClickListener section4_clk= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==ll_sec4_cut){
				if(current_type.equalsIgnoreCase("fup")){
					if(flag_speed_post){
						flag_speed_post=false;
						set_post_speed_adapter(calci_Speed_Global);
						showInstructions();
						name_sel_post_speed="";
						getPlanName();
					}
					else{
						if(flag_data_limit){
							flag_data_limit=false;
							set_data_limit_adapter(calci_Speed_Global);
							showInstructions();
							name_sel_data_limit="";
							getPlanName();
						}
						else{
							if(flag_speed){
								flag_speed=false;
								set_speed_adapter(current_days);
								showInstructions();
								name_sel_speed="";
								getPlanName();
							}
						}
					}
					
					getDataPlan();
				}
				
				if(current_type.equalsIgnoreCase("ul")){
					if(flag_speed){
						flag_speed=false;
						set_speed_adapter("ul_"+current_days);
						showInstructions();
						name_sel_speed="";
						getPlanName();
						initialsetup();
					}
				}
			}
			
			if(v==ll_sec4_c){
				if(current_type.equalsIgnoreCase("fup")){
					flag_data_limit=false;flag_speed_post=false;flag_speed=false;
					name_sel_post_speed="";name_sel_data_limit="";name_sel_speed="";
					set_speed_adapter(current_days);					
					showInstructions();
					getPlanName();
					initialsetup();
				}
				if(current_type.equalsIgnoreCase("ul")){
					if(flag_speed){
						flag_speed=false;
						set_speed_adapter("ul_"+current_days);
						showInstructions();
						name_sel_speed="";
						getPlanName();
						initialsetup();
					}
				}
			}
			
			if(v==ll_Paynow){
				if(getFinalPackagerate()){
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						 new GetFinalDeductedAmtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
					 else
						 new GetFinalDeductedAmtAsyncTask().execute();
				}
			}
		}
	};
	
	public class GetPlanDetailsAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		
		ProgressHUD mProgressHUD;
		GetPlanParameterSOAP getPlanParameterSOAP;
		String getPlanResult,getPlanResponse;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(Make_my_plan_Activity.this,getString(R.string.app_please_wait_label),true,true,this);
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
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
				mProgressHUD.dismiss();
			if(getPlanResult.length()>0){
				if(getPlanResult.equalsIgnoreCase("ok")){
					if(getPlanResponse.length()>0){
						Utils.log("Response",":"+getPlanResponse);
						parse_plan_details(getPlanResponse);
					}
				}
				else{
					if(Make_my_plan_Activity.this.isRestricted()){
					}
					else
						AlertsBoxFactory.showAlert(getPlanResult, Make_my_plan_Activity.this);
				}
			}
			else{
				if(Make_my_plan_Activity.this.isRestricted()){
				}
				else
					AlertsBoxFactory.showAlert(getPlanResult, Make_my_plan_Activity.this);
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

	public void parse_plan_details(String json){
		try {
		if(json!=null){
			
				JSONObject json_main= new JSONObject(json);
				if(json_main.has("NewDataSet")){
					if(json_main.get("NewDataSet") instanceof JSONObject){
						JSONObject json_newdataset= json_main.getJSONObject("NewDataSet");
						parse_json_30_days(json_newdataset);
						parse_json_90_days(json_newdataset);
						parse_json_180_days(json_newdataset);
						parse_json_365_days(json_newdataset);
						
						parse_json_ul_30_days(json_newdataset);
						parse_json_ul_90_days(json_newdataset);
						parse_json_ul_180_days(json_newdataset);
						parse_json_ul_365_days(json_newdataset);
						
						
						
						try {
							calci_Days.setHm_calci_speeds(hs_calci_days);
							InternalStorage.writeObject(Make_my_plan_Activity.this, KEY, calci_Days);
							SharedPreferences.Editor edit= sharedPreferences.edit();
							edit.putBoolean("local_plan_check", true);
							edit.commit();
							
							 try {
									calci_Days=(Calci_Days)InternalStorage.readObject(Make_my_plan_Activity.this, KEY);
									
									if(calci_Days!=null){
										hs_calci_days=calci_Days.getHm_calci_speeds();
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
					}
				}
			
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse_json_30_days(JSONObject json_newdataset){
		try{
		if(json_newdataset.has("Days1")){

			ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
			if(json_newdataset.get("Days1") instanceof JSONObject){
				
				Calci_Speed calci_Speed= new Calci_Speed();
				JSONObject json_days_30= json_newdataset.getJSONObject("Days1");
				
				String str_speed_rates=json_days_30.optString("BasicRate1","0.0");
				if(Double.valueOf(str_speed_rates)>0){
			
					String str_speed_values=json_days_30.optString("Speed1");
				
					String arr_data_limit_values=json_days_30.optString("DataLimit1");
					String arr_data_limit_rate=json_days_30.optString("DataLimitAmount1");
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int i=0;i<data_limit_values.length;i++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[i];
								String data_rate=data_limit_rates[i];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_30.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_30.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
				}
			}
			
			if(json_newdataset.get("Days1") instanceof JSONArray){
				JSONArray json_array_days_30=json_newdataset.getJSONArray("Days1");
				for(int i=0;i<json_array_days_30.length();i++){
					
					Calci_Speed calci_Speed= new Calci_Speed();
					JSONObject json_days_30= json_array_days_30.getJSONObject(i);
					String str_speed_rates=json_days_30.optString("BasicRate1","0.0");
					if(Double.valueOf(str_speed_rates)>0){
					
					String str_speed_values=json_days_30.optString("Speed1");	
					
					String arr_data_limit_values=json_days_30.optString("DataLimit1");
					String arr_data_limit_rate=json_days_30.optString("DataLimitAmount1");
					
					Utils.log("All Data"," Value:"+arr_data_limit_values);
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int j=0;j<data_limit_values.length;j++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[j];
								String data_rate=data_limit_rates[j];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								Utils.log("Data"," Value:"+data_value);
								Utils.log("Data"," Rate:"+data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_30.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_30.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
					}
				}
				
			}
			
			if(alCalci_Speeds!=null){
				if(alCalci_Speeds.size()>0){
					hs_calci_days.put("30", alCalci_Speeds);									
				}
			}
		}
	
		} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
		}
	}

	public void parse_json_90_days(JSONObject json_newdataset){
		try{
		if(json_newdataset.has("Days2")){

			ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
			if(json_newdataset.get("Days2") instanceof JSONObject){
				
				Calci_Speed calci_Speed= new Calci_Speed();
				JSONObject json_days_90= json_newdataset.getJSONObject("Days2");
				
				String str_speed_rates=json_days_90.optString("BasicRate1","0.0");
				if(Double.valueOf(str_speed_rates)>0){
			
					String str_speed_values=json_days_90.optString("Speed1");
				
					String arr_data_limit_values=json_days_90.optString("DataLimit1");
					String arr_data_limit_rate=json_days_90.optString("DataLimitAmount1");
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int i=0;i<data_limit_values.length;i++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[i];
								String data_rate=data_limit_rates[i];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_90.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_90.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
				}
			}
			
			if(json_newdataset.get("Days2") instanceof JSONArray){
				JSONArray json_array_days_90=json_newdataset.getJSONArray("Days2");
				for(int i=0;i<json_array_days_90.length();i++){
					
					Calci_Speed calci_Speed= new Calci_Speed();
					JSONObject json_days_90= json_array_days_90.getJSONObject(i);
					String str_speed_rates=json_days_90.optString("BasicRate1","0.0");
					if(Double.valueOf(str_speed_rates)>0){
					
					String str_speed_values=json_days_90.optString("Speed1");	
					
					String arr_data_limit_values=json_days_90.optString("DataLimit1");
					String arr_data_limit_rate=json_days_90.optString("DataLimitAmount1");
					
					Utils.log("All Data"," Value:"+arr_data_limit_values);
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int j=0;j<data_limit_values.length;j++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[j];
								String data_rate=data_limit_rates[j];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								Utils.log("Data"," Value:"+data_value);
								Utils.log("Data"," Rate:"+data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_90.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_90.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
					}
				}
				
			}
			
			if(alCalci_Speeds!=null){
				if(alCalci_Speeds.size()>0){
					hs_calci_days.put("90", alCalci_Speeds);									
				}
			}
		}
	
		} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
		}
	}
	
	public void parse_json_180_days(JSONObject json_newdataset){
		try{
		if(json_newdataset.has("Days3")){

			ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
			if(json_newdataset.get("Days3") instanceof JSONObject){
				
				Calci_Speed calci_Speed= new Calci_Speed();
				JSONObject json_days_180= json_newdataset.getJSONObject("Days3");
				
				String str_speed_rates=json_days_180.optString("BasicRate1","0.0");
				if(Double.valueOf(str_speed_rates)>0){
			
					String str_speed_values=json_days_180.optString("Speed1");
				
					String arr_data_limit_values=json_days_180.optString("DataLimit1");
					String arr_data_limit_rate=json_days_180.optString("DataLimitAmount1");
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int i=0;i<data_limit_values.length;i++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[i];
								String data_rate=data_limit_rates[i];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_180.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_180.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
				}
			}
			
			if(json_newdataset.get("Days3") instanceof JSONArray){
				JSONArray json_array_days_180=json_newdataset.getJSONArray("Days3");
				for(int i=0;i<json_array_days_180.length();i++){
					
					Calci_Speed calci_Speed= new Calci_Speed();
					JSONObject json_days_180= json_array_days_180.getJSONObject(i);
					String str_speed_rates=json_days_180.optString("BasicRate1","0.0");
					if(Double.valueOf(str_speed_rates)>0){
					
					String str_speed_values=json_days_180.optString("Speed1");	
					
					String arr_data_limit_values=json_days_180.optString("DataLimit1");
					String arr_data_limit_rate=json_days_180.optString("DataLimitAmount1");
					
					Utils.log("All Data"," Value:"+arr_data_limit_values);
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int j=0;j<data_limit_values.length;j++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[j];
								String data_rate=data_limit_rates[j];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								Utils.log("Data"," Value:"+data_value);
								Utils.log("Data"," Rate:"+data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_180.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_180.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
					}
				}
				
			}
			
			if(alCalci_Speeds!=null){
				if(alCalci_Speeds.size()>0){
					hs_calci_days.put("180", alCalci_Speeds);									
				}
			}
		}
	
		} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
		}
	}
	
	public void parse_json_365_days(JSONObject json_newdataset){
		try{
		if(json_newdataset.has("Days4")){

			ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
			if(json_newdataset.get("Days4") instanceof JSONObject){
				
				Calci_Speed calci_Speed= new Calci_Speed();
				JSONObject json_days_365= json_newdataset.getJSONObject("Days4");
				
				String str_speed_rates=json_days_365.optString("BasicRate1","0.0");
				if(Double.valueOf(str_speed_rates)>0){
			
					String str_speed_values=json_days_365.optString("Speed1");
				
					String arr_data_limit_values=json_days_365.optString("DataLimit1");
					String arr_data_limit_rate=json_days_365.optString("DataLimitAmount1");
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int i=0;i<data_limit_values.length;i++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[i];
								String data_rate=data_limit_rates[i];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_365.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_365.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
				}
			}
			
			if(json_newdataset.get("Days4") instanceof JSONArray){
				JSONArray json_array_days_365=json_newdataset.getJSONArray("Days4");
				for(int i=0;i<json_array_days_365.length();i++){
					
					Calci_Speed calci_Speed= new Calci_Speed();
					JSONObject json_days_365= json_array_days_365.getJSONObject(i);
					String str_speed_rates=json_days_365.optString("BasicRate1","0.0");
					if(Double.valueOf(str_speed_rates)>0){
					
					String str_speed_values=json_days_365.optString("Speed1");	
					
					String arr_data_limit_values=json_days_365.optString("DataLimit1");
					String arr_data_limit_rate=json_days_365.optString("DataLimitAmount1");
					
					Utils.log("All Data"," Value:"+arr_data_limit_values);
					
					if(arr_data_limit_values.length()>0){
						ArrayList<Calci_Data_Limit> alCalci_Data_Limits= new ArrayList<Calci_Data_Limit>();
						if(arr_data_limit_values.contains(",")){
							String[] data_limit_values=	arr_data_limit_values.split(",");
							String[] data_limit_rates=	arr_data_limit_rate.split(",");
							
							for(int j=0;j<data_limit_values.length;j++){
								
								Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
								String data_value=data_limit_values[j];
								String data_rate=data_limit_rates[j];
								calci_Data_Limit.setCalc_data_limit_value(data_value);
								calci_Data_Limit.setCalc_data_limit_rate(data_rate);
								Utils.log("Data"," Value:"+data_value);
								Utils.log("Data"," Rate:"+data_rate);
								alCalci_Data_Limits.add(calci_Data_Limit);
							}
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						else{
							Calci_Data_Limit calci_Data_Limit= new Calci_Data_Limit();
							calci_Data_Limit.setCalc_data_limit_value(arr_data_limit_values);
							calci_Data_Limit.setCalc_data_limit_rate(arr_data_limit_rate);
							alCalci_Data_Limits.add(calci_Data_Limit);
							calci_Speed.setAlCalci_Data_Limits(alCalci_Data_Limits);
						}
						
					}
					
					String arr_post_speed_values=json_days_365.optString("PostSpeed1");
					String arr_post_speed_rate=json_days_365.optString("PostRateAmount1");
					
					Utils.log("All Post Speed"," Value:"+arr_post_speed_values);
					
					if(arr_post_speed_values.length()>0){
						ArrayList<Calci_Post_Speed> alCalci_Post_Speeds= new  ArrayList<Calci_Post_Speed>();
						
						if(arr_post_speed_values.contains(",")){
							String[] post_speed_values=	arr_post_speed_values.split(",");
							String[] post_speed_rates=	arr_post_speed_rate.split(",");
							
							for(int j=0;j<post_speed_values.length;j++){
								
								Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
								String post_speed_value=post_speed_values[j];
								String post_speed_rate=post_speed_rates[j];
								calci_Post_Speed.setCalc_post_speed_value(post_speed_value);
								calci_Post_Speed.setCalc_post_speed_rate(post_speed_rate);
								Utils.log("Post Speed"," Value:"+post_speed_value);
								Utils.log("Post Speed"," Rate:"+post_speed_rate);
								alCalci_Post_Speeds.add(calci_Post_Speed);
							}
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}						
						else{
							Calci_Post_Speed calci_Post_Speed= new Calci_Post_Speed();
							calci_Post_Speed.setCalc_post_speed_value(arr_post_speed_values);
							calci_Post_Speed.setCalc_post_speed_rate(arr_post_speed_rate);
							alCalci_Post_Speeds.add(calci_Post_Speed);
							calci_Speed.setAlCalci_Post_Speeds(alCalci_Post_Speeds);
						}
					}
				
					calci_Speed.setCalc_speed_rate(str_speed_rates);
					calci_Speed.setCalc_speed_value(str_speed_values);
					alCalci_Speeds.add(calci_Speed);
					}
				}
				
			}
			
			if(alCalci_Speeds!=null){
				if(alCalci_Speeds.size()>0){
					hs_calci_days.put("365", alCalci_Speeds);									
				}
			}
		}
	
		} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
		}
	}
	
	public void parse_json_ul_30_days(JSONObject json_newdataset){
		try{
			if(json_newdataset.has("ULDays1")){
				ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
				if(json_newdataset.get("ULDays1") instanceof JSONObject){
					
					JSONObject json_days_ul_30= json_newdataset.getJSONObject("ULDays1");
					String arr_speed_rate=json_days_ul_30.getString("BasicRate1");
					String arr_speed_value=json_days_ul_30.getString("Speed1");
					
					if(arr_speed_rate.length()>0){
						if(arr_speed_rate.contains(",")){
							String[] local_speed_rate=arr_speed_rate.split(",");
							String[] local_speed_value=arr_speed_value.split(",");
							for(int i=0;i<local_speed_rate.length;i++){
								Calci_Speed calci_Speed= new Calci_Speed();
								calci_Speed.setCalc_speed_rate(local_speed_rate[i]);
								calci_Speed.setCalc_speed_value(local_speed_value[i]);
								alCalci_Speeds.add(calci_Speed);
							}
							
						}
						else{
							
						}
					}
				}
				
				if(alCalci_Speeds!=null){
					if(alCalci_Speeds.size()>0){
						hs_calci_days.put("ul_30", alCalci_Speeds);									
					}
				}
			}
		}
		catch(JSONException e){
			
		}
	}
	
	public void parse_json_ul_90_days(JSONObject json_newdataset){
		try{
			if(json_newdataset.has("ULDays2")){
				ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
				if(json_newdataset.get("ULDays2") instanceof JSONObject){
					
					JSONObject json_days_ul_30= json_newdataset.getJSONObject("ULDays2");
					String arr_speed_rate=json_days_ul_30.getString("BasicRate2");
					String arr_speed_value=json_days_ul_30.getString("Speed2");
					
					if(arr_speed_rate.length()>0){
						if(arr_speed_rate.contains(",")){
							String[] local_speed_rate=arr_speed_rate.split(",");
							String[] local_speed_value=arr_speed_value.split(",");
							for(int i=0;i<local_speed_rate.length;i++){
								Calci_Speed calci_Speed= new Calci_Speed();
								calci_Speed.setCalc_speed_rate(local_speed_rate[i]);
								calci_Speed.setCalc_speed_value(local_speed_value[i]);
								alCalci_Speeds.add(calci_Speed);
							}
							
						}
						else{
							
						}
					}
				}
				
				if(alCalci_Speeds!=null){
					if(alCalci_Speeds.size()>0){
						hs_calci_days.put("ul_90", alCalci_Speeds);									
					}
				}
			}
		}
		catch(JSONException e){
			
		}
	}
	
	public void parse_json_ul_180_days(JSONObject json_newdataset){
		try{
			if(json_newdataset.has("ULDays3")){
				ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
				if(json_newdataset.get("ULDays3") instanceof JSONObject){
					
					JSONObject json_days_ul_30= json_newdataset.getJSONObject("ULDays3");
					String arr_speed_rate=json_days_ul_30.getString("BasicRate3");
					String arr_speed_value=json_days_ul_30.getString("Speed3");
					
					if(arr_speed_rate.length()>0){
						if(arr_speed_rate.contains(",")){
							String[] local_speed_rate=arr_speed_rate.split(",");
							String[] local_speed_value=arr_speed_value.split(",");
							for(int i=0;i<local_speed_rate.length;i++){
								Calci_Speed calci_Speed= new Calci_Speed();
								calci_Speed.setCalc_speed_rate(local_speed_rate[i]);
								calci_Speed.setCalc_speed_value(local_speed_value[i]);
								alCalci_Speeds.add(calci_Speed);
							}
							
						}
						else{
							
						}
					}
				}
				
				if(alCalci_Speeds!=null){
					if(alCalci_Speeds.size()>0){
						hs_calci_days.put("ul_180", alCalci_Speeds);									
					}
				}
			}
		}
		catch(JSONException e){
			
		}
	}
	
	public void parse_json_ul_365_days(JSONObject json_newdataset){
		try{
			if(json_newdataset.has("ULDays4")){
				ArrayList<Calci_Speed> alCalci_Speeds= new ArrayList<Calci_Speed>();
				if(json_newdataset.get("ULDays4") instanceof JSONObject){
					
					JSONObject json_days_ul_30= json_newdataset.getJSONObject("ULDays4");
					String arr_speed_rate=json_days_ul_30.getString("BasicRate4");
					String arr_speed_value=json_days_ul_30.getString("Speed4");
					
					if(arr_speed_rate.length()>0){
						if(arr_speed_rate.contains(",")){
							String[] local_speed_rate=arr_speed_rate.split(",");
							String[] local_speed_value=arr_speed_value.split(",");
							for(int i=0;i<local_speed_rate.length;i++){
								Calci_Speed calci_Speed= new Calci_Speed();
								calci_Speed.setCalc_speed_rate(local_speed_rate[i]);
								calci_Speed.setCalc_speed_value(local_speed_value[i]);
								alCalci_Speeds.add(calci_Speed);
							}
							
						}
						else{
							
						}
					}
				}
				
				if(alCalci_Speeds!=null){
					if(alCalci_Speeds.size()>0){
						hs_calci_days.put("ul_365", alCalci_Speeds);									
					}
				}
			}
		}
		catch(JSONException e){
			
		}
	}
	
	public void set_speed_adapter(String key){
		flag_speed=false;
		alCalci_Speeds_Global=hs_calci_days.get(key);
		gvData.setVisibility(View.GONE);
		gvPostSpeed.setVisibility(View.GONE);
		if(alCalci_Speeds_Global!=null){
			if(alCalci_Speeds_Global.size()>0){
				
			speed_Adapter= new Calci_Speed_Adapter(Make_my_plan_Activity.this,R.layout.gv_data_row_item, alCalci_Speeds_Global, "");
				gvSpeed.setAdapter(speed_Adapter);
				gvSpeed.setVisibility(View.VISIBLE);
				if (alCalci_Speeds_Global.size() < 6) {
					gvSpeed.setNumColumns(2);
				} else {
					gvSpeed.setNumColumns(3);
					if (alCalci_Speeds_Global.size() > 9) {
						gvSpeed.setNumColumns(4);
					}
				}
			}
			else{
				flag_speed=false;
				calci_Speed_Global=null;
				gvSpeed.setVisibility(View.GONE);
				gvData.setVisibility(View.GONE);
				gvPostSpeed.setVisibility(View.GONE);
			}
			
		}
		else{
			flag_speed=false;
			calci_Speed_Global=null;
			gvSpeed.setVisibility(View.GONE);
			gvData.setVisibility(View.GONE);
			gvPostSpeed.setVisibility(View.GONE);
		}
	}
	
	public void set_data_limit_adapter(Calci_Speed calci_Speed){
	
		if(calci_Speed!=null){
			alData_Limits_Global=calci_Speed.getAlCalci_Data_Limits();
			if(alData_Limits_Global!=null){
				if(alData_Limits_Global.size()>0){
					data_Limit_Adapter= new Calci_Data_Limit_Adapter(Make_my_plan_Activity.this,R.layout.gv_data_row_item,alData_Limits_Global, "");
					gvData.setAdapter(data_Limit_Adapter);
					gvData.setVisibility(View.VISIBLE);
					
					if (alData_Limits_Global.size() < 6) {
						gvData.setNumColumns(2);
					} else {
						gvData.setNumColumns(3);
						if (alData_Limits_Global.size() > 9) {
							gvData.setNumColumns(4);
						}
					}
				}
				else{
					gvData.setVisibility(View.GONE);
				}
			}
			else{
				gvData.setVisibility(View.GONE);
			}
		}
		else{
			gvData.setVisibility(View.GONE);
		}
	}
		
	public void set_post_speed_adapter(Calci_Speed calci_Speed){
		
		if(calci_Speed!=null){
			alPost_Speeds_Global=calci_Speed.getAlCalci_Post_Speeds();
			if(alPost_Speeds_Global!=null){
				if(alPost_Speeds_Global.size()>0){
					post_Speed_Adapter= new Calci_Post_Speed_Adapter(Make_my_plan_Activity.this,R.layout.gv_row_item,alPost_Speeds_Global,"speed");
					gvPostSpeed.setAdapter(post_Speed_Adapter);
					gvPostSpeed.setVisibility(View.VISIBLE);
					if (alPost_Speeds_Global.size() < 6) {
						gvPostSpeed.setNumColumns(2);
					} else {
						gvPostSpeed.setNumColumns(3);
						if (alPost_Speeds_Global.size() > 9) {
							gvPostSpeed.setNumColumns(4);
						}
					}
				}
				else{
					gvPostSpeed.setVisibility(View.GONE);
				}
			}
			else{
				gvPostSpeed.setVisibility(View.GONE);
			}
		}
		else{
			gvPostSpeed.setVisibility(View.GONE);
		}
	}

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

	public void initialsetup(){
		
		speed_rate=0.0;data_limit_rate=0.0;speed_post_rate=0.0;final_package_rate=0.0;
		flag_speed=false;flag_data_limit=false;flag_speed_post=false;
		tvDays.setVisibility(View.GONE);
		tvData.setVisibility(View.GONE);
		tvSpeed.setVisibility(View.GONE);
		tvPostSpeed.setVisibility(View.GONE);
		tvFinalPrice.setVisibility(View.GONE);
		showInstructions();
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
		if(flag_data_limit&&flag_speed){
			final_package_rate=speed_rate+data_limit_rate+speed_post_rate;
			tvFinalPrice.setVisibility(View.VISIBLE);
			tvFinalPrice.setText("="+final_package_rate.intValue());
		}
		else{
			tvFinalPrice.setVisibility(View.GONE);
			
		}
		
		if(!flag_speed_post){
			tvFinalPrice.setVisibility(View.GONE);
			speed_post_rate=0.0;
			tvDays.setText(""+speed_rate.intValue()+"+");
			tvData.setText(""+data_limit_rate.intValue()+"+");
			tvPostSpeed.setText(""+speed_post_rate.intValue());
		}
		if(!flag_data_limit){
			tvFinalPrice.setVisibility(View.GONE);
			data_limit_rate=0.0;
			tvDays.setText(""+speed_rate.intValue()+"+");
			tvData.setText(""+data_limit_rate.intValue()+"+");
			tvPostSpeed.setText(""+speed_post_rate.intValue());
		}
		if(!flag_speed){
			tvFinalPrice.setVisibility(View.GONE);
			speed_rate=0.0;
			tvDays.setText(""+speed_rate.intValue()+"+");
			tvData.setText(""+data_limit_rate.intValue()+"+");
			tvPostSpeed.setText(""+speed_post_rate.intValue());
		}
		}
		if(current_type.equalsIgnoreCase("ul")){
			tvDays.setVisibility(View.VISIBLE);
			final_package_rate=speed_rate;
			tvFinalPrice.setVisibility(View.GONE);
			tvDays.setText(""+final_package_rate.intValue());
		}
	
	}

	public void resetFlags(){
		flag_days=false;flag_speed=false;flag_data_limit=false;flag_speed_post=false;
		name_sel_data_limit="";name_sel_post_speed="";name_sel_speed="";
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
	
	public boolean getFinalPackagerate(){
		if(current_type.equalsIgnoreCase("fup")){
			if(flag_plan_type&&flag_speed&&flag_speed_post){
				if(speed_rate>0&&data_limit_rate>0&&speed_post_rate>0){
					final_package_rate=speed_rate+data_limit_rate+speed_post_rate;
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		
		if(current_type.equalsIgnoreCase("ul")){
			if(flag_speed){
				if(speed_rate>0){
					final_package_rate=speed_rate;
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		return false;
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
			mProgressHUD=ProgressHUD.show(Make_my_plan_Activity.this, getString(R.string.app_please_wait_label), true, false, this);
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
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
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
					AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), Make_my_plan_Activity.this);
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

	public void showFinalDialog(){
		final Dialog dialog = new Dialog(Make_my_plan_Activity.this);
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
			AlertsBoxFactory.showAlert(getString(R.string.fail_response), Make_my_plan_Activity.this);
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
					AlertsBoxFactory.showAlert("This Feature will <b>Discontinue</b> The Current Package and New Package will be Applied from Today.<br/> Are you Sure? ", Make_my_plan_Activity.this);
					
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
				if(Utils.isOnline(Make_my_plan_Activity.this)){
					checkValue="creditdebit";
					//new GetMemberDetailWebService().execute();
					
					if(!compulsory_immediate){
							if(updateFrom.length()>0){
								show_pg_dialog(checkValue);
							}else{
								Toast.makeText(Make_my_plan_Activity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
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
				if(Utils.isOnline(Make_my_plan_Activity.this)){
					
					checkValue="netbanking";
					//new GetMemberDetailWebService().execute();
					if(!compulsory_immediate){
						if(updateFrom.length()>0){
							show_pg_dialog(checkValue);
						}
						else{
							Toast.makeText(Make_my_plan_Activity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
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
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
		
	private class AdjustmentWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		ProgressHUD mProgressHUD;

		String adjustmentResult="",adjustmentResponse="";

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(Make_my_plan_Activity.this,getString(R.string.app_please_wait_label), true,true, this);
			
		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
		
			
		}
		protected void onPostExecute(Void unused) {
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
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
						AlertsBoxFactory.showAlert("Conversion is not possible.", Make_my_plan_Activity.this);
					}else{
						rb_immediate.setChecked(false);
						updateFrom="";
						AlertsBoxFactory.showAlert(adjustmentResponse, Make_my_plan_Activity.this);
					}
				}

			} else {
				rb_immediate.setChecked(false);
				updateFrom="";
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ adjustmentResult, Make_my_plan_Activity.this);
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
	
	public void show_pg_dialog(String check) {
		pg_dialog = new Dialog(Make_my_plan_Activity.this);
		pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pg_dialog.setContentView(R.layout.dialog_payment_gateway);
		
		int width = 0;
		int height =0;
		
		
		    Point size = new Point();
		    WindowManager w =((Activity)Make_my_plan_Activity.this).getWindowManager();

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
					SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
					int citrus=sharedPreferences1.getInt("citrus", 1);
					if(citrus>0){
						proceed_to_pay();
					}
					else{
						AlertsBoxFactory.showAlert(Utils.CITRUS_MESSAGE, Make_my_plan_Activity.this);
					}
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
					AlertsBoxFactory.showAlert(Utils.CCAvenue_Message, Make_my_plan_Activity.this);
				}
				
				}

				
			});
		    
		    pg_dialog.show();
		    pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		    pg_dialog.getWindow().setLayout((width/2)+(width/2), LayoutParams.WRAP_CONTENT);	
	}

	private void proceed_to_pay() {
				// TODO Auto-generated method stub
				if(compulsory_immediate){
					Intent i;
					if(Utils.is_CCAvenue)
						i = new Intent(Make_my_plan_Activity.this, MakeMyPayments_CCAvenue.class);
					else
					 i = new Intent(Make_my_plan_Activity.this, MakeMyPayments.class);
					i.putExtra("PackageName", tvPlanName.getText().toString());
					i.putExtra("PackageAmount", tvDialogPrice.getText().toString());
					i.putExtra("PackageValidity", days);
					i.putExtra("updateFrom", "I");
					i.putExtra("ServiceTax", "0");
					i.putExtra("datafrom", datafrom);
					i.putExtra("ClassName", Make_my_plan_Activity.this.getClass().getSimpleName());
					i.putExtra("addtional_amount", additionalAmount);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				}
				else
				{
					if(updateFrom.length()>0){
						//PlanCalculatorActivity.this.finish();	
						Intent i;
						if(Utils.is_CCAvenue)
							i = new Intent(Make_my_plan_Activity.this, MakeMyPayments_CCAvenue.class);
						else
						 i = new Intent(Make_my_plan_Activity.this, MakeMyPayments.class);
						i.putExtra("PackageName", tvPlanName.getText().toString());
						i.putExtra("PackageAmount", tvDialogPrice.getText().toString());
						i.putExtra("PackageValidity", days);
						i.putExtra("updateFrom", updateFrom);
						i.putExtra("ServiceTax", "0");
						i.putExtra("datafrom", datafrom);
						i.putExtra("ClassName", Make_my_plan_Activity.this.getClass().getSimpleName());
						i.putExtra("addtional_amount", additionalAmount);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						}
					else{
						Toast.makeText(Make_my_plan_Activity.this, "Please Select Package Update Details ", Toast.LENGTH_LONG).show();						
					}
			}
			}
		
	public class GetCalciVersion extends AsyncTask<String, Void, Void> implements OnCancelListener{
		ProgressHUD mProgressHUD;
		String calc_version_result="",calc_version_response="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(Make_my_plan_Activity.this,getString(R.string.app_please_wait_label), true,false, this);
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
				calc_version_result="error";
				Utils.log("Error", "is"+calc_version_result);
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
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
			mProgressHUD.dismiss();
			if(calc_version_result.length()>0){
				Utils.log("Result",":"+calc_version_result);
				if(calc_version_result.equalsIgnoreCase("ok")){
					Utils.log("Version",":"+calc_version_response);
					
					String version_response[]=calc_version_response.split("#");
					if(version_response!=null){
						if(version_response.length>2&&version_response.length>0){
								if(version_response[2].equalsIgnoreCase("0")){
									showEndDialog("This feature is not available.");
								}
								else{
		
								if(version_response.length>1){
									is_version_checked=true;
									
									if(version_response[0].equalsIgnoreCase(CalciVersion)){
										SharedPreferences.Editor edit=sharedPreferences.edit();
										edit.putString("Calc_Version", version_response[0]);
										edit.commit();
										 if(sharedPreferences.getBoolean("local_plan_check", false)){
											try {
												calci_Days=(Calci_Days)InternalStorage.readObject(Make_my_plan_Activity.this, KEY);
												hs_calci_days=calci_Days.getHm_calci_speeds();
											} catch (ClassNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										 }
										 else{
											
												edit.putString("Calc_Version", version_response[0]);
												edit.commit();
												 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
													 new GetPlanDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
												 }
												 else{
													 new GetPlanDetailsAsyncTask().execute();
												 }
										 }
										
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
				else{
					if(calc_version_result.equalsIgnoreCase("error")){
						iError.display();
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

	public void showEndDialog(String Message){
		final Dialog dialog = new Dialog(Make_my_plan_Activity.this);
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
				Make_my_plan_Activity.this.finish();
				/*Intent i = new Intent(PlanCalculatorActivity,IONHome.class);
				startActivity(i);*/
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		dialog.show();
		//(width/2)+((width/2)/2)
		//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		 
			 this.finish();
			 overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
		
	}
	
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		if(Make_my_plan_Activity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			Make_my_plan_Activity.this.finish();
		}
		
	}
	
}
