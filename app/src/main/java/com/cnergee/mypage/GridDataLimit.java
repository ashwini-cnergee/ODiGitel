package com.cnergee.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cnergee.plan.calc.DataLimit;
import cnergee.krishna.ookla.odigitel.R;

public class GridDataLimit  extends ArrayAdapter<DataLimit>{
	ArrayList<DataLimit> alDataLimit;
	Context ctx;
	String data;
	int resource_id;
		public GridDataLimit(Context context, int textViewResourceId,
				ArrayList<DataLimit> alDataLimit,String data) {
			super(context, textViewResourceId, alDataLimit);
			// TODO Auto-generated constructor stub
			this.alDataLimit=alDataLimit;
			ctx=context;
			this.data=data;
			this.resource_id=textViewResourceId;
		}
		
		public class ViewHolder{
			TextView tvNumber;
			TextView tvUnit;
			LinearLayout ll_gv;
		}

		@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) ctx
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder= new ViewHolder();
			DataLimit dataLimit=(DataLimit)this.getItem(position);
			if(convertView==null){
				if(data.equalsIgnoreCase("speed")){
				convertView=inflater.inflate(resource_id, null);
				}
				else{
				convertView=inflater.inflate(resource_id, null);
				}
				holder.tvNumber=(TextView) convertView.findViewById(R.id.tvNumber);
				holder.tvUnit=(TextView)convertView.findViewById(R.id.tvUnit);
				holder.ll_gv=(LinearLayout) convertView.findViewById(R.id.ll_gv);
				 convertView.setTag(holder);
			}
			else{
				 holder = (ViewHolder) convertView.getTag();
			}
			holder.tvNumber.setText(dataLimit.getData_Gb_Value());
			
			if(data!=null){
				if(data.equalsIgnoreCase("speed")){
			if(position==0){
				holder.tvUnit.setVisibility(View.VISIBLE);
				holder.tvUnit.setText("kbps");
			}
			else{
				holder.tvUnit.setVisibility(View.GONE);
			}
				}
			}
				return convertView;
			}
		
		
	}
