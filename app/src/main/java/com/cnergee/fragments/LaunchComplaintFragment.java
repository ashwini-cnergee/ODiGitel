package com.cnergee.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.SelfResolution;
import com.cnergee.mypage.caller.ComplaintCategoryListCaller;
import com.cnergee.mypage.caller.InsertComplaintCaller;
import com.cnergee.mypage.obj.ComplaintCategoryList;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import androidx.fragment.app.Fragment;
import cnergee.krishna.ookla.odigitel.R;

public class LaunchComplaintFragment extends Fragment {
	
View view;
Spinner spComplaint;
Button btnCancel,btnSubmit;
EditText etComments;
ArrayList<String> ComplaintName;
ArrayList<String> ComplaintId;
public static String rslt = "",responseMsg="";

public static ArrayList<ComplaintCategoryList>complaintcategorylist;


@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Utils.log(this.getClass().getSimpleName(), "OnAttach Called");
		 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			 new ComplaintCategoryListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		    else
		    new ComplaintCategoryListWebService().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view= inflater.inflate(R.layout.fragment_launch_complaint, container, false);
		initializeControls();
		
	
		 
		 btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		 
		 
		 btnSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Utils.isOnline(getActivity())){
						Utils.log("submit Button","Submiit Button Executed");
					
					
						if(spComplaint.getSelectedItemPosition()!=0){
							Utils.log("spinner executed","Spinner Executed");
					if(TextUtils.isEmpty(etComments.getText().toString().trim()))
					{	
						Utils.log("spinner  empty Text executed","Spinner Emptytext Executed");
						AlertsBoxFactory.showAlert(" Please enter valid comments.",getActivity() );
						
					}
					else
					{
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							 new InsertComplaintWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						    else
						    new InsertComplaintWebService().execute();
						 
				
					}
					
						}
						else{
							//AlertsBoxFactory.showAlertOffer("Package Alert","Please select Complaint category type.",context);
							
							AlertsBoxFactory.showAlert2("Please select Complaint category type.",getActivity() );
						}
					}
					else{
						Toast.makeText(getActivity().getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
					}
				}
			});
		return view;
	}
	
	public void initializeControls(){
		spComplaint=(Spinner) view.findViewById(R.id.spComplaint);
		btnCancel=(Button)view.findViewById(R.id.btncancel);
		btnSubmit=(Button)view.findViewById(R.id.btnsubmit);
		etComments=(EditText)view.findViewById(R.id.comments);
		 ComplaintName= new ArrayList<String>();
		 ComplaintId= new ArrayList<String>();
	}
	
	

@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Utils.log(this.getClass().getSimpleName(), "onDetach Called");
	}
@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.log(this.getClass().getSimpleName(), "onResume Called");
	}
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Utils.log(this.getClass().getSimpleName(), "onDestroyView Called");
	}
@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Utils.log(this.getClass().getSimpleName(), "onDestroy Called");
	}
	
	private class ComplaintCategoryListWebService extends AsyncTask<String, Void, Void> implements OnCancelListener  {
		
		ProgressHUD mProgressHUD;
	
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true, this);
		}
		@Override
		protected void onCancelled() {
			
			
		}
		protected void onPostExecute(Void unused) {
			
			mProgressHUD.dismiss();
			
		
		
		if (rslt.trim().equalsIgnoreCase("ok")) {
			ComplaintName = new ArrayList<String>();
			ComplaintId = new ArrayList<String>();
			for(int i=0; i< complaintcategorylist.size(); i++ )
			{
					ComplaintName.add(complaintcategorylist.get(i).getComnplaintName());
					ComplaintId.add(complaintcategorylist.get(i).getComplaintId());
			}
			ArrayAdapter ComplainArray = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ComplaintName);
			spComplaint.setAdapter(ComplainArray);
			} else {
				
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, getActivity());
			}
		//new SelectedWebService().execute();
	;
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				ComplaintCategoryListCaller caller = new ComplaintCategoryListCaller(getActivity().getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								getActivity().getApplicationContext().getResources().getString(
								R.string.SOAP_URL),getActivity(). getApplicationContext()
								.getResources().getString(
										R.string.METHOD_COMPLAINT_CATEGORY_LIST));
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
			}
			return null;
		}
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			
		}

	}
	
	private class InsertComplaintWebService extends AsyncTask<String, Void, Void>implements OnCancelListener {

		ProgressHUD mProgressHUD;
		ComplaintObj complaintobj = new ComplaintObj();

		protected void onPreExecute() {
		
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true, this);
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			
		}

		
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			//submit.setClickable(true);
			

			if (rslt.trim().equalsIgnoreCase("ok")) {
				
				final Dialog dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//tell the Dialog to use the dialog.xml as it's layout description
				dialog.setContentView(R.layout.dialog_box);
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
				
				
				
				    TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

				TextView txt = (TextView) dialog.findViewById(R.id.tv);

				txt.setText(Html.fromHtml(responseMsg));
				dtv.setText(Html.fromHtml("Confirmation"));
				Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

				
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						dialog.dismiss();
						getActivity().finish();
						Intent i = new Intent(getActivity(), IONHome.class);
						startActivity(i);
					}
				});
				
				dialog.show();
				//(width/2)+((width/2)/2)
				//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
				
			} else {
				AlertsBoxFactory.showAlert(rslt,getActivity() );
				return;
			}
		}
		

		@Override
		protected Void doInBackground(String... params) {
			try {
				//Log.i("ComplaintId",""+ComplaintId.get(spinnerList.getSelectedItemPosition()));
				//complaintobj.setMemberComplaintNo(txtcomplaintno.getText().toString());
				complaintobj.setComplaintId(ComplaintId.get(spComplaint.getSelectedItemPosition()));
				complaintobj.setMemberId(SelfResolution.Memberid);
				complaintobj.setMessage(etComments.getText().toString());
				
				InsertComplaintCaller caller = new InsertComplaintCaller(
						getActivity().getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								getActivity().getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getActivity().getApplicationContext()
								.getResources().getString(
										R.string.METHOD_INSERT_COMPLAINTS),"complaint");
				
				
				caller.setcomplaintobj(complaintobj);
	
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				//AlertsBoxFactory.showAlert(rslt,context );
				
				Utils.log("Error Complaint","596"+e);
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
}
