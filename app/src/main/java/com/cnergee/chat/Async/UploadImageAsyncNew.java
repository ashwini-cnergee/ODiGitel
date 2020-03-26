package com.cnergee.chat.Async;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;

import com.cnergee.mypage.SOAP.SendImageSOAP;
import com.cnergee.mypage.SOAP.SendMessageSOAP;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import cnergee.krishna.ookla.odigitel.R;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;





public class UploadImageAsyncNew extends AsyncTask<String, Void, Void> {
	Context context;
	Cursor mCur;
	String row_id="0";
	FTPClient client;
	public String profile_id,group_id="1";
	SharedPreferences sharedPreferences_;
	public UploadImageAsyncNew(){
		
	}
	public UploadImageAsyncNew(Context ctx){
		this.context=ctx;
	}
DatabaseAdapter dbAdapter;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		super.onPreExecute();
		dbAdapter= new DatabaseAdapter(context);
		dbAdapter.open();
		
		String sharedPreferences_name =context. getString(R.string.shared_preferences_name);
		sharedPreferences_ =context. getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		Utils utils= new Utils();
		utils.setSharedPreferences(sharedPreferences_);
		
		profile_id=utils.getMemberId();
		String grp_id=dbAdapter.getGroupId();
		if(grp_id.length()>0){
			group_id=grp_id;
		}
	
		mCur=dbAdapter.getImagesToSend("no_uploaded");
		Utils.log("Images","to Upload"+mCur.getCount());
		
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		   
		client = new FTPClient();
      
     
            
           try {
        	  /* client.connect(TableConstants.FTP_IP,2121);
			  client.login("Administrator", "S@gar1234#");*/
			  Utils.log("FTP IP", ":"+Utils.FTP_IP);
			  Utils.log("FTP_USERNAME",":"+Utils.FTP_USERNAME);
			  Utils.log("FTP_PASSWORD",":"+Utils.FTP_PASSWORD);
			  client.connect(Utils.FTP_IP);
				client.login(Utils.FTP_USERNAME, Utils.FTP_PASSWORD);
	           //client.login("", "");
	        
	           client.setType(FTPClient.TYPE_BINARY);
	          
	           client.changeDirectory("/MyApp/Images/");
	         client.setPassive(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           //client.co
        /*  
         * To check the image use this
         * 
         * // ftp://114.79.181.53/ */
           //	114.79.181.66
         
		if(mCur.getCount()>0){
			while(mCur.moveToNext()){
				row_id=mCur.getString(mCur
						.getColumnIndex(TableConstants.ROW_ID));
				
				String message=mCur.getString(mCur
						.getColumnIndex(TableConstants.MESSAGE));
								
				String type=mCur.getString(mCur
						.getColumnIndex(TableConstants.MESSAGE_TYPE));
				
				String path=mCur.getString(mCur
						.getColumnIndex(TableConstants.PATH));
				
				 File f = new File(Environment.getExternalStorageDirectory()+"/ChatApp/Image/Sent/"+path);
				 uploadFile(f);
				 
			}
		}
		try {
			client.disconnect(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dbAdapter.close();
		/*try {
			client.disconnect(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public class MyTransferListener implements FTPDataTransferListener {
		 
	     public void started() {
	          
	    	 dbAdapter.open();
			 dbAdapter.updateuploadstatus(row_id, "uploading");
			 dbAdapter.close();
	    	 Utils.log("started", "now");
	         // Transfer started
	        // Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" Upload Started ...");
	     }

	     public void transferred(int length) {
	    	 Utils.log("transferred", "now");
	         // Yet other length bytes has been transferred since the last time this
	         // method was called
	         //Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
	         //System.out.println(" transferred ..." + length);
	     }

	     public void completed() {
	    	 String rslt;
	    	 Utils.log("completed", "now");
	    	// new SendToServerAsyncTask().execute();
	         // Transfer completed
	          
	         //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" completed ..." );
	    	 
	    	 dbAdapter.open();
	    	long i= dbAdapter.updateuploadstatus(row_id,"uploaded");
	    	 dbAdapter.close();
	    	 Utils.log("Image uploaded"," successfully"+i);
	    	 SendImageSOAP sendImageSOAP= new SendImageSOAP(context.getString(R.string.WSDL_TARGET_NAMESPACE), context.getString(R.string.SOAP_URL),context.getString(R.string.METHOD_SEND_SERVER_MESSAGE));
				try {
					rslt=sendImageSOAP.sendServerImage(profile_id,context);
					
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					Utils.log("SendImageSOAP SocketTimeoutException","is: "+e);
					rslt="error";
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					Utils.log("SendImageSOAP SocketException","is: "+e);
					rslt="error";
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Utils.log("SendImageSOAP Exception","is: "+e);
					rslt="error";
					e.printStackTrace();
				}
				Utils.log("SendImageSOAP doInBackground","finished");
	     }

	     public void aborted() {
	          
	    	 Utils.log("aborted", "now");
	    	 new UploadImageAsync().cancel(true);
	         // Transfer aborted
	         //Toast.makeText(getBaseContext()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
	         //System.out.println(" aborted ..." );
	    	 
	    	 
	    		
	     }

	     public void failed() {
	          
	    	 new UploadImageAsync().cancel(true);
	         // Transfer failed
	        // System.out.println(" failed ..." );
	     }

	 }
	
 public void uploadFile(File fileName){
         
      
             
            try {
				client.upload(fileName, new MyTransferListener());
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FTPIllegalReplyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FTPDataTransferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FTPAbortedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
             
       
         
    }

	public class SendToServerAsyncTask extends AsyncTask<String, Void, Void>{
		public String rslt;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Utils.log("onPreExecute"," start");
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Utils.log("SendToServerAsyncTask doInBackground","started");
			
			SendMessageSOAP sendMessageSOAP= new SendMessageSOAP(context.getString(R.string.WSDL_TARGET_NAMESPACE), context.getString(R.string.SOAP_URL),context.getString(R.string.METHOD_SEND_SERVER_MESSAGE));
			try {
				rslt=sendMessageSOAP.sendServerMessage(profile_id,context,group_id);
				
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				Utils.log("SendToServerAsyncTask SocketTimeoutException","is: "+e);
				rslt="error";
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				Utils.log("SendToServerAsyncTask SocketException","is: "+e);
				rslt="error";
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Utils.log("SendToServerAsyncTask Exception","is: "+e);
				rslt="error";
				e.printStackTrace();
			}
			Utils.log("SendToServerAsyncTask doInBackground","finished");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Utils.log("SendToServerAsyncTask onPostExecute"," start");
		}
		
	}
}
