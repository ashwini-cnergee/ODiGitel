package com.cnergee.mypage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.Utils;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;


public class NotificationAdapter extends ArrayAdapter<Notificationobj> {
	
	Context context;
	int layoutResourceId;
	CheckBox chk;
	Boolean[] alChecked;
	Button delt;
	Notificationobj data[] = null;
	NotificationInterface Inotification;
	ArrayList<String> alNotifyId= new ArrayList<String>();
	 
	public NotificationAdapter(Context context, int layoutResourceId, Notificationobj[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		Inotification=(NotificationInterface) context;
		alChecked= new Boolean[data.length];
		for(int i=0;i<data.length;i++){
			alChecked[i]=false;
		}
		/*System.out.println(layoutResourceId);
		System.out.println(context);
		System.out.println(">>>DataL"+data.length);*/
		//System.out.println("dataposition---"+data[0].getNotification());
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		 ComplaintListHolder  holder = new ComplaintListHolder();

		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
	
			
			//holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
			holder.txtnotificationmessage = (TextView)row.findViewById(R.id.txtnotificationmessage);
			holder.txtnotifyid = (TextView)row.findViewById(R.id.txtnotifyid);
			holder.tv_expire = (TextView)row.findViewById(R.id.tv_expire);
			
			holder.chkbx = (CheckBox)row.findViewById(R.id.chkbx);
			holder.ll_parent=(LinearLayout)row.findViewById(R.id.ll_parent);
		
			row.setTag(holder);
		}
		else{
			holder = (ComplaintListHolder)row.getTag();
		}
		
		
		
		final Notificationobj notificationObj = data[position];
		//System.out.println("position////"+position);
		//System.out.println("dataposition"+data[position].getNotificationMessage());
		if(notificationObj.getNotificationMessage()!=null)
		holder.txtnotificationmessage.setText(Html.fromHtml(notificationObj.getNotificationMessage()));
		if(notificationObj.getNotifyId()!=null)
		holder.txtnotifyid.setText(Html.fromHtml(notificationObj.getNotifyId()));
		holder.chkbx.setChecked(alChecked[position]);
		
		if(notificationObj.getDataFrom().equalsIgnoreCase("SMSPG")){
			if(notificationObj.isIs_red()){
				holder.txtnotificationmessage.setTextColor(Color.RED);
				holder.tv_expire.setVisibility(View.VISIBLE);
				holder.tv_expire.setTextColor(Color.RED);
			}
			else{
				holder.txtnotificationmessage.setTextColor(Color.BLUE);
				holder.tv_expire.setVisibility(View.GONE);
			}
		}
		else{
			holder.txtnotificationmessage.setTextColor(Color.BLACK);
			holder.tv_expire.setVisibility(View.GONE);
		}
		
		
		holder.chkbx.setId(position);
	
	holder.txtnotificationmessage.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Inotification.showIcard(notificationObj.getIcard_id());
			
			if(notificationObj.getDataFrom()!=null){
				if(notificationObj.getDataFrom().length()>0){
					Utils.log("DataFrom","is:"+notificationObj.getDataFrom());
					if(notificationObj.getDataFrom().equalsIgnoreCase("Identity Notification")){
						Inotification.showIcard(notificationObj.getIcard_id(),notificationObj.getDataFrom(),notificationObj);
					}
					if(notificationObj.getDataFrom().equalsIgnoreCase("SMSPG")){
						
						Inotification.showIcard(notificationObj.getNotifyId(),notificationObj.getDataFrom(),notificationObj);
					}
				}
				else{
					Utils.log("DataFrom","is null");
				}
			}
			else{
				Utils.log("DataFrom","is null");
			}
		}
	});	
		
	 holder.chkbx.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			
			CheckBox cb= (CheckBox) v; 
			int id =cb.getId();
			
				if (alChecked[id]) {
					Utils.log("Position Unchecked",
							":" + data[position].getNotifyId());
					cb.setChecked(false);
					alNotifyId.remove(data[position].getNotifyId());
					alChecked[id] = false;
				} else {
					Utils.log("Position Checked",
							":" + data[position].getNotifyId());
					cb.setChecked(true);
					alNotifyId.add(data[position].getNotifyId());
					alChecked[id] = true;
				}

			 Inotification.showDelete(alNotifyId);
			
		}
	});

		return row;
	}
	
	
	public class ComplaintListHolder
	{
		//ImageView imgIcon;
		TextView txtnotificationmessage;
		TextView txtnotifyid,tv_expire;
		CheckBox chkbx;
		LinearLayout ll_parent;
	}
	

}
