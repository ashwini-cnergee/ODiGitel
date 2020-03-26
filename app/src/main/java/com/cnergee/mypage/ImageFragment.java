package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cnergee.mypage.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import cnergee.krishna.ookla.odigitel.R;

public class ImageFragment extends Fragment {
    private String imagePath;
   
	public ImageFragment(){

	}
    
    @SuppressLint("ValidFragment")
	public ImageFragment(String imageResourceId) {
        this.imagePath = imageResourceId;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Test", "hello");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
       /* Bitmap bmp=ShrinkBitmap(imagePath, 300, 300);
        Utils.log("Image Fragment","is: "+imagePath);
      //  imageView.setImageResource(imagePath);
        imageView.setImageBitmap(bmp);*/
      
        Picasso.with(getActivity()).load(new File(imagePath)).into(imageView, new Callback() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Utils.log("imageview","success");
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				Utils.log("imageview","error");
			}
		});
        
     /*   Picasso picasso = new Picasso.Builder(getActivity()).listener(
                new Listener() {
                  
					@Override
					public void onImageLoadFailed(Picasso arg0, Uri arg1,
							Exception arg2) {
						// TODO Auto-generated method stub
						
					}

                }).debugging(true).build();
        picasso.load(new File(imagePath)).into(imageView);
*/
        
       // new BitmapWorkerTask(imageView).execute(0);
       // bmp=null;
        return view;
    }
    
    
    private Bitmap ShrinkBitmap(String file, int width, int height) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
		int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
		if (heightRatio > 1 || widthRatio > 1)
		{
			if (heightRatio > widthRatio)
			{
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}
    
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private int data = 0;

	    public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	        data = params[0];
	        //return decodeSampledBitmapFromResource(getResources(), data, 1000, 1000);
	        Bitmap bmp=ShrinkBitmap(imagePath, 300, 300);
	        Utils.log("Image Fragment","is: "+imagePath);
	      //  imageView.setImageResource(imagePath);
	    
	        return bmp;
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	                
	            }
	        }
	    }
	}
    
    
}
