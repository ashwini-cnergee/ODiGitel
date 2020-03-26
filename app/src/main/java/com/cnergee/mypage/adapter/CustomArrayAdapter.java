package com.cnergee.mypage.adapter;

import java.util.ArrayList;

import all.interface_.IDaysSelecttion_Topup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;

public class CustomArrayAdapter extends ArrayAdapter<String> {
	
	ArrayList<String> alNum;
	Context ctx;
	ViewHolder holder;
	IDaysSelecttion_Topup iDaysSelecttion_Topup;
	public CustomArrayAdapter(Context context, int resource,
			ArrayList<String> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alNum=objects;
		iDaysSelecttion_Topup=(IDaysSelecttion_Topup) ctx;
	}
	
	public class ViewHolder{
		TextView tvNumber;
		
		LinearLayout ll_gv;
	}

	
	public View getView(final int position, View convertView, final ViewGroup parent){
		View v = convertView;

		LayoutInflater inflater = (LayoutInflater) ctx
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		holder	= new ViewHolder();
		if(convertView==null){
			
			convertView=inflater.inflate(R.layout.days_row_item, null);
			
			holder.tvNumber=(TextView) convertView.findViewById(R.id.tvNum);
		
			holder.ll_gv=(LinearLayout) convertView.findViewById(R.id.ll_button);
			 convertView.setTag(holder);
		}
		else{
			 holder = (ViewHolder) convertView.getTag();
		}
		holder.tvNumber.setText(alNum.get(position));
		if(alNum.get(position).equalsIgnoreCase("1")){
			holder.ll_gv.setBackgroundResource(R.drawable.tp_single_day_button);
		}
		else{
			holder.ll_gv.setBackgroundResource(R.drawable.tp_days_button);
		}
		holder.ll_gv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				iDaysSelecttion_Topup.Days_Selected(alNum.get(position).toString());
				
				Utils.log("Button Clicked","For Days");
				for(int i=0;i<alNum.size();i++){
					
					View v1=parent.getChildAt(i);
					LinearLayout c=(LinearLayout)v1.findViewById(R.id.ll_button);
					if(i==position){
						if(alNum.get(position).equalsIgnoreCase("1")){
							
								c.setBackgroundResource(R.drawable.tp_single_day_button_selected);
						}
							else{
								c.setBackgroundResource(R.drawable.tp_days_button_selected);
							}
					}
					else{
						if(alNum.get(position).equalsIgnoreCase("1")){
							c.setBackgroundResource(R.drawable.tp_single_day_button);
						}
						else{
							c.setBackgroundResource(R.drawable.tp_days_button);
							}
					
					}
				}
			}
		});
		
		return convertView;
	}
}
