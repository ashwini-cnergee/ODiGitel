package com.cnergee.mypage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cnergee.mypage.utils.Utils;

import java.io.File;

import cnergee.krishna.ookla.odigitel.R;

public class ShowVideoActivity extends Activity  {
	VideoView vvVideoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_video);
		Intent i = getIntent();
		String videoPath=i.getStringExtra("videopath");
		vvVideoView=(VideoView) findViewById(R.id.videoView);
		File file = new File(videoPath);
		Uri mUri = Uri.fromFile(file);
		vvVideoView.setVideoURI(mUri);
		vvVideoView.setMediaController(new MediaController(this));
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vvVideoView.start();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		vvVideoView.stopPlayback();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		Intent chatIntent= new Intent(ShowVideoActivity.this,ChatActivity.class);
		chatIntent.putExtra("callby", "showvideo");
		startActivity(chatIntent);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
      
       case android.R.id.home:
           // your title was clicked!
       		ShowVideoActivity.this.finish();
			Intent chatIntent = new Intent(ShowVideoActivity.this,ChatActivity.class);
			chatIntent.putExtra("callby", "showvideo");
			startActivity(chatIntent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
       	Utils.log("Homebutton"," clicked");
       	  return true;
            
        default:
            return super.onOptionsItemSelected(item);
    }
	}
}
