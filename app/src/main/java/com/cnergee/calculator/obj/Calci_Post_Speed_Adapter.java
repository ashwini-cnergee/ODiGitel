/**
 * 
 */
package com.cnergee.calculator.obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;

/**
 * @author Sandip
 *
 */
public class Calci_Post_Speed_Adapter extends ArrayAdapter<Calci_Post_Speed>{
	ArrayList<Calci_Post_Speed> alSpeddPost;
	Context ctx;
	String data;
	int resource_id;
		public Calci_Post_Speed_Adapter(Context context, int textViewResourceId,
				ArrayList<Calci_Post_Speed> alSpeddPost,String data) {
			super(context, textViewResourceId, alSpeddPost);
			// TODO Auto-generated constructor stub
			this.alSpeddPost=alSpeddPost;
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
			Calci_Post_Speed calci_Post_Speed=(Calci_Post_Speed)this.getItem(position);
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
			holder.tvNumber.setText(calci_Post_Speed.getCalc_post_speed_value());
			
			if(data!=null){
				if(calci_Post_Speed.getCalc_post_speed_value().equalsIgnoreCase("512")){
		
				holder.tvUnit.setVisibility(View.VISIBLE);
				holder.tvUnit.setText("kbps");
			}
			else{
				holder.tvUnit.setVisibility(View.GONE);
			}
				
			}
				return convertView;
			}
		
		
	}
