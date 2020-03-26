

package com.cnergee.mypage.utils;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.List;

import cnergee.krishna.ookla.odigitel.R;

public class AlertsBoxFactory {

		public static void showAlert(String message, Context ctx)
		{
			//showAlert(null,message,ctx);
			showAlert2(message, ctx);
		}
	   public static void showAlert(String strTitle,String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	      
	      builder.setIcon(R.drawable.alert_yellow);
	      
	      if(strTitle != null)
	    	  builder.setTitle(strTitle);
	      else
	    	  builder.setTitle("Alert");
	      
	      builder.setMessage(Html.fromHtml(message));
	      
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }
	   
	   public static void showAlertOffer12(String strTitle,String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	    //  builder.setIcon(android.R.drawable.ic_dialog_alert);
	      builder.setIcon(R.drawable.alert_yellow);
	      if(strTitle != null)
	    	  builder.setTitle(strTitle);
	      else
	    	  builder.setTitle("Alert");
	      
	      builder.setMessage(Html.fromHtml(message));
	      
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }
	   public static void showAlertColorTxt(String str_color,String message, Context ctx)
	   {
		   showAlertColorTxt(null,str_color,message,ctx);
	   }
	   public static void showAlertColorTxt(String strTitle,String str_color,String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	     // builder.setIcon(android.R.drawable.ic_dialog_alert);
	      builder.setIcon(R.drawable.alert_yellow);
	      if(strTitle != null)
	    	  builder.setTitle(strTitle);
	      else
	    	  builder.setTitle("Alert");
	      
	      if(str_color != null || str_color != ""){
	    	  builder.setMessage(Html.fromHtml("<font color='"+str_color+"'>"+message+"</font>"));
	      }
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }

	   public static void showErrorAlert(String message, Context ctx)
	   {
	      //get a builder and set the view
	      AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	     // builder.setIcon(android.R.drawable.ic_dialog_info);
	      builder.setIcon(R.drawable.alert_yellow);
	      builder.setTitle("Exception ");
	      builder.setMessage(message);
	      
	      //add buttons and listener
	      EmptyListener pl = new EmptyListener();
	      builder.setPositiveButton("OK", pl);
	      
	      //get the dialog
	      AlertDialog ad = builder.create();
	      
	      //show
	      ad.show();
	   }

	   public static void showExitAlert(String message, final Context context){
		   AlertDialog.Builder builder = new AlertDialog.Builder(
					context);
			builder.setMessage(
					Html.fromHtml("<font color='#FFA500'><b>Are you sure you want to process?</b></font>"))
			.setIcon(R.drawable.alert_yellow)	
					.setTitle(
							Html.fromHtml("<font color='#FFA500'>Change Web Service Authentication</font>"))
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {
									
							        Intent intent = new Intent(Intent.ACTION_MAIN); 
							        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							        intent.addCategory(Intent.CATEGORY_HOME); 
							        context.startActivity(intent);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {
									dialog.cancel();
									// isConfirm = false;
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
	   }
	   
	   public static void showAlert2(String message, Context ctx){
		   final Dialog dialog = new Dialog(ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.custom_dialog_box);
			/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);*/
			int width = 0;
			int height =0;
			dialog.setCancelable(false);
			
			    Point size = new Point();
			    WindowManager w =((Activity)ctx).getWindowManager();

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

			txt.setText(Html.fromHtml(message));

			Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

			
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
				}
			});
			if(isRunning((Activity)ctx))
			dialog.show();
			//(width/2)+((width/2)/2)
			//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
	   }
	   
	   public static void showAlertOffer(String strTitle,String message, Context ctx){
		   final Dialog dialog = new Dialog(ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.custom_dialog_box);
			/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);*/
			int width = 0;
			int height =0;
			
			
			    Point size = new Point();
			    WindowManager w =((Activity)ctx).getWindowManager();

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
			dtv.setText(Html.fromHtml(strTitle));
			txt.setText(Html.fromHtml(message));

			Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

			
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
				}
			});
			
			dialog.show();
			//(width/2)+((width/2)/2)
			//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
	   }
	   
	   public static void showAlert3(String message, Context ctx){
		   final Dialog dialog = new Dialog(ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.alerts);
			/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);*/
			int width = 0;
			int height =0;
			
			
			    Point size = new Point();
			    WindowManager w =((Activity)ctx).getWindowManager();

			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			        w.getDefaultDisplay().getSize(size);
			        width = size.x;
			        height = size.y;
			    } else {
			        Display d = w.getDefaultDisplay();
			        width = d.getWidth();
			        height   = d.getHeight();;
			    }
			
			TextView	dtv = (TextView) dialog.findViewById(R.id.text);

			TextView txt = (TextView) dialog.findViewById(R.id.tvv);

			txt.setText(Html.fromHtml(message));

			Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmitt);

			
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
				}
			});
			
			dialog.show();
			//(width/2)+((width/2)/2)
			//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
	   } 
	   
	
	   public static boolean isRunning(Context ctx) {
	        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

	        for (RunningTaskInfo task : tasks) {
	            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) 
	                return true;                                  
	        }

	        return false;
	    }  
	   
	   
}