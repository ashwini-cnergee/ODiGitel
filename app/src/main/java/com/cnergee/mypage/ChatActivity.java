package com.cnergee.mypage;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import all.interface_.IRefreshAdapter;
import all.interface_.IViewImage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.chat.Async.UploadImageAsync;
import com.cnergee.chat.Async.UploadImageAsyncCommons;
import com.cnergee.chat.Async.UploadImageAsyncNew;
import com.cnergee.chat.Async.UploadVideoAsync;
import com.cnergee.mypage.SOAP.ChatRequestSOAP;
import com.cnergee.mypage.SOAP.SendMessageSOAP;
import com.cnergee.mypage.adapter.ChatArrayAdapter;
import com.cnergee.mypage.database.DatabaseAdapter;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Chat;
import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.sys.AlarmBroadcastReceiver1;
import com.cnergee.mypage.sys.CheckChatResponse;
import com.cnergee.mypage.sys.DateChangeBroadcastReceiver;
import com.cnergee.mypage.sys.ServerCommunicationService;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;

@SuppressLint("ResourceAsColor")
public class ChatActivity extends Activity implements IViewImage,IRefreshAdapter,OnScrollListener {
	EditText etChatText;
	Button btnSend,btnLoadMore;
	ListView lvChatMessgage;
	ArrayList<Chat> alChat;
	ChatArrayAdapter chatArrayAdapter;
	DatabaseAdapter dbAdapter;
	Cursor cur_Chat;
	Calendar cal;

	SharedPreferences sharedPreferences;
	//LocalBroadcastManager localBroadcast;
	ImageView tvAttach;
	Utils utils;
	UploadImageAsync uploadImageAsync;
	UploadImageAsyncNew uploadImageAsyncNew;
	UploadImageAsyncCommons uploadImageAsyncCommons;
	UploadVideoAsync uploadVideoAsync;
	String text="";
	static int number_rows=50;
	
	//private Boolean flag = true;
	int count_Rows=0;
	int last_value;
	int pos;
	public String profile_id,group_id="1";
	SharedPreferences sharedPreferences_;
	String reg_id="";

	  public static ArrayList<Integer> alSelected;
	  private static final int SELECT_IMAGE = 1;
	  private static boolean start_async=true;
	  
	  ActionMode mActionMode =null;
	  ArrayList<Chat> Action_Chat= new ArrayList<Chat>();
	  public static ArrayList<String> Action_Row_ID= new ArrayList<String>();
	  Button btnStartChat;
	  LinearLayout llStartChat,llWaitChat;
	  //GoogleCloudMessaging gcm;
	  public static PendingIntent  penIntentOneAlarm;
	  public static PendingIntent penIntentRepeatAlarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		alSelected= new ArrayList<Integer>();
		
		setContentView(R.layout.activity_chat);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		/*getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
		//getSupportActionBar().hide();
		initializeControls();
		lvChatMessgage.setOnScrollListener(ChatActivity.this);
		utils= new Utils();
		cal= Calendar.getInstance();
		dbAdapter= new DatabaseAdapter(getApplicationContext());
		number_rows=50;
		
		String sharedPreferences_name = getString(R.string.shared_preferences_name);
		sharedPreferences_ = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences_);
		
	/*	SharedPreferences sharedPreferences = this.getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		SharedPreferences.Editor editor= sharedPreferences.edit();
		editor.putBoolean("chat_status", false);
		editor.commit();*/
		/*if(sharedPreferences_.getBoolean("wait_status", false)){
			llWaitChat.setVisibility(View.VISIBLE);
			llStartChat.setVisibility(View.GONE);
		
		}
		else{
			if(sharedPreferences_.getBoolean("chat_status", false)){
				llWaitChat.setVisibility(View.GONE);
				llStartChat.setVisibility(View.GONE);
			}
			else{
				llWaitChat.setVisibility(View.GONE);
				llStartChat.setVisibility(View.VISIBLE);
			}
		}*/
		
		/*if(sharedPreferences_.getBoolean("chat_status", false)){
			Utils.log("Chat Status","true");
			
			llStartChat.setVisibility(View.GONE);
			
		}
		else{
			if(sharedPreferences_.getBoolean("wait_status", false)){
				Utils.log("Chat Status","false");
				Utils.log("wait Status","true");
				llWaitChat.setVisibility(View.VISIBLE);
				llStartChat.setVisibility(View.GONE);
			
			}
			else{
				Utils.log("Chat Status","false");
				Utils.log("wait Status","false");
				llWaitChat.setVisibility(View.GONE);
				llStartChat.setVisibility(View.GONE);
			}
			//llWaitChat.setVisibility(View.GONE);
			//llStartChat.setVisibility(View.VISIBLE);
		}*/

		if(sharedPreferences_.getBoolean("chat_status", false)){
			llWaitChat.setVisibility(View.GONE);
			llStartChat.setVisibility(View.GONE);
		}
		else{
			if(sharedPreferences_.getBoolean("wait_status", false)){
				llWaitChat.setVisibility(View.VISIBLE);
				llStartChat.setVisibility(View.GONE);
			}
			else{
				llWaitChat.setVisibility(View.GONE);
				llStartChat.setVisibility(View.VISIBLE);
			}
		}

		dbAdapter.open();
		profile_id= (utils.getMemberId());
		String grp_id=dbAdapter.getGroupId();
		if(grp_id.length()>0){
			group_id=grp_id;
		}
		dbAdapter.close();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new ChatBindAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			new ChatBindAsyncTask().execute();
		
		btnSend.setOnClickListener(myClickListener);
		tvAttach.setOnClickListener(myClickListener);
		btnLoadMore.setOnClickListener(myClickListener);
		uploadImageAsync= new UploadImageAsync(ChatActivity.this);
		uploadVideoAsync= new UploadVideoAsync(ChatActivity.this);
		uploadImageAsyncNew=new UploadImageAsyncNew(ChatActivity.this);
		uploadImageAsyncCommons= new UploadImageAsyncCommons(ChatActivity.this);
		
		Intent i= getIntent();
		String s=i.getStringExtra("callby");
		//System.out.print("CheckIntent"+s);
		Utils.log("Check","Intent"+s);
		Log.e("Check","Intent"+s);
		if(s!=null){
		if(s.equalsIgnoreCase("Image")||s.equalsIgnoreCase("Video")||s.equalsIgnoreCase("CameraImage")){
			Utils.log("Run Async","to upload image");
			//uploadImageAsync.execute();
			uploadImageAsyncNew.execute();
			uploadVideoAsync.execute();
			//uploadImageAsyncCommons.execute();
		}}

		Intent intentSetRepeatAlarm12 = new Intent(ChatActivity.this,AlarmBroadcastReceiver1.class);
		
		penIntentRepeatAlarm= PendingIntent.getBroadcast(ChatActivity.this, 0, intentSetRepeatAlarm12, 0);
		Calendar cal = Calendar.getInstance();
		AlarmManager Repeatalarm = (AlarmManager)ChatActivity.this.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
		// Start every 1mon..
		Repeatalarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 6*1000, penIntentRepeatAlarm);
		
		new UploadImageAsync(ChatActivity.this);
		sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_app_setting), MODE_PRIVATE);
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("speedExceeded"));
		LocalBroadcastManager.getInstance(this).registerReceiver(mOnlineBroadcast, new IntentFilter("chat_online"));
		LocalBroadcastManager.getInstance(this).registerReceiver(mChatEnd, new IntentFilter("chat_end"));

		/*
		 * 	These checks apps runs for first time and starts alarms
		 */

		if(sharedPreferences.getBoolean("start_chat_service",true)){
			Utils.log("Chat ","service starts");
			
			/*Intent intentSetRepeatAlarm = new Intent(ChatActivity.this,AlarmBroadcastReceiver1.class);
			
			penIntentRepeatAlarm= PendingIntent.getBroadcast(this, 0, intentSetRepeatAlarm, 0);
			Calendar cal = Calendar.getInstance();
			AlarmManager Repeatalarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
			// Start every 1mon..
			Repeatalarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*1000, penIntentRepeatAlarm);*/
			
			int INTERVAL_DAY1 = 24 * 60 * 60 * 1000 ;
			Intent intentSetOneAlarm = new Intent(ChatActivity.this,DateChangeBroadcastReceiver.class);
			
			penIntentOneAlarm = PendingIntent.getBroadcast(this, 0, intentSetOneAlarm, 0);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 00);
			 calendar.set(Calendar.SECOND, 00);
			long triggerMillis = calendar.getTimeInMillis();

			if(sharedPreferences.getBoolean("date_check",true)){
				
			}else{
			   if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
					triggerMillis = calendar.getTimeInMillis() + INTERVAL_DAY1;
				}
			}

			AlarmManager oneAlarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);24*60*60*1000
			// Start every 1mon..
			
			oneAlarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, penIntentOneAlarm);
			
			Editor edit= sharedPreferences.edit();
			edit.putBoolean("date_check", false);
			edit.commit();
		}

		lvChatMessgage.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				Chat chat=(Chat)arg0.getItemAtPosition(arg2);
			Utils.log("ListView clicked","Id: "+chat.getRow_id());
			LinearLayout ll=(LinearLayout) arg1.findViewById(R.id.llMainChat);
			Utils.log("ListView clicked","Position: "+arg2);
			//alSelected.add(arg2);
			
			if(mActionMode!=null){
				Utils.log("New Row","Added");
				if(Action_Row_ID.size()>0){
					Utils.log("Action_Row_ID","is:"+Action_Row_ID.size());
				//for(int i=0;i<Action_Row_ID.size();i++){
					if(Action_Row_ID.contains(String.valueOf(arg2))){
					//	Utils.log("Removing Item","at Position:"+Action_Row_ID.get(i));
						Utils.log("Removing Item","at Value:"+arg2);
						Action_Chat.remove(chat);
						Action_Row_ID.remove(String.valueOf(arg2));
						//ll.setBackgroundColor(android.R.color.white);
					}
					else{
						//Utils.log("Adding Item","at Position:"+i);
						Utils.log("Adding Item","at Value:"+arg2);
						Action_Chat.add(chat);
						Action_Row_ID.add(String.valueOf(arg2));
						//ll.setBackgroundColor(R.color.kesari_color);
					}
				}
				else{
					Utils.log("Action_Row_ID","is:"+Action_Row_ID.size());
					Action_Chat.add(chat);
					Action_Row_ID.add(String.valueOf(arg2));
					//ll.setBackgroundColor(R.color.kesari_color);
				}
			}
			else{
				Utils.log("New Row","Not Added");
			}
			}
		});
		
		lvChatMessgage.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Utils.log("Long","Click");
				Chat chat=(Chat)arg0.getItemAtPosition(arg2);
				Utils.log("ListView Long clicked","Id: "+chat.getRow_id());
				if(chat.getDate_change().equalsIgnoreCase("false")){
					//showDialog(chat);
					if(mActionMode==null){
						Utils.log("ActionMode","start");
					}
					else{
						Utils.log("ActionMode","End");
					}
				}
				pos=arg2;
				return false;
			}
		});
		
		btnStartChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				sharedPreferences_ = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
				if(sharedPreferences_.getString("Gcm_reg_id", "").length()>0){
					Utils.log("Reg id ","SharedPreference:"+sharedPreferences_.getString("Gcm_reg_id", ""));
					reg_id=sharedPreferences_.getString("Gcm_reg_id", "");
					if(Utils.isOnline(ChatActivity.this)){
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							new ChatRequestAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);						
						else
							new ChatRequestAsyncTask().execute();
					}
					else{
						Toast.makeText(ChatActivity.this, getString(R.string.app_please_wait_label), Toast.LENGTH_LONG).show();
					}
				}
				else{
					getRegId();
				}
			}
		});
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.activity_chat, menu);
		    return true;

	}*/
	
	
	
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
      
            
       case android.R.id.home:
           // your title was clicked!
       		ChatActivity.this.finish();
			//Intent dashboardIntent = new Intent(ChatActivity.this,DashBoardActivity.class);
			//startActivity(dashboardIntent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
       	Utils.log("Homebutton"," clicked");
       	  return true;
            
        default:
            return super.onOptionsItemSelected(item);
    }


		
	}
	*/
	public void initializeControls(){
		etChatText=(EditText)findViewById(R.id.etChatMessage);
		btnSend=(Button)findViewById(R.id.btnSend);
		lvChatMessgage=(ListView)findViewById(R.id.lvChat);
		btnStartChat=(Button) findViewById(R.id.btnStartChat);
		alChat= new ArrayList<Chat>();
		chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
		tvAttach=(ImageView) findViewById(R.id.ivAttachement);
		btnLoadMore=(Button) findViewById(R.id.btnLoadmore);
		llStartChat=(LinearLayout) findViewById(R.id.llStartChat);
		llWaitChat=(LinearLayout) findViewById(R.id.llWaitChat);
	}
	
	public OnClickListener myClickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btnSend){
				
				Utils.log("Chat status",""+sharedPreferences_.getBoolean("chat_status", false));				
				Utils.log("Wait status",""+sharedPreferences_.getBoolean("wait_status", false));
				Utils.log("Chat response",""+sharedPreferences_.getBoolean("chat_response", false));
				
				
				if(sharedPreferences_.getBoolean("wait_status", false)){
					if(sharedPreferences_.getBoolean("chat_status", false)){
				if(etChatText.getText().toString().length()>0){
					text=etChatText.getText().toString();
					
					new SendMessageAsyncTask().execute();
					Utils.log("text ","is: "+etChatText.getText().toString());
					etChatText.setText("");
				/*InputMethodManager inputManager = 
				        (InputMethodManager) ChatActivity.this.
				            getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(
						ChatActivity.this.getCurrentFocus().getWindowToken(),
				        InputMethodManager.HIDE_NOT_ALWAYS); */
				//text="";
				}
				else{
					Toast.makeText(ChatActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
				}
					}else{
						AlertsBoxFactory.showAlert("Please wait our customer care executive will get back to you shortly ", ChatActivity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please Select Start Chat to initiate your chat", ChatActivity.this);
				}
			}
			
			if(v.getId()==R.id.ivAttachement){
				/*ChatActivity.this.finish();
				Intent intent = new Intent(ChatActivity.this, SendImage.class);			
				startActivity(intent);*/
				if(sharedPreferences_.getBoolean("wait_status", false)){
					if(sharedPreferences_.getBoolean("chat_status", false)){
						showShareDialog();
					}
					else{
						AlertsBoxFactory.showAlert("Please wait our customer care executive will get back to you shortly ", ChatActivity.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert("Please Select Start Chat to initiate your chat", ChatActivity.this);
				}
			}
			
			if(v.getId()==R.id.btnLoadmore){
				number_rows=number_rows+50;
				new ChatBindAsyncTask().execute();
				btnLoadMore.setVisibility(View.GONE);
			}
		}
	};

	/*
	 * These method get All messages from database and add all data in arraylist
	 * 
	 */
	public void getChatMessagesBindToChatList(){
		cur_Chat= dbAdapter.getAllChatMessgaes();
		alChat.clear();
		try{
			int i=0;
			count_Rows=cur_Chat.getCount();
		if(cur_Chat.getCount()>0){
			ArrayList<Chat> alTemp= new ArrayList<Chat>();
			while(cur_Chat.moveToNext()){
				if(i<=number_rows){
				String row_id=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.ROW_ID));
				
				String message=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.MESSAGE));
								
				String type=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.MESSAGE_TYPE));
				
				String date=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.DATE));
				
				String time=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.TIME));
				
				String path=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.PATH));
				
				String status=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.STATUS));
				
				String source=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.SOURCE));
				
				String source_id=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.SOURCE_ID));
				
				String date_change=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.DATE_CHANGE));
				
				String sync_status=cur_Chat.getString(cur_Chat.getColumnIndex(TableConstants.SYNC_STATUS));
				
				String url=cur_Chat.getString(cur_Chat
						.getColumnIndex(TableConstants.URL));
				String Name=cur_Chat.getString(cur_Chat
						.getColumnIndex(TableConstants.ADMIN_NAME));
				
				Chat chat= new Chat(row_id,message, type, date, time, source, path, status, date_change, source_id, sync_status, url,"0",Name);
				//alChat.add(chat);
				alTemp.add(chat);
				i++;
				}
			}
			getLastMessages(alTemp);
			Utils.log("rows are","in db: "+i);
		}
		}
		catch(ArrayIndexOutOfBoundsException e){
			Utils.log("ArrayIndexOutOfBoundsException",":"+e);
		}
		catch(CursorIndexOutOfBoundsException e){
			Utils.log("CursorIndexOutOfBoundsException",":"+e);
		}
	}
	
/*
 * 	Async Task used to get all message from database and bind to the listview
 * 
 */
	
	public class ChatBindAsyncTask extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			dbAdapter.open();			
			getChatMessagesBindToChatList();
			dbAdapter.close();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(lvChatMessgage.getAdapter()!=null){
				last_value=lvChatMessgage.getAdapter().getCount()-1;
			}

			chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
			lvChatMessgage.setAdapter(chatArrayAdapter);
			lvChatMessgage.setSelection(lvChatMessgage.getAdapter().getCount()-1);
		
			/*if(number_rows>105){
				Utils.log("set Selection","95: "+number_rows);
				lvChatMessgage.setSelection(number_rows-lvChatMessgage.getAdapter().getCount()-1);
			}
			else if(number_rows>150){
				Utils.log("set Selection","145: "+number_rows);
				lvChatMessgage.setSelection(number_rows-lvChatMessgage.getAdapter().getCount()-1);
			}
			else{
				Utils.log("set Selection",": "+number_rows);
				lvChatMessgage.setSelection(lvChatMessgage.getAdapter().getCount()-1);
			}*/
		}
		
	}

	/*
	 * 	Async Task used to insert the message into database and refreshes listview
	 * 
	 */
	public class SendMessageAsyncTask extends AsyncTask<String, Void, Void>{
		String s="";
		long i;
		String nowTime = "";
		String nowDate = "";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		
			//s=etChatText.getText().toString();
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

		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				dbAdapter.open();
				if (text.length() > 0) {
					i = dbAdapter.insertMessgae(text, "Text", nowDate, nowTime, "client", "noimage", "view", "false", group_id, "no", "nourl", "0", "uploaded", "me");
				}
				text = "";

				if (i != -1) {
					getChatMessagesBindToChatList();
				}
				dbAdapter.close();
				return null;
			}
			   catch(Exception e){
				Utils.log("doinBackground error","inserting row"+e);
				return null;
			  }
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//chatArrayAdapter.add(new Chat(s, "hi", "hi", "hi", "hi", "hi", "client", "hi","true"));
			etChatText.setText("");
			chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
			lvChatMessgage.setAdapter(chatArrayAdapter);
			lvChatMessgage.setSelection(lvChatMessgage.getAdapter().getCount()-1);
			if(i!=-1){
				if(start_async)
				new SendToServerAsyncTask().execute();
			}
		}
	}
	
	public void createFolders(){
		if(!new File(Environment.getExternalStorageDirectory(), "ChatApp").exists()) {                                 
			new File(Environment.getExternalStorageDirectory(), "ChatApp").mkdirs();
		}
		if(!new File(Environment.getExternalStorageDirectory()+"/ChatApp", "Image").exists()) {                                 
			new File(Environment.getExternalStorageDirectory()+"/ChatApp", "Image").mkdirs();
		}
		
		if(!new File(Environment.getExternalStorageDirectory()+"/ChatApp", "Video").exists()) {                                 
			new File(Environment.getExternalStorageDirectory()+"/ChatApp", "Video").mkdirs();
		}
		
		if(!new File(Environment.getExternalStorageDirectory()+"/ChatApp/Image", "Sent").exists()) {                                 
			new File(Environment.getExternalStorageDirectory()+"/ChatApp/Image", "Sent").mkdirs();
		}
	}

	@Override
	public void showImage(String position,String source,String type) {
		// TODO Auto-generated method stub
		this.finish();
		Utils.log("image name",""+position);

		if(source.equalsIgnoreCase("Client")&& type.equalsIgnoreCase("Image")){
			Utils.log("showImage","Intent sent client");
			Intent i= new Intent(ChatActivity.this,ShowImageActivity.class);
			i.putExtra("img_name", position);
			i.putExtra("source", source);
			startActivity(i);
		}
		
		if(source.equalsIgnoreCase("server")&& type.equalsIgnoreCase("Image")){
			Utils.log("showImage","Intent sent server");
			Intent i= new Intent(ChatActivity.this,ShowImageActivity.class);
			i.putExtra("img_name", position);
			i.putExtra("source", source);
			startActivity(i);
		}

		if(type.equalsIgnoreCase("Video")){
			if(source.equalsIgnoreCase("client")){
				this.finish();
				String videourl=TableConstants.SD_CARD_VIDEO_PATH+"/Sent/"+position;
				Utils.log("Video url",""+videourl);
				Intent intent = new Intent(ChatActivity.this,ShowVideoActivity.class); 
				intent.putExtra("videopath", videourl);
				startActivity(intent);
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TableConstants.CHECK_APP_OPEN=false;
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new SendToServerAsyncTask().execute();
		TableConstants.CHECK_APP_OPEN=true;
		if(TableConstants.CHECK_APP_OPEN){
			new ChatBindAsyncTask().execute();
		}
		
		//new ChatBindAsyncTask().execute();
		btnSend.requestFocus();
		
		 NotificationManager nMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		 nMgr.cancel(1);

		/*Intent intentService1 = new Intent(ChatActivity.this,AlarmBroadcastReceiver.class);
				
		PendingIntent pintent1 = PendingIntent.getBroadcast(this, 0, intentService1, 0);
		Calendar cal = Calendar.getInstance();
		AlarmManager alarm1 = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
		// Start every 1mon..
		alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*1000, pintent1);*/
		
		/*
		 * To get Network provided date and time in android
		 * 
		  	LocationManager locMan = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
		  	long networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
		 */
		
		/*
		 * To enable hindi chat send to use these code
		 * 
		 * Intent	intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.google.android.apps.inputmethod.hindi"));
			startActivity(intent);
		
		*/

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mOnlineBroadcast);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mChatEnd);
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        Utils.log("Service","Message");
	        //  ... react to local broadcast message
	        TableConstants.MESSAGE_COUNT=0;
	        dbAdapter.open();
	        getChatMessagesBindToChatList();
	        dbAdapter.close();
	        chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
			lvChatMessgage.setAdapter(chatArrayAdapter);
			lvChatMessgage.setSelection(lvChatMessgage.getAdapter().getCount()-1);
	    }
	};
	
	private BroadcastReceiver mOnlineBroadcast = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        Utils.log("Chat","Online");
	        //  ... react to local broadcast message
	        llWaitChat.setVisibility(View.GONE);
	        llStartChat.setVisibility(View.GONE);
	      
	    }
	};
	
	private BroadcastReceiver mChatEnd = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
			//  ... react to local broadcast message
			Utils.log("Chat","End:");
			llWaitChat.setVisibility(View.GONE);
			llStartChat.setVisibility(View.VISIBLE);
		}
	};
	
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
			start_async=false;
			Utils.log("SendToServerAsyncTask doInBackground","started");
			
			SendMessageSOAP sendMessageSOAP= new SendMessageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL),getString(R.string.METHOD_SEND_SERVER_MESSAGE));
			try {
				rslt=sendMessageSOAP.sendServerMessage(profile_id,ChatActivity.this,group_id);
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
			start_async=true;;
		}
	}
	
	public void imageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_IMAGE);
    }
	
	public void VideoFromGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_IMAGE);
    }

	@Override
	public void refreshAdapter(int pos) {
		// TODO Auto-generated method stub
			dbAdapter.open();
	        getChatMessagesBindToChatList();
	        dbAdapter.close();
	        chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
	        chatArrayAdapter.notifyDataSetChanged();
			lvChatMessgage.setAdapter(chatArrayAdapter);
	        lvChatMessgage.setSelection(pos);
	}
	
	/*public void showDialog(final Chat chat){
		final Dialog dialog = new Dialog(ChatActivity.this);
		dialog.setContentView(R.layout.layout_dialog_box);
		dialog.setTitle("Select...");
		
		// set the custom dialog components - text, image and button
		ImageView ivCopyText = (ImageView) dialog.findViewById(R.id.ivCopy);
		
		ImageView ivDeleteRow = (ImageView) dialog.findViewById(R.id.ivDelete);
		
		
		// if button is clicked, close the custom dialog
		ivCopyText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				 ClipboardManager cm = (ClipboardManager)ChatActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
			     cm.setText(chat.getMessage());
			     Toast.makeText(ChatActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
			}
		});
		
		ivDeleteRow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dbAdapter.open();
				dbAdapter.deleteRow(chat.getRow_id());
		        getChatMessagesBindToChatList();
		        dbAdapter.close();
		        chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
		        chatArrayAdapter.notifyDataSetChanged();
				lvChatMessgage.setAdapter(chatArrayAdapter);
		        lvChatMessgage.setSelection(pos);
				dialog.dismiss();
			}
		});

		dialog.show();
	}*/
	
	public ArrayList<Chat> getLastMessages(ArrayList<Chat> alChat1){
		
		for(int i=alChat1.size()-1;i>=0;i--){
			this.alChat.add(alChat1.get(i));
		}
		return alChat;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	//	Utils.log("visible","is: "+lvChatMessgage.getFirstVisiblePosition());
		//Utils.log("visibleItemCount",""+visibleItemCount);
		//Utils.log("firstVisibleItem",""+firstVisibleItem);
		if(count_Rows>number_rows){
			if(lvChatMessgage.getFirstVisiblePosition()<=5){
			btnLoadMore.setVisibility(View.VISIBLE);
			}
			else{
				btnLoadMore.setVisibility(View.GONE );
			}
		}
		else{
			btnLoadMore.setVisibility(View.GONE );
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	/*	if(flag){
			
		Intent dashboardIntent = new Intent(ChatActivity.this,Complaints.class);
		startActivity(dashboardIntent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
		
		
		flag = false;
		
	} else {*/
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
		
	//}
	}
	
	
	public void showShareDialog(){
		final Dialog dialog = new Dialog(ChatActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_share);
		dialog.setTitle("Select...");
		
		int width = 0;
		int height =0;
		 Point size = new Point();
		    WindowManager w =this.getWindowManager();

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		        w.getDefaultDisplay().getSize(size);
		        width = size.x;
		        height = size.y;
		    } else {
		        Display d = w.getDefaultDisplay();
		        width = d.getWidth();
		        height   = d.getHeight();;
		    }
		    
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);	
		
		// set the custom dialog components - text, image and button
		ImageView ivShareCaptureImage = (ImageView) dialog.findViewById(R.id.ivShareCaptureImage);
		
		ImageView ivShareImage = (ImageView) dialog.findViewById(R.id.ivShareImage);
		
		
		// if button is clicked, close the custom dialog
		ivShareCaptureImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ChatActivity.this.finish();
				Intent intent = new Intent(ChatActivity.this, SendImage.class);	
				intent.putExtra("share","CameraImage");
				startActivity(intent);
			}
		});

		ivShareImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isDeviceSupportCamera()){
				ChatActivity.this.finish();
				Intent intent = new Intent(ChatActivity.this, SendImage.class);	
				intent.putExtra("share","Image");
				startActivity(intent);
				}
				else{
					Toast.makeText(ChatActivity.this, "Camera not supprted in your Device", Toast.LENGTH_LONG).show();
				}
				
			}
		});

		dialog.show();
		}
	
	
	 private class ActionModeCallback implements ActionMode.Callback {
		 
	        @Override
	        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	            // inflate contextual menu
	            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
	            return true;
	        }
	 
	        @Override
	        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	            return false;
	        }
	 
	        @SuppressWarnings("deprecation")
			@Override
	        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	 
	            switch (item.getItemId()) {
	            case R.id.menu_delete:
	                // retrieve selected items and delete them out
	            	deleteRow(Action_Chat);
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            case R.id.menu_copy:
	                // retrieve selected items and copy them
	            	
	            	
	            	// ClipboardManager cm = (ClipboardManager)ChatActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
	            	 String chat_copy="";
	            	 for(int j=0;j<Action_Chat.size();j++){
	            		 if(chat_copy.length()>0){
	            			chat_copy=chat_copy+Action_Chat.get(j).getMessage();
	            		 }
	            		 else{
	            			 chat_copy =Action_Chat.get(j).getMessage();
	            		 }
	     		 	}
	            	 
	            	 if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
		            	    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		            	    clipboard.setText(chat_copy);
		            	} else {
		            	    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		            	    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", chat_copy);
		            	            clipboard.setPrimaryClip(clip);
		            	}
	            	// cm.setText(chat_copy);
	            	   chatArrayAdapter.notifyDataSetChanged();
				     Toast.makeText(ChatActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	            }
	 
	        }
	 
	        @Override
	        public void onDestroyActionMode(ActionMode mode) {
	            // remove selection
	        	mActionMode=null;
	        	Action_Chat.clear();
	        	Action_Row_ID.clear();
	        	  chatArrayAdapter.notifyDataSetChanged();
	        }
	    }
	 
	 public void deleteRow(ArrayList<Chat> alchat){
		 	dbAdapter.open();
		 	Utils.log("Delete Chat","size:"+alchat.size());
		 	for(int j=0;j<alchat.size();j++){
		 		dbAdapter.deleteRow(alchat.get(j).getRow_id());
		 	}
	        getChatMessagesBindToChatList();
	        dbAdapter.close();
	        chatArrayAdapter= new ChatArrayAdapter(ChatActivity.this, R.layout.row_item, alChat);
	        chatArrayAdapter.notifyDataSetChanged();
			lvChatMessgage.setAdapter(chatArrayAdapter);
	        lvChatMessgage.setSelection(pos);
	        if(mActionMode!=null)
	        mActionMode.finish();
	 }
	 
	 public class ChatRequestAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		 ProgressHUD mProgressHUD;
		 ChatRequestSOAP chatRequestSOAP;
		 String chatResult="",chatResponse="";
		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD=ProgressHUD.show(ChatActivity.this, getString(R.string.app_please_wait_label),true,true,this);
		}
		 
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			chatRequestSOAP=new ChatRequestSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_CHAT_REQUEST));
			try {
				chatResult=chatRequestSOAP.sendRequest(profile_id,reg_id);
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			if(chatResult.length()>0){
				if(chatResult.equalsIgnoreCase("OK")){
					llStartChat.setVisibility(View.GONE);
					llWaitChat.setVisibility(View.VISIBLE);
					String sharedPreferences_name = getString(R.string.shared_preferences_name);
					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
					
					Editor editor= sharedPreferences.edit();
					editor.putBoolean("chat_status", false);
					editor.putBoolean("wait_status", true);
					editor.commit();
					
					
					Intent serviceIntent = new Intent(ChatActivity.this,ServerCommunicationService.class);
					startService(serviceIntent);
					
					Intent intentSetRepeatAlarm12 = new Intent(ChatActivity.this,AlarmBroadcastReceiver1.class);
					
					penIntentRepeatAlarm= PendingIntent.getBroadcast(ChatActivity.this, 0, intentSetRepeatAlarm12, 0);
					Calendar cal = Calendar.getInstance();
					AlarmManager Repeatalarm = (AlarmManager)ChatActivity.this.getSystemService(Context.ALARM_SERVICE);
					// Start every 30 seconds
					//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
					// Start every 1mon..
					Repeatalarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*1000, penIntentRepeatAlarm);
				}
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			mProgressHUD.dismiss();
		}
	 }
	 /**
	     * Checking device has camera hardware or not
	     * */
	    private boolean isDeviceSupportCamera() {
	        if (getApplicationContext().getPackageManager().hasSystemFeature(
	                PackageManager.FEATURE_CAMERA)) {
	            // this device has a camera
	            return true;
	        } else {
	            // no camera on this device
	            return false;
	        }
	    }

	  
	    public void getRegId(){
	    	 Utils.log("GCM",  "Start");
	    	new AsyncTask<Void, Void, String>() {
	            @Override
	            protected String doInBackground(Void... params) {
	                String msg = "";
	                try {
	        			/*   if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	                    }
	                    reg_id = gcm.register(AuthenticationMobile.PROJECT_NUMBER);*/
	                    msg = "Device registered, registration ID=" + reg_id;
	                    Utils.log("GCM",  msg);
	                    
	                    Utils.log("RegID",":"+reg_id);
	                   
	                } catch (Exception ex) {
	                    msg = "error";
	                    Utils.log("GCM error",  ""+ex);
	                }
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(String msg) {
	            	if(msg.equalsIgnoreCase("error")){
	            		try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		getRegId();
	            	}
	            	else{
	            		sharedPreferences_ = getApplicationContext()
	            				.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
	            		Editor editor=sharedPreferences_.edit();
	            		editor.putString("Gcm_reg_id", reg_id);
	            		editor.commit();
	            		
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							new ChatRequestAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);						
						else
							new ChatRequestAsyncTask().execute();
	            	}
	               
	            }
	        }.execute(null, null, null);
	    }
	    	
	    
	    public void getRegId1(){
	    	new AsyncTask<Void, Void, String>() {
	            @Override
	            protected String doInBackground(Void... params) {
	                String msg = "";
	                try {
	                   /* if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	                    }
	                    reg_id = gcm.register(AuthenticationMobile.PROJECT_NUMBER);*/
	                    msg = "Device registered, registration ID=" + reg_id;
	                    Log.i("GCM",  msg);
	                    
	                    Utils.log("RegID",":"+reg_id);
	                   
	                } catch (Exception ex) {
	                    msg = "Error :" + ex.getMessage();
	                    
	                }
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(String msg) {
	            	Utils.log("Error","GCM"+msg);
	              //  etRegId.setText(msg + "\n");
	            }
	        }.execute(null, null, null);
	    }
}
