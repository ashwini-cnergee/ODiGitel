package com.cnergee.mypage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avenues.lib.adapter.CardAdapter;
import com.avenues.lib.adapter.PayOptAdapter;
import com.avenues.lib.dto.CardTypeDTO;
import com.avenues.lib.dto.PaymentOptionDTO;
import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.Constants;
import com.avenues.lib.utility.ServiceHandler;
import com.avenues.lib.utility.ServiceUtility;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.widgets.HorizontalListView;
import com.cnergee.widgets.MyTextView;
import com.cnergee.widgets.SlidingPanel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cnergee.krishna.ookla.odigitel.R;

public class PaymentOptionSelection_Activity extends Activity{

	private Animation animShow, animHide;
	public boolean is_popup_visible=false;
	
	ArrayList<CardTypeDTO> creditCardList = new ArrayList<CardTypeDTO>();
	ArrayList<CardTypeDTO> debitCardList = new ArrayList<CardTypeDTO>();
	ArrayList<CardTypeDTO> netBankList = new ArrayList<CardTypeDTO>();
	ArrayList<CardTypeDTO> cashCardList = new ArrayList<CardTypeDTO>();
	ArrayList<CardTypeDTO> mobilePaymentList = new ArrayList<CardTypeDTO>();
	ArrayList<CardTypeDTO> optEMIList = new ArrayList<CardTypeDTO>();
	
	ArrayList<CardTypeDTO> selectedCardTypeList = new ArrayList<CardTypeDTO>();
	
	ArrayList<PaymentOptionDTO> payOptionList = new ArrayList<PaymentOptionDTO>();
	
	Map<String,PaymentOptionDTO> payOptions = new LinkedHashMap<String,PaymentOptionDTO>();
	
	
	public static int click_position_opt=0;
	String selectedPaymentOption;
	CardTypeDTO selectedCardType;
	
	PayOptAdapter payOptAdapter;
	
	 HorizontalListView lv_PayOptions; 
	 GridView gvBankName;
	 LinearLayout ll_show_payopt;
	 SlidingPanel popup;
	 LinearLayout btn_close;
	 MemberDetailsObj memberDetailsObj;
	 
	 EditText etMobNumber,etEmail;
	 TextView tvAmount,tvPackageName;
	 ImageView ivBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_options);
		
		
		  initPopup();
	   
	    memberDetailsObj=(MemberDetailsObj)getIntent().getSerializableExtra("MemberDetails");
	    
	
	    if(memberDetailsObj!=null){
	    	tvAmount.setText(getIntent().getStringExtra("Amount"));
	    	etMobNumber.setText(memberDetailsObj.getMobileNo());
	    	etEmail.setText(memberDetailsObj.getEmailId());
	    	tvPackageName.setText(getIntent().getStringExtra("PackageName"));
	    }
	 	payOptions.put("OPTCRDC",new PaymentOptionDTO("OPTCRDC","Credit Cards",R.drawable.credit_card_60));
		payOptions.put("OPTDBCRD",new PaymentOptionDTO("OPTDBCRD","Debit Cards",R.drawable.debit_card_60));
		payOptions.put("OPTNBK",new PaymentOptionDTO("OPTNBK","Net Banking",R.drawable.net_banking_60));
		payOptions.put("OPTCASHC",new PaymentOptionDTO("OPTCASHC","Cash Cards",R.drawable.cash_card_60));
		payOptions.put("OPTMOBP",new PaymentOptionDTO("OPTMOBP","Mobile Payments",R.drawable.mobile_payment_60));
		payOptions.put("OPTEMI",new PaymentOptionDTO("OPTEMI","Credit Card EMI",R.drawable.credit_card_60));
	     
		new GetData().execute();
	     }
	
	
	
	 private void initPopup() {
	    	
		 	popup = (SlidingPanel) findViewById(R.id.popup_window);
	    	lv_PayOptions= (HorizontalListView) findViewById(R.id.lvItems);
	    	gvBankName=(GridView)findViewById(R.id.gvBankName);
	    	etMobNumber=(EditText)findViewById(R.id.etMobileNumber);
	    	etEmail=(EditText)findViewById(R.id.etEmail);
	    	tvAmount=(TextView)findViewById(R.id.tvAmount);
	    	tvPackageName=(TextView)findViewById(R.id.tvPackageName);
	    	ivBack=(ImageView)findViewById(R.id.ivMenuDrawer);
	    	// Hide the popup initially.....
	    	popup.setVisibility(View.GONE);
	    	btn_close=(LinearLayout)findViewById(R.id.btn_close);
	    	animShow = AnimationUtils.loadAnimation( this, R.anim.payopt_show);
	    	animHide = AnimationUtils.loadAnimation( this, R.anim.payopt_hide);
	    	ll_show_payopt=(LinearLayout) findViewById(R.id.btn_show_payopt);
	    	
	    	
	    	ivBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PaymentOptionSelection_Activity.this.finish();
					overridePendingTransition(R.anim.slide_in_left,
		                    R.anim.slide_out_right);
				}
			});
	    
	    	ll_show_payopt.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if(!is_popup_visible){
					popup.setVisibility(View.VISIBLE);
					popup.startAnimation( animShow );
					is_popup_visible=true;
				
					}
					else{
						popup.startAnimation( animHide );
						popup.setVisibility(View.GONE);
						is_popup_visible=false;
					}
					
	        }});
	        
	    	btn_close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popup.startAnimation( animHide );
					popup.setVisibility(View.GONE);
					is_popup_visible=false;
				}
			});
	    	lv_PayOptions.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					 click_position_opt=arg2;
					for(int visiblePosition = lv_PayOptions.getFirstVisiblePosition(); visiblePosition <= lv_PayOptions.getLastVisiblePosition(); visiblePosition++) {
							//View v=getViewByPosition(visiblePosition, lv_PayOptions);
							if(visiblePosition==arg2){
								 View view = lv_PayOptions.getChildAt(visiblePosition);
								 if(view!=null){
									 MyTextView tv=(MyTextView)view.findViewById(R.id.item_value);
									 if(tv!=null)
									 tv.setTextColor(Color.parseColor("#000000"));
								 }
								
							}
							else{
								 View view = lv_PayOptions.getChildAt(visiblePosition);
								 if(view!=null){
									 MyTextView tv=(MyTextView)view.findViewById(R.id.item_value);
									 if(tv!=null)
									 tv.setTextColor(Color.parseColor("#A4A3A3"));
								 }
							}
							
					}
					payOptAdapter.notifyDataSetChanged();
					
					
					PaymentOptionDTO selectedOption = payOptionList.get(arg2);
					if(selectedOption.getPayOptId().equals("OPTCRDC")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)creditCardList.clone();
					}else if(selectedOption.getPayOptId().equals("OPTDBCRD")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)debitCardList.clone();
					}else if(selectedOption.getPayOptId().equals("OPTNBK")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)netBankList.clone();
					}else if(selectedOption.getPayOptId().equals("OPTCASHC")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)cashCardList.clone();
					}else if(selectedOption.getPayOptId().equals("OPTMOBP")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)mobilePaymentList.clone();
					}else if(selectedOption.getPayOptId().equals("OPTEMI")){
						selectedCardTypeList = (ArrayList<CardTypeDTO>)optEMIList.clone();
					}
					
					selectedPaymentOption = selectedOption.getPayOptId();
					
					CardAdapter cardTypeAdapter = new CardAdapter(PaymentOptionSelection_Activity.this, R.layout.card_type_spinner, selectedCardTypeList);
					gvBankName.setAdapter(cardTypeAdapter);
				
				}
			});
	   
	        							
		}
	 
	 /**
		 * Async task class to get json by making HTTP call
		 * */
		private class GetData extends AsyncTask<Void, Void, Void> {
			ProgressDialog pDialog;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Showing progress dialog
				pDialog = new ProgressDialog(PaymentOptionSelection_Activity.this);
				pDialog.setMessage("Please wait...");
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				// Creating service handler class instance
				ServiceHandler sh = new ServiceHandler();

				// Making a request to url and getting response
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(AvenuesParams.COMMAND,"getJsonData"));
				params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE,AvenuesParams.ACCESS_CODE_VALUE));
				params.add(new BasicNameValuePair(AvenuesParams.CURRENCY,AvenuesParams.CURRENCY_VALUE));
				params.add(new BasicNameValuePair(AvenuesParams.AMOUNT,"1.00"));

				String jsonStr = sh.makeServiceCall(Constants.JSON_URL, ServiceHandler.POST, params);

				ServiceUtility.log("Response: ", "> " + jsonStr);

				if (jsonStr != null) {
					try {
						//modifying string as per requirement
						jsonStr = jsonStr.substring(0,jsonStr.length()-1).replace("processData(","");
						JSONArray payOptList = new JSONArray(jsonStr);

						// looping through All Payment Options
						for (int i=0;i<payOptList.length();i++) {
							JSONObject payOpt = payOptList.getJSONObject(i);
							String payOptStr = payOpt.getString("payOpt");
							ServiceUtility.log("Payment Options", ":"+payOptStr);
							try{
								if(payOpt.getString(payOptStr)!=null){
									if(payOptions.containsKey(payOptStr))
									payOptionList.add(payOptions.get(payOptStr));//Add payment option only if it includes any card
									
									JSONArray cardArr = new JSONArray(payOpt.getString(payOptStr));
									for(int j=0;j<cardArr.length();j++){
										JSONObject card = cardArr.getJSONObject(j);
			
										CardTypeDTO cardTypeDTO = new CardTypeDTO();
										cardTypeDTO.setCardName(card.getString("cardName"));
										cardTypeDTO.setCardType(card.getString("cardType"));
										cardTypeDTO.setPayOptType(card.getString("payOptType"));
										cardTypeDTO.setDataAcceptedAt(card.getString("dataAcceptedAt"));
										cardTypeDTO.setStatus(card.getString("status"));
			
										if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTCRDC")){
											creditCardList.add(cardTypeDTO);
										}else if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTDBCRD")){
											debitCardList.add(cardTypeDTO);
										}else if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTNBK")){
											netBankList.add(cardTypeDTO);
										}else if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTCASHC")){
											cashCardList.add(cardTypeDTO);
										}else if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTMOBP")){
											mobilePaymentList.add(cardTypeDTO);
										}else if(ServiceUtility.chkNull(payOptStr).toString().equalsIgnoreCase("OPTEMI")){
											optEMIList.add(cardTypeDTO);
										}
									}
								}
							}catch (Exception e) {}
						}
					} catch (JSONException e) {
						Log.e("ServiceHandler", "Error fetching data from server");
					}
				} else {
					Log.e("ServiceHandler", "Couldn't get any data from the url");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// Dismiss the progress dialog
				if (pDialog.isShowing())
					pDialog.dismiss();
				/**
				 * Updating parsed JSON data into ListView
				 * */
				ServiceUtility.log("Payment Option", "size"+payOptionList.size());
				 payOptAdapter = new PayOptAdapter(PaymentOptionSelection_Activity.this, R.layout.pay_opt_spinner_item, payOptionList);
			
				lv_PayOptions.setAdapter(payOptAdapter);
				// bind adapter to spinner
				
				/*gvBankName.setOnItemSelectedListener(new OnItemSelectedListener(){
					@Override
					public void onItemSelected(AdapterView parent, View view, int position, long id) {
						PaymentOptionDTO selectedOption = payOptionList.get(position);
						if(selectedOption.getPayOptId().equals("OPTCRDC")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)creditCardList.clone();
						}else if(selectedOption.getPayOptId().equals("OPTDBCRD")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)debitCardList.clone();
						}else if(selectedOption.getPayOptId().equals("OPTNBK")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)netBankList.clone();
						}else if(selectedOption.getPayOptId().equals("OPTCASHC")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)cashCardList.clone();
						}else if(selectedOption.getPayOptId().equals("OPTMOBP")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)mobilePaymentList.clone();
						}else if(selectedOption.getPayOptId().equals("OPTEMI")){
							selectedCardTypeList = (ArrayList<CardTypeDTO>)optEMIList.clone();
						}
						
						selectedPaymentOption = selectedOption.getPayOptId();
						
						Spinner cardType = (Spinner) findViewById(R.id.spBankOpt);
						CardAdapter cardTypeAdapter = new CardAdapter(m, android.R.layout.simple_spinner_item, selectedCardTypeList);
						cardType.setAdapter(cardTypeAdapter);
						
						
						cardType.setOnItemSelectedListener(new OnItemSelectedListener(){
							@Override
							public void onItemSelected(AdapterView parent, View view, int position, long id) {
								selectedCardType = selectedCardTypeList.get(position);
							}
							@Override
						    public void onNothingSelected(AdapterView<?> parent) {}
						});
					}
					@Override
				    public void onNothingSelected(AdapterView<?> parent) {}
				});*/
			
			}
		}
		
		public View getViewByPosition(int position, HorizontalListView listView) {
		    final int firstListItemPosition = listView.getFirstVisiblePosition();
		    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		    if (position < firstListItemPosition || position > lastListItemPosition ) {
		        return listView.getAdapter().getView(position, null, listView);
		    } else {
		        final int childIndex = position - firstListItemPosition;
		        return listView.getChildAt(childIndex);
		    }
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			
			if(is_popup_visible){
				popup.startAnimation( animHide );
				popup.setVisibility(View.GONE);
				is_popup_visible=false;
			}
			else{
				this.finish();
				overridePendingTransition(R.anim.slide_in_left,
	                    R.anim.slide_out_right);
			}
		}
}
