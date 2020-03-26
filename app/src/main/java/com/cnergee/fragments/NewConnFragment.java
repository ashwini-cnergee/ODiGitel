package com.cnergee.fragments;

import java.lang.reflect.Member;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import cnergee.krishna.ookla.odigitel.R;
import com.cnergee.mypage.SOAP.GetnewConnDetailSOAP;
import com.cnergee.mypage.SOAP.GetnewEquirySOAP;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.DateTimePicker;
import com.cnergee.widgets.ProgressHUD;


public class NewConnFragment extends Fragment {

	View view;
	EditText etMobile,etDate_Time,etAddress,etEmail,etCaptcha;
	TextView tvCaptcha;
	EditText etName;
	Button Submit;
	DateTimePicker dateTimePicker;
	String date_time="";
	String str_date_time="";
	String captcha ="",MemberId,memberloginid;
	SharedPreferences sharedPreferences1;
	Utils utils =new Utils();
	private String sharedPreferences_name;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view= inflater.inflate(R.layout.fragment_newconn, container, false);

		initializeControls();
		//etDate_Time.setOnTouchListener(otl);

		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for

		utils.setSharedPreferences(sharedPreferences);
		sharedPreferences1 = getContext().getSharedPreferences(
				getString(R.string.shared_preferences_renewal), 0);

		MemberId = utils.getMemberId();
		memberloginid = utils.getMemberLoginID();

		Log.e("MemberId",":"+MemberId);

		return view;
	}

	private OnTouchListener otl = new OnTouchListener() {
		public boolean onTouch (View v, MotionEvent event) {
			showD();
			return true; // the listener has consumed the event
		}
	};

	public void initializeControls(){
		etName=(EditText)view.findViewById(R.id.etFullName);
		etMobile=(EditText)view.findViewById(R.id.etMobNum);
		etDate_Time=(EditText)view.findViewById(R.id.etDateTime);
		etAddress=(EditText)view.findViewById(R.id.etAddress);
		Submit=(Button)view.findViewById(R.id.btnsubmit);
		etEmail=(EditText)view.findViewById(R.id.etEmail);
		etCaptcha=(EditText)view.findViewById(R.id.etCaptcha);
		tvCaptcha=(TextView)view.findViewById(R.id.tvCaptcha);
		etDate_Time.setOnClickListener(clicklisListener);
		Submit.setOnClickListener(clicklisListener);
		Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/adam.ttf");
		tvCaptcha.setTypeface(custom_font);


	}

	public void onResume() {
		super.onResume();

		char[] alphNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder((100 + rnd.nextInt(900)) + "-");
		for (int i1 = 0; i1 < 3; i1++)
			sb.append(alphNum[rnd.nextInt(alphNum.length)]);

		captcha= sb.toString();
		tvCaptcha.setText(captcha);
		//System.out.println(captcha);

		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(7);
		filterArray[1] =new InputFilter.AllCaps();
		etCaptcha.setFilters(filterArray);

		etCaptcha.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(count==7){

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	OnClickListener clicklisListener= new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btnsubmit){
				String Message="";

				if(etName.getText().length()>0){

					if(etMobile.getText().length()>0&&etMobile.getText().length()>9){
						if(etDate_Time.getText().length()>0){

							if(etAddress.getText().length()>0){
								if(etEmail.getText().length()>0){
									if(emailValidator(etEmail.getText().toString())){
										if(captcha.equalsIgnoreCase(etCaptcha.getText().toString()))
											new GetNewConnectionAsyncTask().execute();
									}

									else{
										Message="Please enter valid email id";
										Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
									}
								}
								else{
									Message="Please enter your email id";
									Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
								}
							}
							else{
								Message="Please enter your address";
								Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
							}
						}
						else{
							Message="Please select visit date and time";
							Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
						}
					}
					else{
						Message="Please enter valid mobile Number";
						Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
					}
				}
				else{
					Message="Please enter your Full Name";
					Toast.makeText(getActivity(), Message, Toast.LENGTH_LONG).show();
				}

			}

			if(v.getId()==R.id.etDateTime){
				showD();
			}
		}
	};

	public void showDateTimePicker(){

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(dateTimePicker);
		int width = 0;
		int height =0;


		Point size = new Point();
		WindowManager w =getActivity().getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height   = d.getHeight();;
		}
		Button Ok=(Button) dialog.findViewById(R.id.btnOk);
		Button Cancel=(Button) dialog.findViewById(R.id.btnCancel);
		Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		Cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);

		dialog.show();
	}

	public void showD(){
		final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		final AlertDialog alertd = dialog.create();
		dateTimePicker= new DateTimePicker(getActivity());
		dialog.setView(dateTimePicker);



		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				long date_time=dateTimePicker.getDateTimeMillis();
				Calendar cl = Calendar.getInstance();
				cl.setTimeInMillis(date_time);  //here your time in miliseconds
				String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + (cl.get(Calendar.MONTH)+1) + "/" + cl.get(Calendar.YEAR);
				String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) ;
				//insertValue(value,st1,st2,st3,st4);

				String day="";
				if(cl.get(Calendar.DAY_OF_MONTH)>9){
					day=String.valueOf(cl.get(Calendar.DAY_OF_MONTH));
				}
				else{
					day="0"+String.valueOf(cl.get(Calendar.DAY_OF_MONTH));
				}
				String month="";
				if((cl.get(Calendar.MONTH)+1)>9){
					month=String.valueOf(cl.get(Calendar.MONTH)+1);
				}
				else{
					month="0"+String.valueOf(cl.get(Calendar.MONTH)+1);
				}
				String hour="";
				if((cl.get(Calendar.HOUR_OF_DAY))>9){
					hour=String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
				}
				else{
					hour="0"+String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
				}


				String min="";
				if((cl.get(Calendar.MINUTE))>9){
					min=String.valueOf(cl.get(Calendar.MINUTE));
				}
				else{
					min="0"+String.valueOf(cl.get(Calendar.MINUTE));
				}

				String sec="";
				if((cl.get(Calendar.SECOND))>9){
					sec=String.valueOf(cl.get(Calendar.SECOND));
				}
				else{
					sec="0"+String.valueOf(cl.get(Calendar.SECOND));
				}

		        	/*if((cl.get(Calendar.HOUR_OF_DAY)+1)>9){
		        		month=String.valueOf(cl.get(Calendar.MONTH)+1);
		        	}
		        	else{
		        		day="0"+String.valueOf(cl.get(Calendar.MONTH)+1);
		        	}*/
				str_date_time=day+month+ cl.get(Calendar.YEAR)+hour+min+sec;
				Utils.log("Date",":"+date);
				Utils.log("Time",":"+time);
				etDate_Time.setText(date +" "+ time);
			}
		});

		dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				dialog.cancel();
				alertd.dismiss();
			}
		});

			/*dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);*/

		dialog.show();
	}

	public class GetNewConnectionAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		GetnewConnDetailSOAP getnewConnDetailSOAP;
		ProgressHUD mProgressHUD;
		String rslt="",response="";

		GetnewEquirySOAP getnewEquirySOAP;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true,this);
			char[] alphNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			Random rnd = new Random();

			StringBuilder sb = new StringBuilder((100 + rnd.nextInt(900)) + "-");
			for (int i1 = 0; i1 < 3; i1++)
				sb.append(alphNum[rnd.nextInt(alphNum.length)]);

			captcha= sb.toString();
			tvCaptcha.setText(captcha);
			//System.out.println(captcha);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if(memberloginid!=null && memberloginid.length()>0) {
					getnewEquirySOAP = new GetnewEquirySOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_ENQUIRY_WITH_SUBSCRIBERID));
					rslt = getnewEquirySOAP.sendProspectDetails(memberloginid,etName.getText().toString(), etMobile.getText().toString(), str_date_time, etAddress.getText().toString(),etEmail.getText().toString());
					response = getnewEquirySOAP.getResponse();
				}else{
					getnewConnDetailSOAP = new GetnewConnDetailSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_NEW_CONN));
					rslt = getnewConnDetailSOAP.sendProspectDetails(etName.getText().toString(), etMobile.getText().toString(), str_date_time,  etAddress.getText().toString(),etEmail.getText().toString());
					response = getnewConnDetailSOAP.getResponse();
				}
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
			if(rslt.length()>0){
				if(rslt.equalsIgnoreCase("ok")){
					if(response.length()>0){

						AlertsBoxFactory.showAlert(response
								, getActivity());

						etName.setText("");etMobile.setText("");etDate_Time.setText("");etAddress.setText("");etEmail.setText("");



					}
				}
			}
			else{
				AlertsBoxFactory.showAlert("Please try again!!", getActivity());
			}

		}
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}

	}
	public boolean emailValidator(String email)
	{
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
