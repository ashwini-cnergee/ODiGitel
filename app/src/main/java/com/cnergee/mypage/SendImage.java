package com.cnergee.mypage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.utils.Utils;
import cnergee.krishna.ookla.odigitel.R;


public class SendImage extends Activity {
	Button btnSend,btnCancel;
	ImageView ivGalleryImage;
  
    String mSelectedImagePath;
    DatabaseAdapter dbAdapter;
    Bitmap bitmap=null;
    String imageStamp;
    ProgressDialog prgDialog;
    String str_share="";
    public String profile_id,group_id="1";
    private static final int SELECT_IMAGE = 1,REQUEST_CAMERA=2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkimage);
        btnSend=(Button) findViewById(R.id.btnSend);
        btnCancel=(Button) findViewById(R.id.btnCancel);
		ivGalleryImage=(ImageView) findViewById(R.id.ivSet);
		dbAdapter=new DatabaseAdapter(SendImage.this);
		dbAdapter.open();
		profile_id=dbAdapter.getProfileId();
		String grp_id=dbAdapter.getGroupId();
		if(grp_id.length()>0){
			group_id=grp_id;
		}
		dbAdapter.close();
		Intent i= getIntent();
		str_share=i.getStringExtra("share");
		Utils.log("sharing",":"+str_share);
		if(str_share.equalsIgnoreCase("CameraImage")){
        
			ImageFromCamera();
		}
		if(str_share.equalsIgnoreCase("Image")){
			imageFromGallery();
	       // VideoFromGallery();
		}
		
        //get the received intent
        btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bitmap!=null){
					prgDialog= new ProgressDialog(getApplicationContext());
					String nowTime = "";
					String nowDate = "";
					
					Calendar now = Calendar.getInstance();
					String min = "";
					if(now.get(Calendar.MINUTE)<10)
						min="0"+now.get(Calendar.MINUTE);
					else
						min=""+now.get(Calendar.MINUTE);
					int a = now.get(Calendar.AM_PM);
					if(a == Calendar.AM)
						nowTime= now.get(Calendar.HOUR)+":"+min+" AM";
					if(a == Calendar.PM)
						nowTime= now.get(Calendar.HOUR)+":"+min+" PM";
					nowDate=now.get(Calendar.DAY_OF_MONTH)+"/"+(now.get(Calendar.MONTH)+1)+"/"+now.get(Calendar.YEAR);
					Utils.log("now","date :"+nowDate);
					imageStamp=now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH)+1)+"_"+now.get(Calendar.DAY_OF_MONTH)+
							   "_"+now.get(Calendar.HOUR)+"_"+now.get(Calendar.MINUTE)+"_"+now.get(Calendar.SECOND);
					long i=-1;
					Utils.log("nowTime",""+nowTime);
					Utils.log("nowDate",""+nowDate);
					Utils.log("imageStamp",""+imageStamp);
					dbAdapter.open();
					if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage")){
					i=dbAdapter.insertMessgae("no_message", "Image", nowDate, nowTime, "client", imageStamp+".jpg", "View", "false", group_id, "no", "nourl","0","no_uploaded","me");
					}
					if(str_share.equalsIgnoreCase("video")){
						i=dbAdapter.insertMessgae("no_message", str_share, nowDate, nowTime, "client", imageStamp+".mp4", "View", "false", group_id, "no", "nourl","0","no_uploaded","me");
						}
					dbAdapter.close();
					if(i!=-1)
					new SyncImageAsyncTask().execute();
				}
			}
		});
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SendImage.this.finish();
				Intent intent = new Intent(SendImage.this, ChatActivity.class);
				intent.putExtra("callby", "cancel");
				startActivity(intent);
			}
		});
    }

    /*** this function opens the gallery and gets the image ***/
    public void imageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_IMAGE);
    }
 
    /* After opening gallery control comes here */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case SELECT_IMAGE:
                	
                	Uri selectedImageUri = data.getData();
                	 if (Build.VERSION.SDK_INT < 19) {
                    mSelectedImagePath = getPath(selectedImageUri);
                   // System.out.println("mSelectedImagePath : " + mSelectedImagePath);
                   
                  try {
					
					
					 Utils.log("selected file","is: "+mSelectedImagePath);
					 Bitmap bt=null;
					 if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage")){
						 bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
						 bt  =Bitmap.createScaledBitmap(bitmap, 300, 300, false);  
					 }
					 if(str_share.equalsIgnoreCase("video")){
						 bitmap = ThumbnailUtils.createVideoThumbnail(mSelectedImagePath,
			           			    Images.Thumbnails.MINI_KIND);
						 bt = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
					 }
                 //  bt.compress(CompressFormat.JPEG, 70, fos);
                   ivGalleryImage.setImageBitmap(bt);
                  }  catch (Exception e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   					Utils.log("Exception","android "+e);
   				}
                  catch(OutOfMemoryError e){
                	  Utils.log("Out of memory error","android "+e);
	
                  }
                	 }
                	 else{
                		 ParcelFileDescriptor parcelFileDescriptor;
                         try {
                             parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                             FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                             Bitmap bt=null;
        					 if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage")){
        						 bitmap  = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        						 bt  =Bitmap.createScaledBitmap(bitmap, 300, 300, false);  
        						 mSelectedImagePath=selectedImageUri.getPath();
        						 
        					 }
        					 if(str_share.equalsIgnoreCase("video")){
        						 bitmap = ThumbnailUtils.createVideoThumbnail(mSelectedImagePath,
        			           			    Images.Thumbnails.MINI_KIND);
        						 bt = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
        					 }
                         //  bt.compress(CompressFormat.JPEG, 70, fos);
                           ivGalleryImage.setImageBitmap(bt);
                         }
                         catch (Exception e) {
            					// TODO Auto-generated catch block
            					e.printStackTrace();
            					Utils.log("Exception","android "+e);
            				}
                           catch(OutOfMemoryError e){
                         	  Utils.log("Out of memory error","android "+e);
         	
                           }
                	 }
                 
                    break;
                    
                case REQUEST_CAMERA:
                   // mSelectedImagePath = getPath(tempUri);
                	
                    //System.out.println("mSelectedImagePath : " + mSelectedImagePath);
                   // Uri selectedImageUri = data.getData();
               //  String   selectedImagePath =mSelectedImagePath;
                    bitmap = (Bitmap) data.getExtras().get("data"); 
                    Uri tempUri = getImageUri(SendImage.this, bitmap);
                    mSelectedImagePath = getPath(tempUri);
                 
                  try {
					//FileOutputStream fos= new FileOutputStream(mSelectedImagePath);
					
					 Utils.log("selected file","is: "+mSelectedImagePath);
					 Bitmap bt=null;
					/* if(str_share.equalsIgnoreCase("image")){
						// bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
						 bt  =Bitmap.createScaledBitmap(bitmap, 100, 100, false);  
					 }
					 if(str_share.equalsIgnoreCase("video")){
						 bitmap = ThumbnailUtils.createVideoThumbnail(mSelectedImagePath,
			           			    MediaStore.Images.Thumbnails.MINI_KIND);
						 bt = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
					 }*/
					 
					 bt = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                 //  bt.compress(CompressFormat.JPEG, 70, fos);
                   ivGalleryImage.setImageBitmap(bt);
                  }  catch (Exception e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   					Utils.log("Exception","android "+e);
   				}
                  catch(OutOfMemoryError e){
                	  Utils.log("Out of memory error","android "+e);
	
                  }
                   /* try {
                        File sd = Environment.getExternalStorageDirectory();
                        if (sd.canWrite()) {
                            System.out.println("(sd.canWrite()) = " + (sd.canWrite()));
                           // String destinationImagePath= "/file.jpg"; Environment.getExternalStorageDirectory()+"/ChatApp/Image"  // this is the destination image path.
                            String destinationImagePath= "/ChatApp/Image/file.jpg";   // this is the destination image path.
                            File source = new File(mSelectedImagePath );
                            File destination= new File(sd, destinationImagePath);
                            if (source.exists()) {
                                FileChannel src = new FileInputStream(source).getChannel();
                                FileChannel dst = new FileOutputStream(destination).getChannel();
                                dst.transferFrom(src, 0, src.size());       // copy the first file to second.....
                                src.close();
                                dst.close();
                                Toast.makeText(getApplicationContext(), "Check the copy of the image in the same path as the gallery image. File is name file.jpg", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "SDCARD Not writable.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e) {
                        System.out.println("Error :" + e.getMessage());
                    }*/
                  
                    break;
          }
        }
    }
 
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(CompressFormat.PNG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    
    public String getPath(Uri uri) {
        String[] projection = { Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public class SyncImageAsyncTask extends AsyncTask<String, Void, Void>{
    	Boolean status=false;
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    	}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			//***START*********OLD CODE FOR COPYING ***************************
			/*try {
                File sd = Environment.getExternalStorageDirectory();
                if (sd.canWrite()) {
                    System.out.println("(sd.canWrite()) = " + (sd.canWrite()));
                   // String destinationImagePath= "/file.jpg"; Environment.getExternalStorageDirectory()+"/ChatApp/Image"  // this is the destination image path.
                    String destinationImagePath= imageStamp+".jpg";   // this is the destination image path.
                    File source = new File(mSelectedImagePath );
                   
                    File destination= new File(sd+"/ChatApp/Image/Sent", destinationImagePath);
                    if (source.exists()) {
                        FileChannel src = new FileInputStream(source).getChannel();
                        
                        FileChannel dst = new FileOutputStream(destination).getChannel();
                        dst.transferFrom(src, 0, src.size());       // copy the first file to second.....
                        src.close();
                        dst.close();
                        status=true;
                       //Toast.makeText(getApplicationContext(), "Check the copy of the image in the same path as the gallery image. File is name file.jpg", Toast.LENGTH_LONG).show();
                    }
                }else{
                	status=false;
                    //Toast.makeText(getApplicationContext(), "SDCARD Not writable.", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e) {
            	status=true;
                System.out.println("Error :" + e.getMessage());
            }*/
			//***END*********OLD CODE FOR COPYING ***************************
			 String destinationImagePath="";
			if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage"))
			  destinationImagePath= imageStamp+".jpg";
			if(str_share.equalsIgnoreCase("video"))
				  destinationImagePath= imageStamp+".mp4";
			 File sd = Environment.getExternalStorageDirectory();
			 File destination=null;
			 if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage"))
				 destination= new File(sd+"/ChatApp/Image/Sent", destinationImagePath);
			 if(str_share.equalsIgnoreCase("video"))
				 destination= new File(sd+"/ChatApp/Video/Sent", destinationImagePath);
			File source = new File(mSelectedImagePath );
			try {
				 if(str_share.equalsIgnoreCase("image")||str_share.equalsIgnoreCase("CameraImage"))
				copyDirectory(source, destination);
				 if(str_share.equalsIgnoreCase("video"))
					 copyVideo();
				 status=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				 status=false;
				e.printStackTrace();
			}
			
			return null;
		}
    	@Override
    	protected void onPostExecute(Void result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		if(status){
    			SendImage.this.finish();
				Intent intent = new Intent(SendImage.this, ChatActivity.class);
				intent.putExtra("callby", str_share);
				startActivity(intent);
    		}else{
    			Toast.makeText(getApplicationContext(), "File is corrupted failed to send", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    public void copyDirectory(File sourceLocation , File targetLocation)
    		throws IOException {

    		    if (sourceLocation.isDirectory()) {
    		        if (!targetLocation.exists() && !targetLocation.mkdirs()) {
    		            throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
    		        }

    		        String[] children = sourceLocation.list();
    		        for (int i=0; i<children.length; i++) {
    		            copyDirectory(new File(sourceLocation, children[i]),
    		                    new File(targetLocation, children[i]));
    		        }
    		    } else {

    		        // make sure the directory we plan to store the recording in exists
    		        File directory = targetLocation.getParentFile();
    		        if (directory != null && !directory.exists() && !directory.mkdirs()) {
    		            throw new IOException("Cannot create dir " + directory.getAbsolutePath());
    		        }

    		       
    		//   	 bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
                 Bitmap bt=Bitmap.createScaledBitmap(bitmap, 300, 300, false);  
                // InputStream in = new FileInputStream(sourceLocation);
                 FileOutputStream out = new FileOutputStream(targetLocation);
                 bt.compress(CompressFormat.PNG, 100, out);
                
    		       out.flush();
    		       out.close();

    		        // Copy the bits from instream to outstream
    		      /*  byte[] buf = new byte[1024];
    		        int len;
    		        while ((len = in.read(buf)) > 0) {
    		            out.write(buf, 0, len);
    		        }
    		       
    		        in.close();
    		        out.close();*/
    		    }
    		}
    public void copyVideo(){
    	try{
			  File sd = Environment.getExternalStorageDirectory();
              if (sd.canWrite()) {
                 // System.out.println("(sd.canWrite()) = " + (sd.canWrite()));
                 // String destinationImagePath= "/file.jpg"; Environment.getExternalStorageDirectory()+"/ChatApp/Image"  // this is the destination image path.
                  String destinationImagePath= imageStamp+".mp4";   // this is the destination image path.
                  File source = new File(mSelectedImagePath);
                 
                  File destination= new File(sd+"/ChatApp/Video/Sent", destinationImagePath);
                  if (source.exists()) {
                      FileChannel src = new FileInputStream(source).getChannel();
                      
                      FileChannel dst = new FileOutputStream(destination).getChannel();
                      dst.transferFrom(src, 0, src.size());       // copy the first file to second.....
                      src.close();
                      dst.close();
                     
                     //Toast.makeText(getApplicationContext(), "Check the copy of the image in the same path as the gallery image. File is name file.jpg", Toast.LENGTH_LONG).show();
                  }
              }else{
              
                  //Toast.makeText(getApplicationContext(), "SDCARD Not writable.", Toast.LENGTH_LONG).show();
              }
          }catch (Exception e) {
        	 // System.out.println("Error :" + e.getMessage());
          }
    }
    
    public void VideoFromGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_IMAGE);
    }
    
    public void ImageFromCamera() {
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    
   
}
