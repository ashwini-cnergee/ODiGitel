package com.cnergee.chat.Async;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;

import com.cnergee.mypage.SOAP.SendImageSOAP;
import com.cnergee.mypage.SOAP.SendMessageSOAP;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;




public class UploadImageAsync extends AsyncTask<String, Void, Void> {
	Context context;
	Cursor mCur;
	String row_id="0";
	public String profile_id,group_id="1";
	public UploadImageAsync(){
		
	}
	public UploadImageAsync(Context ctx){
		this.context=ctx;
	}
	DatabaseAdapter dbAdapter;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		super.onPreExecute();
		dbAdapter= new DatabaseAdapter(context);
		dbAdapter.open();
		
		profile_id=dbAdapter.getProfileId();
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
				 dbAdapter.updateuploadstatus(row_id, "uploading");
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dbAdapter.close();
	}
	public class MyTransferListener implements FTPDataTransferListener {
		 
	     public void started() {
	          
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
	         System.out.println(" failed ..." );
	     }

	 }
	
 public void uploadFile(File fileName){
         
         
         FTPClient client = new FTPClient();
       
        try {
             
            client.connect(TableConstants.FTP_IP,2121);
            //client.co
         /*  
          * To check the image use this
          * 
          * // ftp://114.79.181.53/ */
            //	114.79.181.66
            client.login("Administrator", "S@gar1234#");
            //client.login("", "");
         
            client.setType(FTPClient.TYPE_BINARY);
           
            client.changeDirectory("/ChatApp/Image/");
          client.setPassive(true);
             
            client.upload(fileName, new MyTransferListener());
           
             
        } catch (Exception e) {
        	Utils.log("Ftp ","error"+e);
            e.printStackTrace();
            try {
               // client.disconnect(true);   
            } catch (Exception e2) {
                e2.printStackTrace();
            }
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
