package com.cnergee.mypage.adapter;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import all.interface_.IRefreshAdapter;
import all.interface_.IViewImage;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.ChatActivity;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.Chat;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;
import com.squareup.picasso.Picasso;



public class ChatArrayAdapter extends ArrayAdapter<Chat> {
	DatabaseAdapter dbAdapter;
	ArrayList<Chat> alChat;
	Context ctx;
	public boolean thumbnails[];
	IViewImage iShowImage;
	IRefreshAdapter iRefreshAdapter;
	ViewHolder holder= null;
	String image_name="";
	String row_id="";
	int pos;
	int height,width;
	SharedPreferences sharedPreferences;


	public ChatArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Chat> alChat) {
		super(context, textViewResourceId, alChat);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alChat= new ArrayList<Chat>(alChat);
		this.alChat.addAll(alChat);
	//	this.thumbnails = new boolean[alChat.size()];
		iShowImage=(IViewImage)context;
		iRefreshAdapter=(IRefreshAdapter)context;
		dbAdapter= new DatabaseAdapter(ctx);
	}

	public static class ViewHolder{
		TextView tvAdminName,tvName;
		TextView tvClient,tvClientTime;
		TextView tvServer,tvServerTime,tvDate;
		ImageView ivServer,ivClient;
		Button btnServerDwld,btnServerView;
		LinearLayout llClient,llServer,lldate,llMain;			
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ArrayList<Integer> alSel=ChatActivity.alSelected;
		holder= new ViewHolder();
		try{
		final Chat chat= (Chat)this.getItem(position);
		

		if(convertView==null)
		{
			LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.row_item, null);
			
			holder.tvAdminName=(TextView)convertView.findViewById(R.id.tvAdminName);
			holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
			
			holder.tvClient=(TextView)convertView.findViewById(R.id.tvClient);
			holder.tvClientTime=(TextView)convertView.findViewById(R.id.tvClientTime);
			holder.ivClient=(ImageView) convertView.findViewById(R.id.ivClient);
			
			holder.tvServer=(TextView)convertView.findViewById(R.id.tvServer);
			holder.tvServerTime=(TextView)convertView.findViewById(R.id.tvServerTime);
			holder.ivServer=(ImageView) convertView.findViewById(R.id.ivServer);
			
			holder.btnServerDwld=(Button) convertView.findViewById(R.id.btnServerDwld);
			holder.btnServerView=(Button) convertView.findViewById(R.id.btnServerView);
			
			holder.llClient=(LinearLayout) convertView.findViewById(R.id.llClient);
			holder.llServer=(LinearLayout) convertView.findViewById(R.id.llServer);
			holder.llMain=(LinearLayout) convertView.findViewById(R.id.llMainChat);
			
			holder.tvDate=(TextView)convertView.findViewById(R.id.tvdate);
			holder.lldate=(LinearLayout) convertView.findViewById(R.id.llDate);
			
			sharedPreferences=(SharedPreferences)ctx.getSharedPreferences(ctx.getString(R.string.app_setting), ctx.MODE_PRIVATE);
			if(sharedPreferences.getInt("font_size", 1)==0){
				//Large
				holder.tvAdminName.setTextSize(18);
				holder.tvName.setTextSize(18);
				holder.tvClient.setTextSize(20);
				holder.tvClientTime.setTextSize(15);
				holder.tvServer.setTextSize(20);
				holder.tvServerTime.setTextSize(15);
				holder.tvDate.setTextSize(20);
				
				height=100;
				width=100;
			}
			if(sharedPreferences.getInt("font_size", 1)==1){
				//Medium
				height=70;
				width=70;
			}
			if(sharedPreferences.getInt("font_size", 1)==2){
				//Small
				holder.tvAdminName.setTextSize(9);
				holder.tvName.setTextSize(9);
				holder.tvClient.setTextSize(10);
				holder.tvClientTime.setTextSize(8);
				holder.tvServer.setTextSize(10);
				holder.tvServerTime.setTextSize(8);
				holder.tvDate.setTextSize(10);
				height=50;
				width=50;
			}
			convertView.setTag(holder);
			
		}
		else{			
			holder=(ViewHolder) convertView.getTag();			
		}
		
		if(ChatActivity.Action_Row_ID.size()>0){
			if(ChatActivity.Action_Row_ID.contains(String.valueOf(position))){
				holder.llMain.setBackgroundColor(R.color.kesari_color);
			}
			else{
				holder.llMain.setBackgroundColor(android.R.color.white);
			}
		}
		else{
			holder.llMain.setBackgroundColor(android.R.color.white);
		}

		
		if(chat.getDate_change().equalsIgnoreCase("true")){
			holder.llClient.setVisibility(View.GONE);
			holder.llServer.setVisibility(View.GONE);
			holder.lldate.setVisibility(View.VISIBLE);
			SimpleDateFormat sf= new SimpleDateFormat("dd/MM/yyyy");
			String str_date=chat.getDate();
			try {
				Date date= sf.parse(str_date);
				SimpleDateFormat f = new SimpleDateFormat("d MMM yyyy");
				System.out.println(" Date " + f.format(date)); 
				holder.tvDate.setText(f.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Utils.log("parse exception",""+e);
				e.printStackTrace();
			}
		}
		
		if(chat.getSource().equalsIgnoreCase("server")){
			//thumbnails[position] = false;
			Utils.log("Name","is:"+chat.getAdmin_name());
			holder.tvAdminName.setText(chat.getAdmin_name());
			holder.tvServer.setText(chat.getMessage());
			holder.tvServerTime.setText(chat.getTime());
			holder.llClient.setVisibility(View.GONE);	
			holder.lldate.setVisibility(View.GONE);
			holder.llServer.setVisibility(View.VISIBLE);
			
			if(chat.getType().equalsIgnoreCase("image")||chat.getType().equalsIgnoreCase("Video")){
				holder.ivServer.setVisibility(View.VISIBLE);
				
				if(chat.getStatus().equalsIgnoreCase("download")){
				holder.btnServerDwld.setVisibility(View.VISIBLE);
				holder.ivServer.setVisibility(View.GONE);
				holder.btnServerView.setVisibility(View.GONE);
				}
				else if(chat.getStatus().equalsIgnoreCase("view")){
					holder.btnServerDwld.setVisibility(View.GONE);
					holder.btnServerView.setVisibility(View.GONE);
					holder.ivServer.setVisibility(View.VISIBLE);
					holder.tvServer.setVisibility(View.VISIBLE);
					holder.tvServer.setText(chat.getType());

					Bitmap bmp=null;
					try{
						//bmp=Bitmap.createScaledBitmap(decodeSampledBitmapFromResource(TableConstants.SD_CARD_IMAGE_PATH+"/"+chat.getPath(),30,30), 50, 50,true);
						if(chat.getType().equalsIgnoreCase("image")){
							bmp = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(TableConstants.SD_CARD_IMAGE_PATH+"/"+chat.getPath()), width, height);
						}
						
						if(chat.getType().equalsIgnoreCase("video")){
							Utils.log("Video",": "+chat.getPath());
						    Bitmap	bmp1=	ThumbnailUtils.createVideoThumbnail(TableConstants.SD_CARD_VIDEO_PATH+"/"+chat.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
						    bmp = ThumbnailUtils.extractThumbnail(bmp1, width, height);
						}
					}
					catch(Exception e){
						Utils.log("Error in server",":"+e);
						holder.btnServerView.setVisibility(View.VISIBLE);
						//holder.tvServer.setVisibility(View.VISIBLE);
						//holder.tvServer.setText("Image Corrupted");
					}
					catch(OutOfMemoryError e){
						Utils.log("Error in server",":"+e);
						holder.btnServerView.setVisibility(View.VISIBLE);
						bmp=null;
					}
					if(bmp!=null)
					holder.ivServer.setImageBitmap(bmp);
					bmp=null;
					//}
				}
				else if(chat.getStatus().equalsIgnoreCase("downloading")){
					holder.btnServerDwld.setVisibility(View.VISIBLE);
					holder.btnServerDwld.setText("Wait");
					holder.btnServerView.setVisibility(View.GONE);
					
				}
				else{
				holder.btnServerDwld.setVisibility(View.GONE);
				holder.btnServerView.setVisibility(View.GONE);
				}
				//holder.tvServer.setVisibility(View.GONE);
				Utils.log("Path","is view : "+TableConstants.SD_CARD_IMAGE_PATH+"/"+chat.getPath());
				//holder.ivServer.setImageBitmap(decodeSampledBitmapFromResource(TableConstants.SD_CARD_IMAGE_PATH+"/"+chat.getPath(), 30, 30));
				
			}
			else{
				holder.btnServerDwld.setVisibility(View.GONE);
				holder.ivServer.setVisibility(View.GONE);
				holder.tvServer.setVisibility(View.VISIBLE);
				holder.btnServerView.setVisibility(View.GONE);
			}
		}
		if(chat.getSource().equalsIgnoreCase("client")){
			//thumbnails[position] = true;
			holder.tvClient.setText(chat.getMessage());
			holder.tvClientTime.setText(chat.getTime());
			holder.llServer.setVisibility(View.GONE);
			holder.lldate.setVisibility(View.GONE);
			holder.llClient.setVisibility(View.VISIBLE);
			holder.ivServer.setVisibility(View.GONE);
			holder.btnServerDwld.setVisibility(View.GONE);
			holder.btnServerView.setVisibility(View.GONE);
			if(chat.getType().equalsIgnoreCase("image")||chat.getType().equalsIgnoreCase("video")){
				holder.ivClient.setVisibility(View.VISIBLE);
				//holder.tvClient.setText("Image");
				holder.tvClient.setVisibility(View.VISIBLE);
				holder.tvClient.setText(chat.getType());
				//Bitmap bmp=decodeSampledBitmapFromResource(ctx.getResources(), R.drawable.ic_launcher, 30, 30);
				Utils.log("Image Path","Client"+TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+chat.getPath());
				//if(decodeSampledBitmapFromResource(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+chat.getPath(),30,30)!=null){
				Bitmap bmp=null;
				try{
				 //bmp=Bitmap.createScaledBitmap(decodeSampledBitmapFromResource(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+chat.getPath(),30,30), 50, 50,true);
					if(chat.getType().equalsIgnoreCase("image")){
						Utils.log("Image",": "+chat.getPath());
						bmp = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+chat.getPath()), width, height);
					
					}if(chat.getType().equalsIgnoreCase("video")){
						Utils.log("Video",": "+chat.getPath());
					Bitmap	bmp1=	ThumbnailUtils.createVideoThumbnail(TableConstants.SD_CARD_VIDEO_PATH+"/Sent/"+chat.getPath(),
		           			    MediaStore.Images.Thumbnails.MINI_KIND);
					bmp = ThumbnailUtils.extractThumbnail(bmp1, width, height);
					}
				}
				catch(Exception e){
					Utils.log("Error in client",":"+e);
					
				}
				catch(OutOfMemoryError e){
					Utils.log("Error in server",":"+e);
					bmp=null;
				}
				if(bmp!=null){
				//holder.ivClient.setImageBitmap(bmp);
					/*Picasso.with(ctx)
				    .load(new File(TableConstants.SD_CARD_VIDEO_PATH+"/Sent/"+chat.getPath()))
				    .placeholder(R.drawable.chat_back)				   
				    .into(holder.ivClient);*/
				Picasso.with(ctx).load(new File(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+chat.getPath())).into(holder.ivClient);
				bmp=null;
				}
				//}
				//holder.ivClient.setImageBitmap(bmp);
			}
			else{
				holder.ivClient.setVisibility(View.GONE);
				holder.tvClient.setVisibility(View.VISIBLE);
				
			}
		}

			holder.llClient.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.log("position","is "+position);
				Utils.log("msg_type","is "+chat.getType());
			}
		});
		
		holder.llServer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.log("position","is "+position);
				Utils.log("msg_type","is "+chat.getType());
				Utils.log("status","is "+chat.getStatus());
			}
		});
		
		holder.btnServerDwld.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(holder.btnServerDwld.getText().toString().equalsIgnoreCase("Download")){
				// TODO Auto-generated method stub
				Utils.log("image name","is:"+ chat.getPath());
				Utils.log("row id","is:"+ chat.getRow_id());
				image_name=chat.getPath();
				row_id=chat.getRow_id();
				if(image_name.length()>0){
				new DownLoadImageAsyncTask().execute();
				dbAdapter.open();
				dbAdapter.UpdateDownloadStatus(row_id,"Downloading");
				dbAdapter.close();
				iRefreshAdapter.refreshAdapter(position);
				pos=position;
				}
			}
			}
		});
		
		holder.ivServer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(chat.getType().equalsIgnoreCase("Image")){
					iShowImage.showImage(chat.getPath(),"server","image");
					}
					
					if(chat.getType().equalsIgnoreCase("Video")){
						iShowImage.showImage(chat.getPath(),"server","video");
					}
			}
		});	
		
		holder.ivClient.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.log("Client Clicked",":"+chat.getPath());
				if(chat.getType().equalsIgnoreCase("Image")){
					Utils.log("Client Clicked","Image :"+chat.getPath());
					iShowImage.showImage(chat.getPath(),"client","image");
				}
				
				if(chat.getType().equalsIgnoreCase("Video")){
					Utils.log("Client Clicked","Video :"+chat.getPath());
					iShowImage.showImage(chat.getPath(),"client","video");
				}
			}
		});	
		
		
		}
		catch(ArrayIndexOutOfBoundsException e){
			Utils.log("index out of bound",""+e);
		}
		catch(Exception e){
			Utils.log("index out of bound",""+e);
		}
		Utils.log("selected","size"+alSel.size());
		for(int i=0;i<alSel.size();i++){
			if(position==alSel.get(i)){
				holder.llMain.setBackgroundColor(R.color.kesari_color);
			}
			else{
				holder.llMain.setBackgroundResource(R.drawable.llayout_selector);
			}
		}
		
		return convertView;
		
		
	}
	
	public static Bitmap decodeSampledBitmapFromResource(String pathName,
	        int reqWidth, int reqHeight) {
		Utils.log("Path","is: "+pathName);
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    File file= new File(pathName);
	    
	    if(file.exists()){
	    BitmapFactory.decodeFile(pathName);
	    Utils.log("Decoded file","is:"+ pathName );
	    }
	    else{
	    	Utils.log("Decoded file","not exist:" );
	    return null;
	   
	    }
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	   
	    options.inJustDecodeBounds = false;
	    
	    return BitmapFactory.decodeFile(new File(pathName).getAbsolutePath());
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 4;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	class DownLoadImageAsyncTask extends AsyncTask<String, Void, Void>{
		int i=1;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Utils.log("Download asynctask prexecute","starts");
			
			
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Utils.log("Download asynctask doInBackground","starts");
			downLoadFile(image_name, new File(TableConstants.SD_CARD_IMAGE_PATH+"/"+image_name));
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Utils.log("Download asynctask onPostExecute","starts");
			// TODO Auto-generated method stub
			//chatArrayAdapter.add(new Chat(s, "hi", "hi", "hi", "hi", "hi", "client", "hi","true"));
			dbAdapter.open();
			dbAdapter.UpdateDownloadStatus(row_id,"View");
			dbAdapter.close();
			iRefreshAdapter.refreshAdapter(pos);
			
		}
	}
	
	public boolean downLoadFile(String Filename,File LocalFile){
		boolean success=false;
		  FTPClient client = new FTPClient();
          
	        try {
	             
	            /*client.connect(TableConstants.FTP_IP,2121);
	            client.login("Administrator", "S@gar1234#");*/
	        	client.connect("114.79.181.131");
				client.login("cnergee1", "Dvois@123");
	        	
	        	
	         /*  
	          * To check the image use this
	          * 
	          * // ftp://114.79.181.53/ */
	            //	114.79.181.66
	          
	            //client.login("", "");
	            client.setType(FTPClient.TYPE_BINARY);
	            client.changeDirectory("/ChatApp/Image/");
	             client.setPassive(true);
	            client.download(Filename, LocalFile, new MyTransferListener());
	           // long i=client.fileSize(Filename);
	            
	        //	Utils.log("Ftp file ","size: "+i);
	             
	        } catch (Exception e) {
	        	Utils.log("Ftp ","error"+e);
	            e.printStackTrace();
	            try {
	                client.disconnect(true);   
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
		return success;
	}
	
	public class MyTransferListener implements FTPDataTransferListener {
		 
	     public void started() {
	          
	    	 Utils.log("ImageDownLoad started", "now");
	         // Transfer started
	        // Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" Upload Started ...");
	     }

	     public void transferred(int length) {
	    	 Utils.log("ImageDownLoad transferred", "now");
	         // Yet other length bytes has been transferred since the last time this
	         // method was called
	         //Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
	         //System.out.println(" transferred ..." + length);
	     }

	     public void completed() {
	    	 String rslt;
	    	 Utils.log("ImageDownLoad completed", "now");
	    	// new SendToServerAsyncTask().execute();
	         // Transfer completed
	          
	         //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" completed ..." );
	    	 
	    	
	     }

	     public void aborted() {
	          
	    	 Utils.log("ImageDownLoad aborted", "now");
	    	// new UploadImageAsync().cancel(true);
	         // Transfer aborted
	         //Toast.makeText(getBaseContext()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" aborted ..." );
	    	 
	    	 
	    		
	     }

	     public void failed() {
	          
	    	// new UploadImageAsync().cancel(true);
	         // Transfer failed
	        // System.out.println(" failed ..." );
	     }

	 }
}
