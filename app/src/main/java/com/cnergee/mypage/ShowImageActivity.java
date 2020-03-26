package com.cnergee.mypage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.JazzyViewPager;
import com.cnergee.widgets.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;
import cnergee.krishna.ookla.odigitel.R;



public class ShowImageActivity extends FragmentActivity {
	   private MyAdapter mAdapter;
    //   private ViewPager mPager;
	   private static JazzyViewPager mPager;
       ArrayList<String> alImagePaths= new ArrayList<String>();
       ArrayList<String> alMessageType= new ArrayList<String>();
       int totalImages;
       TextView tvImageCount;
      public String img_name;
      public static String source;
      int position;
  
  
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_showimage);
       Intent i=getIntent();
       tvImageCount=(TextView) findViewById(R.id.tvTotalImages);
       img_name=i.getStringExtra("img_name");
       source=i.getStringExtra("source");
       Utils.log("img_name",":"+img_name);
       Utils.log("source",":"+source);
       mAdapter = new MyAdapter(getSupportFragmentManager());
        
           mPager = (JazzyViewPager) findViewById(R.id.jazzy_pager);
         /*  TransitionEffect effect = TransitionEffect.valueOf("FlipHorizontal");
           mPager.setFadeEnabled(true);
           mPager.setTransitionEffect(effect);
           mPager.setPageMargin(30);*/
           DatabaseAdapter dbAdapter= new DatabaseAdapter(ShowImageActivity.this);
           dbAdapter.open();
           Cursor mCursor=dbAdapter.getAllMedia("Image",source);
           Utils.log("Images in Cursor","are :"+mCursor.getCount());
           dbAdapter.close();
           totalImages=mCursor.getCount();
           
           if(mCursor.getCount()>0){
        	   while(mCursor.moveToNext()){
        		   alImagePaths.add(mCursor.getString(mCursor
   						.getColumnIndex(TableConstants.PATH)));
        		    
        		   alMessageType.add(mCursor.getString(mCursor
   						.getColumnIndex(TableConstants.MESSAGE_TYPE)));
        		   
        	   }
           }
           if(alImagePaths.contains(img_name)){
        	   for(int j=0;j<alImagePaths.size();j++){
        		   if(alImagePaths.get(j).equalsIgnoreCase(img_name)){
        			   Utils.log("position in for loop","is: "+j);
        			   position=j;
        		   }
        		  
        	   }
           }
           tvImageCount.setText(" Image " + (position+1) + " of " + totalImages + "  ");
           Utils.log("name for item ","is :"+img_name);
           Utils.log("position for item ","is :"+position);
           mAdapter.setContent(totalImages, alImagePaths, alMessageType);
           
           setupJazziness(TransitionEffect.CubeIn);
           //mPager.setAdapter(mAdapter);
           mPager.setCurrentItem(position,true);
           
           mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tvImageCount.setText(" Image " + (arg0+1) + " of " + totalImages + "  ");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
     //  getMenuInflater().inflate(R.menu.main, menu);
	   
       return true;
   }
   
   public static class MyAdapter extends FragmentPagerAdapter {
	   ArrayList<String> alImagePaths= new ArrayList<String>();
       ArrayList<String> alMessageType= new ArrayList<String>();
       int totalImages;
      
	   
       public MyAdapter(FragmentManager fm) {
           super(fm);
       }
       
       public void setContent(int totalImages,ArrayList<String>ImagePath,ArrayList<String>ImageType){
    	   alImagePaths= ImagePath;
    	   alMessageType=ImageType;
    	   this.totalImages=totalImages;
       }
       @Override
       public int getCount() {
           return totalImages;
       }

       @Override
       public Fragment getItem(int position) {
             	ImageFragment fragment;
    	   	if(ShowImageActivity.source.equalsIgnoreCase("server")){
    	   		fragment=new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   		mPager.setObjectForPosition(fragment, position);
    	   		//return fragment;        	   
    	   		return new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   	}
    	   	else{
    	   		fragment=new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   		mPager.setObjectForPosition(fragment, position);
    	   		//return fragment;    	   	 
    	   		return new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+alImagePaths.get(position));
    	   	}
    	  
           }
       
      /* @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	// TODO Auto-generated method stub
    	   container.removeView(mPager.findViewFromObject(position));
    }*/
       
       //@Override
    public Object instantiateItem(ViewGroup container, int position) {
    	// TODO Auto-generated method stub
    	  Fragment frag = (Fragment) super.instantiateItem(container, position);
    	   mPager.setObjectForPosition(frag, position);
    	   return frag;
    	  /*
    	   	if(ShowImageActivity.source.equalsIgnoreCase("server")){
    	   		ImageFragment fragment;
    	   	
    	   		fragment=new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   		
    	   		mPager.setObjectForPosition(fragment, position);
    	   		return fragment;
        	  // return new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   	}
    	   	else{
    	   		ImageFragment fragment;
    	   		fragment=new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/"+alImagePaths.get(position));
    	   		mPager.setObjectForPosition(fragment, position);
    	   		//container.addView(mPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	   		
    	   		return fragment;
    	   	// return new ImageFragment(TableConstants.SD_CARD_IMAGE_PATH+"/Sent/"+alImagePaths.get(position));
    	   	}*/
    	  
    }
       }
   @Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	this.finish();
	Intent backIntent =new Intent(this,ChatActivity.class);
	backIntent.putExtra("callby","viewimage");
	startActivity(backIntent);
}
   
	private void setupJazziness(TransitionEffect effect) {
		//mPager = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		mPager.setTransitionEffect(effect);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(60);
	}
}
