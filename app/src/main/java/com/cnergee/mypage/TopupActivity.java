package com.cnergee.mypage;

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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.GetAllDetailsFor_TopupSOAP;
import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.GetPlanFor_TopupSOAP;
import com.cnergee.mypage.adapter.CustomArrayAdapter;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import all.interface_.IDaysSelecttion_Topup;
import all.interface_.IError;
import cnergee.plan.calc.DataLimit;
import cnergee.plan.calc.DayRate_Plan;
import cnergee.plan.calc.Days;
import cnergee.plan.calc.Speed;
import cnergee.krishna.ookla.odigitel.R;

public class TopupActivity extends BaseActivity implements IDaysSelecttion_Topup{
String MemberloginName="";
Utils utils = new Utils();
SharedPreferences sharedPreferences;
LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
String plan_type="",days_alloted="";
ProgressHUD mProgressHUD;
ListView lvDaysNumber;
GridView gvSpeed,gvDataLimit;
GridSpeedAdapter gridSpeedAdapter;
GridDataLimit gridDataLimit;
HashMap<String, DayRate_Plan> hash_days= new HashMap<String, DayRate_Plan>();
Days days= new Days();
ArrayList<String> alNumberOfDays= new ArrayList<String>();
CustomArrayAdapter customArrayAdapter;
TextView tv_Popup_price;
Double final_price=0.0,speed_price=0.0,data_limit_price=0.0;
LinearLayout ll_Price,ll_Speed,ll_Data,ll_pay_now;
TextView tvDialogPrice;
String speed_selected="",data_selected="";
String selected_speed_data="";
String day="";
String number_day="";
TextView tv_valid_label,tv_speed_label,tv_data_label;
int firstVisiblePosition,totalVisibleCount;
Dialog pg_dialog;
String MemberId="";
public Double Topup_Discount=0.0,Discounted_final_price=0.0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topup);
		 	sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
		 	utils.setSharedPreferences(sharedPreferences);
			MemberloginName = utils.getMemberLoginID();
			MemberId=utils.getMemberId();
			iError=(IError)this;
			
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			 }
			    else{
			     new GetCalciVersion().execute();
			  }
			
			//plan_type="LIM";
			//days_alloted="7";
			//new GetAllDetails_For_TopUp().execute();
		
		    linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
			linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
			linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
			linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
			tv_Popup_price=(TextView) findViewById(R.id.tv_popup_price);
			ll_Price=(LinearLayout) findViewById(R.id.ll_price);
			ll_Speed=(LinearLayout) findViewById(R.id.ll_speed);
			ll_Data=(LinearLayout) findViewById(R.id.ll_data);
		//	tv_note=(TextView) findViewById(R.id.tv_note);
			ll_pay_now=(LinearLayout) findViewById(R.id.ll_pay_now);
		//	tv_note.setText("Note:The selection will add boost to your current plan");
			tv_valid_label=(TextView) findViewById(R.id.tv_valid_label);
			tv_speed_label=(TextView) findViewById(R.id.tv_speed_label);
			tv_data_label=(TextView) findViewById(R.id.tv_data_label);
			Typeface tf = Typeface.createFromAsset(this.getAssets(),
					"digital_7.ttf");
			tv_valid_label.setTypeface(tf);
			tv_speed_label.setTypeface(tf);
			tv_data_label.setTypeface(tf);
			lvDaysNumber=(ListView) findViewById(R.id.lvDays);
			gvSpeed=(GridView) findViewById(R.id.gvSpeed);
			gvDataLimit=(GridView)findViewById(R.id.gvDataLimit);
			customArrayAdapter= new CustomArrayAdapter(this, R.layout.days_row_item, alNumberOfDays);
			lvDaysNumber.setAdapter(customArrayAdapter);
			
			
			BaseApplication.getEventBus().register(this);
			ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
			ivMenuDrawer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
				}
			});
			
			
			//**************listview Click Listener******************************************
			
			/*lvDaysNumber.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					//lvDaysNumber.invalidateViews();
					number_day=arg0.getItemAtPosition(arg2).toString();
					speed_price=0.0;
					speed_selected="";
					data_limit_price=0.0;
					data_selected="";
					setAdapters(number_day);
					
					for(int i=0;i<alNumberOfDays.size();i++){
						if(i==arg2){
							View v1 = arg0.getChildAt(arg2);
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_button) ;
							if(number_day.equalsIgnoreCase("1"))
							c.setBackgroundResource(R.drawable.tp_single_day_button_selected);
							else
							c.setBackgroundResource(R.drawable.tp_days_button_selected);
						}
						else{
							View v1 = arg0.getChildAt(i);
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_button) ;
							if(number_day.equalsIgnoreCase("1"))
								c.setBackgroundResource(R.drawable.tp_single_day_button);
								else
								c.setBackgroundResource(R.drawable.tp_days_button);
							if(alNumberOfDays.get(i).equalsIgnoreCase("1"))
								c.setBackgroundResource(R.drawable.tp_single_day_button);
						}
					}
					
					
					
					for(int visiblePosition = firstVisiblePosition; visiblePosition <=totalVisibleCount ; visiblePosition++) {
						Utils.log("Visible","Position is"+visiblePosition);		
						   if(visiblePosition==arg2){
							   View v1 = arg0.getChildAt(arg2);
							   
							   Utils.log("Clicked","Position is"+arg2);	
							   if(v1!=null){
								LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_button);
								if(number_day.equalsIgnoreCase("1"))
								c.setBackgroundResource(R.drawable.tp_single_day_button_selected);
								else
								c.setBackgroundResource(R.drawable.tp_days_button_selected);
							   }
							}
							else{
								View v1 = arg0.getChildAt(visiblePosition);
								 Utils.log("Clicked","Position is"+arg2);	
								 if(v1!=null){
									
								LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_button) ;
								if(number_day.equalsIgnoreCase("1"))
									c.setBackgroundResource(R.drawable.tp_single_day_button);
									else
									c.setBackgroundResource(R.drawable.tp_days_button);
								if(alNumberOfDays.get(visiblePosition).equalsIgnoreCase("1"))
									c.setBackgroundResource(R.drawable.tp_single_day_button);
								 }
							}
						   // make something
						}
				}
				
			});*/
			
			//**************listview Click Listener******************************************
			
			  lvDaysNumber.setOnScrollListener(new OnScrollListener()
			    {           
			        @Override
			        public void onScrollStateChanged(AbsListView view, int scrollState) {}

			        @Override
			        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			        {
			            Log.i("MMY: ", "Scroll");
			            Log.i("MMY: ", "First visible item: " + String.valueOf(firstVisibleItem));
			            Log.i("MMY: ", "Visible item count: " + String.valueOf(visibleItemCount));

			            // The variables that belong to this class now equal ->
			            
			            
			            totalVisibleCount= visibleItemCount;
			            firstVisiblePosition= firstVisibleItem;

			           
			        }
			    });
			
			linnhome.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TopupActivity.this.finish();
					//Intent i = new Intent(TopupActivity.this,IONHome.class);
					//startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				}
			});
			
			linnprofile.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TopupActivity.this.finish();
					Intent i = new Intent(TopupActivity.this,Profile.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				}
			});
			
			linnnotification.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TopupActivity.this.finish();
					Intent i = new Intent(TopupActivity.this,NotificationListActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				}
			});
			
			linnhelp.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TopupActivity.this.finish();
					Intent i = new Intent(TopupActivity.this,HelpActivity.class);
					startActivity(i);
					BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				
				}
			});
			
			gvSpeed.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Speed speed= (Speed)arg0.getItemAtPosition(arg2);
					
					
					tv_Popup_price.setText(getString(R.string.rupees_sign)+":"+speed.getSpeed_Rate());
					speed_selected=speed.getSpeed_Value();
					speed_price=speed.getSpeed_Rate();
					getFinalPrice();
					ll_Price.setVisibility(View.VISIBLE);
					ll_pay_now.setVisibility(View.VISIBLE);
					/*for(int i=0;i<gridSpeedAdapter.getCount();i++){
						if(i==arg2){
							View v1 = arg0.getChildAt(arg2);
							
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
							c.setBackgroundResource(R.drawable.top_up_button_selected);
						}
						else{
							View v1 = arg0.getChildAt(i);
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
							c.setBackgroundResource(R.drawable.top_up_button);
						}
					}*/
					
					for(int visiblePosition = gvSpeed.getFirstVisiblePosition(); visiblePosition <= gvSpeed.getLastVisiblePosition(); visiblePosition++) {
						  
						   if(visiblePosition==arg2){
							   View view = gvSpeed.getChildAt(visiblePosition);
							   if(view!=null){
								LinearLayout c=(LinearLayout)view.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.top_up_button_selected);
							   }
							}
							else{
								
								 View view = gvSpeed.getChildAt(visiblePosition);
								  if(view!=null){
								LinearLayout c=(LinearLayout)view.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.top_up_button);
								  }
							}
						   // make something
						}
					
				}
			});
			
			gvDataLimit.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					DataLimit dataLimit= (DataLimit)arg0.getItemAtPosition(arg2);
					data_selected=dataLimit.getData_Gb_Value();
					
					tv_Popup_price.setText(getString(R.string.rupees_sign)+":"+dataLimit.getData_Gb_Rate()*(Integer.valueOf(data_selected)));
					
					data_limit_price=dataLimit.getData_Gb_Rate()*(Integer.valueOf(data_selected));
					getFinalPrice();
					ll_Price.setVisibility(View.VISIBLE);
					ll_pay_now.setVisibility(View.VISIBLE);
					
					/*for(int i=0;i<gridDataLimit.getCount();i++){
						if(i==arg2){
							View v1 = arg0.getChildAt(arg2);
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
							c.setBackgroundResource(R.drawable.top_up_button_selected);
						}
						else{
							View v1 = arg0.getChildAt(i);
							LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_gv) ;
							c.setBackgroundResource(R.drawable.top_up_button);
						}
					}*/
					
					for(int visiblePosition = gvDataLimit.getFirstVisiblePosition(); visiblePosition <= gvDataLimit.getLastVisiblePosition(); visiblePosition++) {
						  
						   if(visiblePosition==arg2){
							   View view = gvDataLimit.getChildAt(visiblePosition);	
							   if(view!=null){
								LinearLayout c=(LinearLayout)view.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.top_up_button_selected);
							   }
							}
							else{
								 View view = gvDataLimit.getChildAt(visiblePosition);
								 if(view!=null){
								LinearLayout c=(LinearLayout)view.findViewById(R.id.ll_gv) ;
								c.setBackgroundResource(R.drawable.top_up_button);
								 }
							}
						   // make something
						}
					
				}
			});
			
			ll_pay_now.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*if(speed_selected.length()>0){
						if(data_selected.length()>0)
							showFinalDialog();	
						else
							Toast.makeText(TopupActivity.this, "Please Select Data ", Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(TopupActivity.this, "Please Select Speed ", Toast.LENGTH_LONG).show();
					}*/
					
					if(plan_type.equalsIgnoreCase("LIM")){
						if(speed_selected.length()>0){
						if(data_selected.length()>0)
							showFinalDialog();	
						else
							Toast.makeText(TopupActivity.this, "Please Select Data ", Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(TopupActivity.this, "Please Select Speed ", Toast.LENGTH_LONG).show();
					}
					}
					if(plan_type.equalsIgnoreCase("ULM")){
						showFinalDialog();	
					}
					
					if(plan_type.equalsIgnoreCase("FUP")){
						showFinalDialog();	
					}
				}
			});
	}

	
	public class GetTopUp_PlanDetailsAsyncTask extends AsyncTask<String	, Void, Void> implements OnCancelListener{
	
		String getTopupPlanDetailsResult="",getTopupPlanDetailsResponse="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub	
			super.onPreExecute();
			mProgressHUD=ProgressHUD.show(TopupActivity.this, getString(R.string.app_please_wait_label), true, false, this);
			
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetPlanFor_TopupSOAP getPlanFor_TopupSOAP= new GetPlanFor_TopupSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_PLANDETAILS_FOR_TOPUP));
			try {
				getTopupPlanDetailsResult=getPlanFor_TopupSOAP.getPlanDeatilsFor_Topup(MemberloginName);
				getTopupPlanDetailsResponse=getPlanFor_TopupSOAP.getResponse();
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				getTopupPlanDetailsResult="Internet is too slow";
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				getTopupPlanDetailsResult="Internet is too slow";
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getTopupPlanDetailsResult="Please try Again";
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
			if(getTopupPlanDetailsResult.length()>0){
				if(getTopupPlanDetailsResult.equalsIgnoreCase("ok")){
					Utils.log("Response","is:"+getTopupPlanDetailsResponse);
					if(getTopupPlanDetailsResponse.length()>0){
						if(getTopupPlanDetailsResponse.contains("#")){
							String[] response_send=getTopupPlanDetailsResponse.split("#");
								plan_type=response_send[0];
								days_alloted=response_send[1];
								//plan_type="LIM";
								//days_alloted="4";
								new GetAllDetails_For_TopUp().execute();
							}
						else{
							mProgressHUD.dismiss();
							showEndDialog(getTopupPlanDetailsResponse);
						}
							
						}
						else{
							mProgressHUD.dismiss();
							showEndDialog(getTopupPlanDetailsResponse);		
						}
					}
				else{
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert(getTopupPlanDetailsResult, TopupActivity.this);
				}
				}
			else{
				mProgressHUD.dismiss();
				AlertsBoxFactory.showAlert(getTopupPlanDetailsResult, TopupActivity.this);
			}
			}
			catch(Exception e){
				mProgressHUD.dismiss();
				Utils.log("","");
			}
			}
		
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
		
	}
	
	public class GetAllDetails_For_TopUp extends AsyncTask<String, Void, Void>{
		String get_all_details_result="";
		String get_all_details_response="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetAllDetailsFor_TopupSOAP getAllDetailsFor_TopupSOAP= new GetAllDetailsFor_TopupSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ALLDETAILS_FOR_TOPUP));
			try {
				get_all_details_result=	getAllDetailsFor_TopupSOAP.getPlanDeatilsFor_Topup(plan_type, days_alloted,MemberloginName);
				get_all_details_response=getAllDetailsFor_TopupSOAP.getResponse();
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
			//mProgressHUD.dismiss();
			if(get_all_details_result.length()>0){
				if(get_all_details_result.equalsIgnoreCase("ok")){
					if(get_all_details_response.length()>0){
						Utils.log("All Plan Details",":"+get_all_details_response);
						parseJsonData(get_all_details_response);
					}
					
				}
			}
		}
		
	}
	
	public void parseJsonData(String json){
		if (json != null) {
			if (json.length() > 0) {
				try {
					JSONObject mainJson = new JSONObject(json);
					JSONObject NewDataSetJson = mainJson
							.getJSONObject("NewDataSet");
					
//STARTS HERE************UNLIMITED DATA*****************UNLIMITED DATA*****************UNLIMITED DATA***************UNLIMITED DATA***********UNLIMITED DATA 					
					
					if(NewDataSetJson.has("UnlimitedData")){
						ll_Data.setVisibility(View.GONE);
						if(NewDataSetJson.get("UnlimitedData") instanceof JSONObject){
							
							ArrayList<Speed>alSpeeds= new ArrayList<Speed>();
							DayRate_Plan dayRate_Plan= new DayRate_Plan();
							
							JSONObject ulJsonObj=NewDataSetJson.getJSONObject("UnlimitedData");
							String speed_value=ulJsonObj.getString("SpeedLimit");
							String speed_rate=ulJsonObj.getString("Charges");
							Topup_Discount=ulJsonObj.optDouble("Discount", 0.0);
							String Day=ulJsonObj.getString("Day");
							Utils.log("Newdataset",":"+NewDataSetJson.toString());
						
							if(speed_value!=null){
								if(speed_value.length()>0){
									if(speed_value.contains(",")){
										String[] arr_speed_value=speed_value.split(",");
										String[] arr_speed_rate=speed_rate.split(",");
										for(int j=0;j<arr_speed_value.length;j++){
											Speed speed= new Speed();
											speed.setSpeed_Value(arr_speed_value[j]);
											speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[j]));
											alSpeeds.add(speed);
										}
									}
									else{
										Speed speed= new Speed();
										speed.setSpeed_Value(speed_value);
										speed.setSpeed_Rate(Double.valueOf(speed_rate));
										alSpeeds.add(speed);
									}
									
									dayRate_Plan.setAlSpeeds(alSpeeds);
									hash_days.put(Day, dayRate_Plan);
								}
								
							}
							Utils.log("Hashmap Unlimited","size:"+hash_days.size());
									
						}
						if(NewDataSetJson.get("UnlimitedData") instanceof JSONArray){
							JSONArray ulJsonArray=NewDataSetJson.getJSONArray("UnlimitedData");
							for(int i=0;i<ulJsonArray.length();i++){
								ArrayList<Speed>alSpeeds= new ArrayList<Speed>();
								DayRate_Plan dayRate_Plan= new DayRate_Plan();
								
								JSONObject arr_data_json=ulJsonArray.getJSONObject(i);
								String speed_value=arr_data_json.getString("SpeedLimit");
								String speed_rate=arr_data_json.getString("Charges");
								Topup_Discount=arr_data_json.optDouble("Discount", 0.0);
								String Day=arr_data_json.getString("Day");
								
								Utils.log("UnlimitedData",":"+arr_data_json.toString());
								
								if(speed_value!=null){
									if(speed_value.length()>0){
										if(speed_value.contains(",")){
											String[] arr_speed_value=speed_value.split(",");
											String[] arr_speed_rate=speed_rate.split(",");
											for(int j=0;j<arr_speed_value.length;j++){
												Speed speed= new Speed();
												speed.setSpeed_Value(arr_speed_value[j]);
												speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[j]));
												alSpeeds.add(speed);
											}
										}
										else{
											Speed speed= new Speed();
											speed.setSpeed_Value(speed_value);
											speed.setSpeed_Rate(Double.valueOf(speed_rate));
											alSpeeds.add(speed);
										}
										
										dayRate_Plan.setAlSpeeds(alSpeeds);
										hash_days.put(Day, dayRate_Plan);
									}
									
								}
							}
							Utils.log("Hashmap Unlimited","size:"+hash_days.size());
						}
					}
//ENDS HERE************UNLIMITED DATA*****************UNLIMITED DATA*****************UNLIMITED DATA***************UNLIMITED DATA***********UNLIMITED DATA					

				
//STARTS HERE************FUP DATA*****************FUP DATA*****************FUP DATA***************FUP DATA***********FUP DATA**************************** 					
					
					if(NewDataSetJson.has("FupData")){
						ll_Speed.setVisibility(View.GONE);
						if(NewDataSetJson.get("FupData") instanceof JSONObject){
							
							ArrayList<DataLimit>alDataLimits= new ArrayList<DataLimit>();
							
							DayRate_Plan dayRate_Plan= new DayRate_Plan();
							
							JSONObject ulJsonObj=NewDataSetJson.getJSONObject("FupData");
							String data_limit_value=ulJsonObj.getString("DataLimit");
							String data_limit_rate=ulJsonObj.getString("Charges");
							String Day=ulJsonObj.getString("Day");
							Topup_Discount=ulJsonObj.optDouble("Discount", 0.0);
							Utils.log("Newdataset",":"+NewDataSetJson.toString());
						
							if(data_limit_value!=null){
								if(data_limit_value.length()>0){
									if(data_limit_value.contains(",")){
										String[] arr_data_limit_value=data_limit_value.split(",");
										String[] arr_data_limit_rate=data_limit_rate.split(",");
										for(int j=0;j<arr_data_limit_value.length;j++){
											DataLimit dataLimit= new DataLimit();
											dataLimit.setData_Gb_Value(arr_data_limit_value[j]);
											dataLimit.setData_Gb_Rate(Double.valueOf(arr_data_limit_rate[j]));
											alDataLimits.add(dataLimit);
										}
									}
									else{
										DataLimit dataLimit= new DataLimit();
										dataLimit.setData_Gb_Value(data_limit_value);
										dataLimit.setData_Gb_Rate(Double.valueOf(data_limit_rate));
										alDataLimits.add(dataLimit);
									}
									
									dayRate_Plan.setAlDataLimits(alDataLimits);
									hash_days.put(Day, dayRate_Plan);
								}
								
							}
							Utils.log("Hashmap FUP","size:"+hash_days.size());
									
						}
						if(NewDataSetJson.get("FupData") instanceof JSONArray){
							JSONArray ulJsonArray=NewDataSetJson.getJSONArray("FupData");
							for(int i=0;i<ulJsonArray.length();i++){
								ArrayList<DataLimit>alDataLimits= new ArrayList<DataLimit>();
								DayRate_Plan dayRate_Plan= new DayRate_Plan();
								
								JSONObject arr_data_json=ulJsonArray.getJSONObject(i);
								String datalimit_value=arr_data_json.getString("DataLimit");
								String datalimit_rate=arr_data_json.getString("Charges");
								String Day=arr_data_json.getString("Day");
								Topup_Discount=arr_data_json.optDouble("Discount", 0.0);
								
								Utils.log("FUPdData",":"+arr_data_json.toString());
								
								if(datalimit_value!=null){
									if(datalimit_value.length()>0){
										if(datalimit_value.contains(",")){
											String[] arr_datalimit_value=datalimit_value.split(",");
											String[] arr_datalimit_rate=datalimit_rate.split(",");
											for(int j=0;j<arr_datalimit_value.length;j++){
												DataLimit datalimit= new DataLimit();
												datalimit.setData_Gb_Value(arr_datalimit_value[j]);
												datalimit.setData_Gb_Rate(Double.valueOf(arr_datalimit_rate[j]));
												alDataLimits.add(datalimit);
											}
										}
										else{
											DataLimit datalimit= new DataLimit();
											datalimit.setData_Gb_Value(datalimit_value);
											datalimit.setData_Gb_Rate(Double.valueOf(datalimit_rate));
											alDataLimits.add(datalimit);
										}
										
										dayRate_Plan.setAlDataLimits(alDataLimits);
										hash_days.put(Day, dayRate_Plan);
									}
									
								}
							}
							Utils.log("Hashmap FUP","size:"+hash_days.size());
						}
					}
//ENDS HERE************FUP DATA*****************FUP DATA*****************FUP DATA***************FUP DATA***********FUP DATA********************************
					

//STARTS HERE************LIMITED DATA*****************LIMITED DATA*****************LIMITED DATA***************LIMITED DATA***********LIMITED DATA**************************** 					
					
					if(NewDataSetJson.has("LimitedData")){
						if(NewDataSetJson.get("LimitedData") instanceof JSONObject){
							
							ArrayList<DataLimit>alDataLimits= new ArrayList<DataLimit>();
							ArrayList<Speed>alSpeeds= new ArrayList<Speed>();
							DayRate_Plan dayRate_Plan= new DayRate_Plan();
							
							JSONObject ulJsonObj=NewDataSetJson.getJSONObject("LimitedData");
							String data_limit_value=ulJsonObj.getString("DataLimit");
							String data_limit_rate=ulJsonObj.getString("DataCharges");
							String speed_value=ulJsonObj.getString("SpeedLimit");
							String speed_rate=ulJsonObj.getString("SpeedCharges");
							Topup_Discount=ulJsonObj.optDouble("Discount", 0.0);
							String Day=ulJsonObj.getString("Day");
							Utils.log("Newdataset",":"+NewDataSetJson.toString());
						
							if(data_limit_value!=null){
								if(data_limit_value.length()>0){
									if(data_limit_value.contains(",")){
										String[] arr_data_limit_value=data_limit_value.split(",");
										String[] arr_data_limit_rate=data_limit_rate.split(",");
										for(int j=0;j<arr_data_limit_value.length;j++){
											DataLimit dataLimit= new DataLimit();
											dataLimit.setData_Gb_Value(arr_data_limit_value[j]);
											dataLimit.setData_Gb_Rate(Double.valueOf(arr_data_limit_rate[j]));
											alDataLimits.add(dataLimit);
										}
									}
									else{
										DataLimit dataLimit= new DataLimit();
										dataLimit.setData_Gb_Value(data_limit_value);
										dataLimit.setData_Gb_Rate(Double.valueOf(data_limit_rate));
										alDataLimits.add(dataLimit);
									}
									
									dayRate_Plan.setAlDataLimits(alDataLimits);
									hash_days.put(Day, dayRate_Plan);
								}
								
							}
							
							if(speed_value!=null){
								if(speed_value.length()>0){
									if(speed_value.contains(",")){
										String[] arr_speed_value=speed_value.split(",");
										String[] arr_speed_rate=speed_rate.split(",");
										for(int j=0;j<arr_speed_value.length;j++){
											Speed speed= new Speed();
											speed.setSpeed_Value(arr_speed_value[j]);
											speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[j]));
											alSpeeds.add(speed);
										}
									}
									else{
										Speed speed= new Speed();
										speed.setSpeed_Value(speed_value);
										speed.setSpeed_Rate(Double.valueOf(speed_rate));
										alSpeeds.add(speed);
									}
									
									dayRate_Plan.setAlSpeeds(alSpeeds);
									hash_days.put(Day, dayRate_Plan);
								}
								
							}
							Utils.log("Hashmap LimitedData","size:"+hash_days.size());
									
						}
						if(NewDataSetJson.get("LimitedData") instanceof JSONArray){
							JSONArray ulJsonArray=NewDataSetJson.getJSONArray("LimitedData");
							for(int i=0;i<ulJsonArray.length();i++){
								ArrayList<DataLimit>alDataLimits= new ArrayList<DataLimit>();
								ArrayList<Speed>alSpeeds= new ArrayList<Speed>();
								DayRate_Plan dayRate_Plan= new DayRate_Plan();
								
								JSONObject arr_data_json=ulJsonArray.getJSONObject(i);
								String datalimit_value=arr_data_json.getString("DataLimit");
								String datalimit_rate=arr_data_json.getString("DataCharges");
								String speed_value=arr_data_json.getString("SpeedLimit");
								String speed_rate=arr_data_json.getString("SpeedCharges");
								String Day=arr_data_json.getString("Day");
								Topup_Discount=arr_data_json.optDouble("Discount", 0.0);
								Utils.log("LimitedData",":"+arr_data_json.toString());
								
								if(datalimit_value!=null){
									if(datalimit_value.length()>0){
										if(datalimit_value.contains(",")){
											String[] arr_datalimit_value=datalimit_value.split(",");
											String[] arr_datalimit_rate=datalimit_rate.split(",");
											for(int j=0;j<arr_datalimit_value.length;j++){
												DataLimit datalimit= new DataLimit();
												datalimit.setData_Gb_Value(arr_datalimit_value[j]);
												datalimit.setData_Gb_Rate(Double.valueOf(arr_datalimit_rate[j]));
												alDataLimits.add(datalimit);
											}
										}
										else{
											DataLimit datalimit= new DataLimit();
											datalimit.setData_Gb_Value(datalimit_value);
											datalimit.setData_Gb_Rate(Double.valueOf(datalimit_rate));
											alDataLimits.add(datalimit);
										}
										
										dayRate_Plan.setAlDataLimits(alDataLimits);
										hash_days.put(Day, dayRate_Plan);
									}
									
								}
								
								
								if(speed_value!=null){
									if(speed_value.length()>0){
										if(speed_value.contains(",")){
											String[] arr_speed_value=speed_value.split(",");
											String[] arr_speed_rate=speed_rate.split(",");
											for(int j=0;j<arr_speed_value.length;j++){
												Speed speed= new Speed();
												speed.setSpeed_Value(arr_speed_value[j]);
												speed.setSpeed_Rate(Double.valueOf(arr_speed_rate[j]));
												alSpeeds.add(speed);
											}
										}
										else{
											Speed speed= new Speed();
											speed.setSpeed_Value(speed_value);
											speed.setSpeed_Rate(Double.valueOf(speed_rate));
											alSpeeds.add(speed);
										}
										
										dayRate_Plan.setAlSpeeds(alSpeeds);
										hash_days.put(Day, dayRate_Plan);
									}
									
								}
							}
							Utils.log("Hashmap LIMITED","size:"+hash_days.size());
						}
					}
//ENDS HERE************LIMITED DATA*****************LIMITED DATA*****************LIMITED DATA***************LIMITED DATA***********LIMITED DATA********************************
					Utils.log("Number of","Days:"+days_alloted);
					for(int i=1;i<=Integer.valueOf(days_alloted);i++){
						alNumberOfDays.add(""+i);
					}
					customArrayAdapter.notifyDataSetChanged();
					lvDaysNumber.setAdapter(customArrayAdapter);
					
					
				} catch (Exception e) {
					Utils.log("Json Exception",":"+e);
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		
		TopupActivity.this.finish();
		//Intent i = new Intent(TopupActivity.this,IONHome.class);
		//startActivity(i);
		overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}
	
	
	public void showEndDialog(String Message){
		final Dialog dialog = new Dialog(TopupActivity.this);
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
				TopupActivity.this.finish();
				Intent i = new Intent(TopupActivity.this,IONHome.class);
				startActivity(i);
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
	
	public void setAdapters(String days){
		if(hash_days!=null){
			DayRate_Plan dayRate_Plan= hash_days.get(days);
			final_price=0.0;
			speed_price=0.0;
			data_limit_price=0.0;
			ll_Price.setVisibility(View.GONE);
			ll_pay_now.setVisibility(View.GONE);
			if(dayRate_Plan!=null){
				if(dayRate_Plan.getAlSpeeds()!=null){
					ll_Speed.setVisibility(View.VISIBLE);
					gridSpeedAdapter= new GridSpeedAdapter(TopupActivity.this, R.layout.topup_item,
							dayRate_Plan.getAlSpeeds(), "");
					gvSpeed.setAdapter(gridSpeedAdapter);
					gridSpeedAdapter.notifyDataSetChanged();
				}
				else{
					ll_Speed.setVisibility(View.GONE);
				}
				
				if(dayRate_Plan.getAlDataLimits()!=null){
					ll_Data.setVisibility(View.VISIBLE);
					gridDataLimit= new GridDataLimit(TopupActivity.this, R.layout.topup_item,
							dayRate_Plan.getAlDataLimits(), "");
					gvDataLimit.setAdapter(gridDataLimit);
					gridDataLimit.notifyDataSetChanged();
				}
				else{
					ll_Data.setVisibility(View.GONE);
				}
			}
			else{
				ll_Speed.setVisibility(View.GONE);
				ll_Data.setVisibility(View.GONE);
			}
		}
	}
	
	public void getFinalPrice(){
		final_price=speed_price+data_limit_price;
		if(final_price>0.0){
			
			tv_Popup_price.setText(getString(R.string.rupees_sign)+" "+final_price);
		}
		else{
			tv_Popup_price.setText(getString(R.string.rupees_sign));
		}
	}
	
	public void showFinalDialog(){
		final Dialog dialog = new Dialog(TopupActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.dialog_topup);
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
		    selected_speed_data	="ION "+(speed_selected.length()>0?speed_selected+"M":"")+(data_selected.length()>0?data_selected+"G":"")+((number_day+"D-TU"));
		
		TextView tvDialogPlanName=(TextView) dialog.findViewById(R.id.tvDialogPlanName);
		TextView TextView02=(TextView)dialog.findViewById(R.id.TextView02);
		TextView tvPlanLabelPrice=(TextView)dialog.findViewById(R.id.tvPlanLabelPrice);
		tvDialogPrice	=(TextView) dialog.findViewById(R.id.tvDialogPlanPrice);
		RelativeLayout rl_Netbanking=(RelativeLayout)dialog.findViewById(R.id.rl_dialog_netbanking);
		RelativeLayout rl_DebitCredit=(RelativeLayout)dialog.findViewById(R.id.rl_dialog_creditdebit);
		RelativeLayout rl_og_price=(RelativeLayout)dialog.findViewById(R.id.rl_og_price);
		TextView tv_og_price=(TextView)dialog.findViewById(R.id.tvOgDialogPlanPrice);
		TextView tv_discountLabel=(TextView)dialog.findViewById(R.id.tvDiscountLabel);
		final RadioGroup rg_check=(RadioGroup)dialog.findViewById(R.id.rg_dialog);
		RadioButton rb_immediate=(RadioButton)dialog.findViewById(R.id.rbimmediate);
		RadioButton rb_next=(RadioButton)dialog.findViewById(R.id.rbnext);
		tvDialogPrice.setText(String.valueOf(final_price));
		Discounted_final_price=final_price;
		
		if(Topup_Discount>0){
			tv_discountLabel.setText("Avail of a "+Topup_Discount+"% discount by paying online.");
			rl_og_price.setVisibility(View.VISIBLE);
			tv_og_price.setText(String.valueOf(final_price));
			Double Discount_price=(final_price*Topup_Discount)/100;
			tvPlanLabelPrice.setText(getString(R.string.plan_price));
			Discounted_final_price=final_price-Discount_price;
			Discounted_final_price=	Double.valueOf(Math.round(Discounted_final_price));
			tvDialogPrice.setText(String.valueOf(Discounted_final_price));
			
		}
		else{
			tv_discountLabel.setVisibility(View.GONE);
			rl_og_price.setVisibility(View.GONE);
			tvPlanLabelPrice.setText(getString(R.string.rupees_sign));
		}
		
		rb_immediate.setVisibility(View.GONE);
		TextView02.setVisibility(View.GONE);
		rb_next.setVisibility(View.GONE);
		
		tvDialogPlanName.setText(selected_speed_data);
	
		
		rl_DebitCredit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.isOnline(TopupActivity.this)){
				String	checkValue="creditdebit";
				
				show_pg_dialog(checkValue);
			
				}
			}
		});
		
		rl_Netbanking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.isOnline(TopupActivity.this)){
					String checkValue="netbanking";
					show_pg_dialog(checkValue);
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
	
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		Utils.log(""+TopupActivity.this.getClass().getSimpleName(),"finish");
		//Utils.log(""+TopupActivity.this.getClass().getSimpleName(),"::"+Utils.Last_Class_Name);
		if(TopupActivity.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			TopupActivity.this.finish();
		}
		
	}

	@Override
	public void Days_Selected(String s) {
		// TODO Auto-generated method stub
		number_day=s;
		speed_price=0.0;
		speed_selected="";
		data_limit_price=0.0;
		data_selected="";
		setAdapters(number_day);
	}
	
	public void show_pg_dialog(String check) {
		pg_dialog = new Dialog(TopupActivity.this);
		pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pg_dialog.setContentView(R.layout.dialog_payment_gateway);
		
		int width = 0;
		int height =0;
		
		
		    Point size = new Point();
		    WindowManager w =((Activity)TopupActivity.this).getWindowManager();

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
						AlertsBoxFactory.showAlert(Utils.CITRUS_MESSAGE, TopupActivity.this);
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
					AlertsBoxFactory.showAlert(Utils.CCAvenue_Message, TopupActivity.this);
				}
				}

				
			});
		    
		    pg_dialog.show();
		    pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    pg_dialog.getWindow().setLayout((width/2)+(width/2), LayoutParams.WRAP_CONTENT);	
	}
	
	private void proceed_to_pay() {
		// TODO Auto-generated method stub
		Intent i;
		if(Utils.is_CCAvenue)
			i = new Intent(TopupActivity.this, MakeMyPaymentsTopUp_CCAvenue.class);
		else
			i = new Intent(TopupActivity.this, MakeMyPaymentsTopUp.class);	
		i.putExtra("PackageName", selected_speed_data);
		i.putExtra("PackageAmount", String.valueOf(Discounted_final_price));
		i.putExtra("PackageValidity", number_day);
		i.putExtra("updateFrom", "I");
		i.putExtra("ServiceTax", "0.0");
		i.putExtra("datafrom", "changepack");
		i.putExtra("speed", speed_selected);
		i.putExtra("data", data_selected);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
		/*dialog.dismiss();*/
	}
	
	public class GetCalciVersion extends AsyncTask<String, Void, Void> implements OnCancelListener{
		ProgressHUD mProgressHUD;
		String calc_version_result="",calc_version_response="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(TopupActivity.this,getString(R.string.app_please_wait_label), true,false, this);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetCurrentVersionSOAP getCurrentVersionsoap= new GetCurrentVersionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CURRENT_VERSION));
			try {
				calc_version_result=getCurrentVersionsoap.getCurrentVersion("T", "",MemberId);
				calc_version_response=getCurrentVersionsoap.getResponse();
				
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				calc_version_result="error";
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
					if(calc_version_response.equalsIgnoreCase("0")){
						showEndDialog("This feature is not available.");
					}
					else{						
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
							 new GetTopUp_PlanDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						 }
						 else{
						     new GetTopUp_PlanDetailsAsyncTask().execute();
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
}
