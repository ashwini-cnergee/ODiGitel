package com.cnergee.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnergee.mypage.caller.DataUsageCaller;
import com.cnergee.mypage.obj.CircularProgressBar;
import com.cnergee.mypage.obj.DataUsageObj;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.cnergee.widgets.ProgressPieView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cnergee.krishna.ookla.odigitel.R;


public class DataUsageFragment extends Fragment {

	private static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	
	private String SharedPreferences_name;
	private boolean flag=true;
	public String memberloginid;
	public long memberid;
	TextView txtused,txtallotedused,txtalotted,txttotalallotted,txtremainig,txttotalremainig, tvUsed;
	private boolean isFinish = false;
	private String sharedPreferences_name;
	
	public static Map<String ,PackageDetails> mapPackageDetails;
	public static Map<String ,DataUsageObj> mapdatausage;
	
	
	private  GetDataUsageWebServices getdatausagewebservice = null;
CircularProgressBar circularBar;
	
	ProgressPieView mProgressPieView;
	boolean isLogout = false;
@Override
public View onCreateView(LayoutInflater inflater,
		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	
	View view= inflater.inflate(R.layout.fragment_data_usage, container, false);
	
	tvUsed=(TextView) view.findViewById(R.id.tvUsedPercentage);
	mProgressPieView = (ProgressPieView) view.findViewById(R.id.progressPieViewXml);
	mProgressPieView.setProgressColor(getResources().getColor(R.color.color_red));
	mProgressPieView.setBackgroundColor(getResources().getColor(R.color.color_green));
	
	//btnimagehome = (ImageView)findViewById(R.id.img_headrhome);
	txtused = (TextView)view.findViewById(R.id.txtused);
	txtallotedused = (TextView)view.findViewById(R.id.txtallotedused);
	txtalotted =(TextView)view.findViewById(R.id.txtalloted);
	txttotalallotted = (TextView)view.findViewById(R.id.txttotaslalotted);
	txtremainig = (TextView)view.findViewById(R.id.txtremaining);
	txttotalremainig =(TextView)view.findViewById(R.id.txtallotedremain);
	getdatausagewebservice= new GetDataUsageWebServices();
	getdatausagewebservice.execute();
	return view;
}
private  class GetDataUsageWebServices extends AsyncTask<String ,Void,Void> implements OnCancelListener {
	// private ProgressDialog Dialog = new ProgressDialog(getActivity());
	 ProgressHUD mProgressHUD ;
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true,this);
			Utils.log("MYAppDataUsage 157" ,"Aynck Task Executed");
		}

		protected void onPostExecute(Void unused) {
			getdatausagewebservice = null;
			mProgressHUD.dismiss();

				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapdatausage != null) {
					
					Set<String> keys = mapdatausage.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					 DataUsageObj datausage = mapdatausage.get(selItem);
					
					//txtallotedtime.setText(datausage.getSessionTime());
					//txtusedtime.setText(datausage.getActiveTime());
					//txtusedupload.setText(datausage.getUpLoadData());
					//txtdownloadused.setText(datausage.getDownLoadData());
					//txtallotedtime.setText(datausage.getAllotedData());
				
					txttotalallotted.setText(datausage.getAllotedData());
					txtallotedused.setText(datausage.getTotalDataTransfer());
					txttotalremainig.setText(datausage.getRemainData());
					
					Thread.sleep(1000);
					
					Double GetPercentage=(Double.parseDouble(datausage.getTotalDataTransfer())*100)/Double.parseDouble(datausage.getAllotedData());
					
					mProgressPieView.setMax(100);
						mProgressPieView.animateProgressFill(GetPercentage
								.intValue());
						mProgressPieView.setText("");
						
						tvUsed.setText(GetPercentage
								.intValue()+"% Used");
						
						
						/*	mProgressPieView.setProgress(GetPercentage
								.intValue());
						mProgressPieView.animateProgressFill(GetPercentage
								.intValue());
						mProgressPieView.setText("");
						tvUsed.setText(GetPercentage
								.intValue()+"%\n used");
						
						
						mProgressPieView.setOnProgressListener(new OnProgressListener() {
							
							@Override
							public void onProgressCompleted() {
								// TODO Auto-generated method stub
							
							}
							
							@Override
							public void onProgressChanged(int progress, int max) {
								// TODO Auto-generated method stub
								Utils.log("Progress","is:"+progress);
								tvUsed.setText(progress+"%\n used");
							}
						});*/
						
						txttotalallotted.setText(datausage.getAllotedData());
						txtallotedused.setText(datausage.getTotalDataTransfer());
						txttotalremainig.setText(datausage.getRemainData());
						
						txttotalallotted.invalidate();
						txtallotedused.invalidate();
						txttotalremainig.invalidate();
					//	mProgressPieView.animateProgressFill();
					
					/*circularBar.animateProgressTo(0, GetPercentage.intValue(), new ProgressAnimationListener() {
						
						@Override
						public void onAnimationStart() {				
						}
						
						@Override
						public void onAnimationProgress(int progress) {
							circularBar.setTitle(progress + "%");
							
							tvUsed.setText(progress+"%\n used");
						}
						
						@Override
						public void onAnimationFinish() {
							circularBar.setSubTitle("used");
						}
					});
					*/
					}
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				//AlertsBoxFactory.showAlert("Subscriber not found !!! ",context );
				mProgressPieView.setText("NA");
				mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
				circularBar.setTitle("NA");
				circularBar.setSubTitle("");
			}else{
				circularBar.setTitle("NA");
				mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
				circularBar.setSubTitle("");
				//AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){
				//AlertsBoxFactory.showAlert(rslt,context );
				Utils.log("Error","is:"+e);
				mProgressPieView.setText("NA");
				mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
				circularBar.setTitle("NA");
				circularBar.setSubTitle("");
			}	
		}
		@Override
		protected Void doInBackground(String... params) {
			try {
				
				Utils.log("In BackGnd Aync203", "AynckTaskExceuted");
				DataUsageCaller datausagecaller = new DataUsageCaller(
						getActivity().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
								getActivity().getResources().getString(
								R.string.SOAP_URL), getActivity()
								.getResources().getString(
										R.string.METHOD_GETUSAGEDETAILS)
										);

				datausagecaller.memberid = memberid;
				
				
				datausagecaller.join();
				datausagecaller.start();
				rslt = "START";

				while(rslt == "START")  {
					try {
					
						Thread.sleep(10);
					} catch (Exception ex) {
					
						ex.printStackTrace();
						
						Utils.log("Try","Excuted try");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			isFinish=true;
			return null;
		}
		 @Override
			protected void onCancelled() {
			 mProgressHUD.dismiss();
				getdatausagewebservice = null;
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
	
}
