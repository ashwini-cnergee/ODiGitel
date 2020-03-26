package com.cnergee.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.cnergee.mypage.SOAP.AllComplaintDetailsSOAP;
import com.cnergee.mypage.obj.Answer;
import com.cnergee.mypage.obj.Question;
import com.cnergee.mypage.obj.Resolve_Complaint_Obj;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;

public class Resolve_Yourself_Fragment extends Fragment {
View view;
ProgressHUD mProgressHUD;
String Current_Method_Name;
ArrayList<String> alNames= new ArrayList<String>();
ArrayList<String> alValues= new ArrayList<String>();
String data_for="";
Spinner spComplaintCategory,spComplaintMaster;
RadioGroup rgConnectionUsed;
RadioButton rbPc,rbRouter;
ArrayList<Resolve_Complaint_Obj> alComplaintCategory= new ArrayList<Resolve_Complaint_Obj>();
ArrayList<Resolve_Complaint_Obj> alComplaintMaster= new ArrayList<Resolve_Complaint_Obj>();
public static ArrayList<Question> alQuestion= new ArrayList<Question>();

public static ArrayList<Answer> alAnswer= new ArrayList<Answer>();
boolean is_comp_category_select=false,is_comp_master_select=false;
String ComplaintId="0",Old_ComplaintId="0", conn_used="";;
@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Utils.log(this.getClass().getSimpleName(), "OnAttach Called");
		Current_Method_Name=getString(R.string.METHOD_GET_COMPLAINT_CATEGORY);
		data_for="complaintCategory";
		 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		 {
			 new GetDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
		 }
		 else
		 {			 
			 new GetDetailsAsyncTask().execute();
		 }
	}

@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Utils.log(this.getClass().getSimpleName(), "onDetach Called");
	}
@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.log(this.getClass().getSimpleName(), "onResume Called");
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view= inflater.inflate(R.layout.fragment_resolve_yourself, container, false);
		Utils.log(this.getClass().getSimpleName(), "onCreateView Called");
		initializeControls();
		
		return view;
	}
	
	public void initializeControls(){
		spComplaintCategory=(Spinner) view.findViewById(R.id.sp_complaint_category);
		spComplaintMaster=(Spinner) view.findViewById(R.id.sp_complaint_master);
		rbPc=(RadioButton)view.findViewById(R.id.rbPc);
		rbRouter=(RadioButton)view.findViewById(R.id.rbRouter);
		//spComplaintCategory.setOnItemSelectedListener(onComplaintCategoryItemSelectedListener);
		spComplaintMaster.setOnItemSelectedListener(onComplaintMasterItemSelectedListener);
		rbPc.setOnClickListener(rbClickListener);
		rbRouter.setOnClickListener(rbClickListener);
		
	}
	
	public OnItemSelectedListener onComplaintCategoryItemSelectedListener= new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(is_comp_category_select){
							
				Current_Method_Name=getString(R.string.METHOD_GET_COMPLAINT_MASTER);
				data_for="complaintMaster";
				alComplaintMaster.clear();
				alNames.clear();
				alValues.clear();
				alNames.add("ComplaintCategoryId");
				alValues.add(alComplaintCategory.get(arg2).getComplaint_Id());
				
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 new GetDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
				 }
				 else
				 {			 
					 new GetDetailsAsyncTask().execute();
				 }
				}
				else{
					Utils.log("Category Selection ", ":"+is_comp_category_select);
				}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public OnItemSelectedListener onComplaintMasterItemSelectedListener= new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			rbPc.setChecked(false);rbRouter.setChecked(false);
			if(is_comp_master_select){
				
				ComplaintId=alComplaintMaster.get(arg2).getComplaint_Id();
				
				}
				else{
					Utils.log("Category Selection ", ":"+is_comp_category_select);
				}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	OnClickListener rbClickListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.rbPc){
				if(!ComplaintId.equalsIgnoreCase("0")){
					if(!Old_ComplaintId.equalsIgnoreCase(ComplaintId)||conn_used.length()==0||!conn_used.equalsIgnoreCase("p")){
						
						conn_used="p";
						Utils.log("Old Comp Id",":"+Old_ComplaintId);
						Utils.log("Current Comp Id",":"+ComplaintId);
						Current_Method_Name=getString(R.string.METHOD_GET_QUESTION_ANSWER);
						data_for="questions";
						alNames.clear();
						alValues.clear();
						alNames.add("ComplaintId");
						alValues.add(ComplaintId);
						alNames.add("QuestionCategory");
						alValues.add("PC");
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						 {
							 new GetDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
						 }
						 else
						 {			 
							 new GetDetailsAsyncTask().execute();
						 }
					}
						else{
								Utils.log("Old Comp Id",":"+Old_ComplaintId);
								Utils.log("Current Comp Id",":"+ComplaintId);
								if(alQuestion!=null){
									if(alQuestion.size()>0){
										
										Intent intent = new Intent("show_question_dialog");
										  // You can also include some extra data.
										  intent.putExtra("message", "This is my message!");
										  LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
									}
									else{
										Utils.log("Call",":Question size 0");
									}
								}
								else{
									Utils.log("Call",":Question List Null");
								}
						}
				}
				else{
					
				}
			}
			
			if(v.getId()==R.id.rbRouter){
				if(!ComplaintId.equalsIgnoreCase("0")){
					if(!Old_ComplaintId.equalsIgnoreCase(ComplaintId)||conn_used.length()==0||!conn_used.equalsIgnoreCase("r")){
						conn_used="r";
						Utils.log("Old Comp Id",":"+Old_ComplaintId);
						Utils.log("Current Comp Id",":"+ComplaintId);
						Current_Method_Name=getString(R.string.METHOD_GET_QUESTION_ANSWER);
						data_for="questions";
						alNames.clear();
						alValues.clear();
						alNames.add("ComplaintId");
						alValues.add(ComplaintId);
						alNames.add("QuestionCategory");
						alValues.add("Router");
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						 {
							 new GetDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
						 }
						 else
						 {			 
							 new GetDetailsAsyncTask().execute();
						 }
					}
					else{
						
						Utils.log("Old Comp Id",":"+Old_ComplaintId);
						Utils.log("Current Comp Id",":"+ComplaintId);
						if(alQuestion!=null){
							if(alQuestion.size()>0){
								
								Intent intent = new Intent("show_question_dialog");
								  // You can also include some extra data.
								  intent.putExtra("message", "This is my message!");
								  LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
							}
							else{
								Utils.log("Call",":Question size 0");
							}
						}
						else{
							Utils.log("Call",":Question List Null");
						}
					}
				}
			}
			else{
				
			}
		}
	};
	
	@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
			Utils.log(this.getClass().getSimpleName(), "onDestroyView Called");
		}
	@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			Utils.log(this.getClass().getSimpleName(), "onDestroy Called");
		}
	
	
	
	public class GetDetailsAsyncTask extends AsyncTask<String, Void, String> implements OnCancelListener{
		String getComplaintDetaiilsResult="",getComplaintDetaiilsResponse="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(getActivity(),getString(R.string.app_please_wait_label), true,true,this);
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AllComplaintDetailsSOAP allComplaintDetailsSOAP= new AllComplaintDetailsSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), Current_Method_Name);
			try {
				getComplaintDetaiilsResult=allComplaintDetailsSOAP.getComplaintDetails(alNames, alValues);
				getComplaintDetaiilsResponse=allComplaintDetailsSOAP.getResponse();
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			if(getComplaintDetaiilsResult.length()>0){
				if(getComplaintDetaiilsResponse!=null){
				if(getComplaintDetaiilsResponse.length()>0){
					if(data_for.equalsIgnoreCase("complaintCategory")){
						
						parseComplaintCategoryJson(getComplaintDetaiilsResponse);
						
						
					}
					
					if(data_for.equalsIgnoreCase("complaintMaster")){
						
						parseComplaintMasterJson(getComplaintDetaiilsResponse);
						
					}
					if(data_for.equalsIgnoreCase("questions")){
						
						//Utils.log("Questions Response", getComplaintDetaiilsResponse);
						
						Old_ComplaintId=ComplaintId;
						parseQuestionAnswerJson(getComplaintDetaiilsResponse);
						
					}
				}
				}
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void parseComplaintCategoryJson(String response){
		if(response!=null){
			if(response.length()>0){
				Utils.log("Parse Json", "ComplaintCategory");
				try {
					JSONObject mainJson= new JSONObject(response);
					JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(NewDataSetJson.has("Table")){
						
						if(NewDataSetJson.get("Table") instanceof JSONObject){
							Utils.log("Parse Table", "Json Object");
							JSONObject tableObj=NewDataSetJson.getJSONObject("Table");
							String comp_id=tableObj.optString("ComplaintCategoryID");
							String comp_name=tableObj.optString("ComplaintCategory");
							Resolve_Complaint_Obj resolve_Complaint_Obj= new Resolve_Complaint_Obj(comp_id, comp_name);
							alComplaintCategory.add(resolve_Complaint_Obj);
						}
						
						if(NewDataSetJson.get("Table") instanceof JSONArray){
							JSONArray tableObj=NewDataSetJson.getJSONArray("Table");
							for(int i=0;i<tableObj.length();i++){
								Utils.log("Parse Table", "Json Array");
								JSONObject table=tableObj.getJSONObject(i);
								String comp_id=table.optString("ComplaintCategoryID");
								String comp_name=table.optString("ComplaintCategory");
								Resolve_Complaint_Obj resolve_Complaint_Obj= new Resolve_Complaint_Obj(comp_id, comp_name);
								alComplaintCategory.add(resolve_Complaint_Obj);
							}
						}
					
						
						}
					
					bindComplaintCategorySpinner();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void parseComplaintMasterJson(String response){
		if(response!=null){
			if(response.length()>0){
				Utils.log("Parse Json", "ComplaintMaster");
				try {
					JSONObject mainJson= new JSONObject(response);
					JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(NewDataSetJson.has("Table")){
						
						if(NewDataSetJson.get("Table") instanceof JSONObject){
							Utils.log("Parse Table", "Json Object");
							JSONObject tableObj=NewDataSetJson.getJSONObject("Table");
							String comp_id=tableObj.optString("ComplaintID");
							String comp_name=tableObj.optString("ComplaintName");
							Resolve_Complaint_Obj resolve_Complaint_Obj= new Resolve_Complaint_Obj(comp_id, comp_name);
							alComplaintMaster.add(resolve_Complaint_Obj);
						}
						
						if(NewDataSetJson.get("Table") instanceof JSONArray){
							JSONArray tableObj=NewDataSetJson.getJSONArray("Table");
							for(int i=0;i<tableObj.length();i++){
								Utils.log("Parse Table", "Json Array");
								JSONObject table=tableObj.getJSONObject(i);
								String comp_id=table.optString("ComplaintID");
								String comp_name=table.optString("ComplaintName");
								Resolve_Complaint_Obj resolve_Complaint_Obj= new Resolve_Complaint_Obj(comp_id, comp_name);
								alComplaintMaster.add(resolve_Complaint_Obj);
							}
						}
	
						}
					bindComplaintMasterSpinner();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void bindComplaintMasterSpinner(){		
				if(alComplaintMaster!=null){
					int listSize=alComplaintMaster.size();
					if(listSize>0){
						ArrayList<String> alComplaintMaster_Name= new ArrayList<String>();
						for(int i=0;i<listSize;i++){
							//String comp_id=alComplaintMaster.get(i).getComplaint_Id();
						//	if(comp_id.equalsIgnoreCase(complaintCategoryId))
							alComplaintMaster_Name.add(alComplaintMaster.get(i).getComplaint_Name());
						}
						spComplaintMaster.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, alComplaintMaster_Name));
						
						Utils.log("bind", "Complaint Category");
						
						
						spComplaintMaster.post(new Runnable() {
						    public void run() {
						    	spComplaintMaster.setOnItemSelectedListener(onComplaintMasterItemSelectedListener);
						    }
						});
						
						is_comp_master_select=true;
					}
		}
	}

	public void bindComplaintCategorySpinner(){
		if(alComplaintCategory!=null){
			int listSize=alComplaintCategory.size();
			if(listSize>0){
				ArrayList<String> alComplaintCat_Name= new ArrayList<String>();
				for(int i=0;i<listSize;i++){
					alComplaintCat_Name.add(alComplaintCategory.get(i).getComplaint_Name());
				}
				spComplaintCategory.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, alComplaintCat_Name));
				//is_comp_category_select=true;				
				Utils.log("bind", "Complaint Category");
				
				
				spComplaintCategory.post(new Runnable() {
				    public void run() {
				    	spComplaintCategory.setOnItemSelectedListener(onComplaintCategoryItemSelectedListener);
				    }
				});
				
				is_comp_category_select=true;
			}
		}
	}
	
	
	public void parseQuestionAnswerJson(String response){
		if(response!=null){
			if(response.length()>0){
				alQuestion.clear();
				alAnswer.clear();
				Utils.log("Parse Json", "QuestionAnswer");
				try {
					JSONObject mainJson= new JSONObject(response);
					JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
					if(NewDataSetJson.has("Table")){
						
						if(NewDataSetJson.get("Table") instanceof JSONObject){
							Utils.log("Parse Table", "Json Object Question");
							JSONObject tableObj=NewDataSetJson.getJSONObject("Table");
							
							Question question=
									new Question(tableObj.optString("QuestionId"), tableObj.optString("Question"), tableObj.optString("SolutionImage"), tableObj.optString("Solution"), 
											tableObj.optString("AnsType"));
							if(tableObj.optString("QuestionId").length()>0)
							alQuestion.add(question);
						
						}
						
						if(NewDataSetJson.get("Table") instanceof JSONArray){
							JSONArray tableObj=NewDataSetJson.getJSONArray("Table");
							for(int i=0;i<tableObj.length();i++){
								Utils.log("Parse Table", "Json Array Question");
								JSONObject table=tableObj.getJSONObject(i);
								
								Question question=
										new Question(table.optString("QuestionId"), table.optString("Question"), table.optString("SolutionImage"), table.optString("Solution"), 
												table.optString("AnsType"));
								if(table.optString("QuestionId").length()>0)
								alQuestion.add(question);
								
							}
						}
	
						}
					
					if(NewDataSetJson.has("Table1")){
						
						if(NewDataSetJson.get("Table1") instanceof JSONObject){
							Utils.log("Parse Table", "Json Object Answer");
							JSONObject tableObj=NewDataSetJson.getJSONObject("Table1");
							Answer answer=new Answer(tableObj.optString("Ans"), tableObj.optString("AnsId"), tableObj.optString("QuestionId"));
							
							Utils.log("AnswerText", ":"+tableObj.optString("Ans"));
							Utils.log("AnswerId", ":"+tableObj.optString("AnsId"));
							Utils.log("QuestionId", ":"+tableObj.optString("QuestionId"));
							if(tableObj.optString("AnsId").length()>0)
							alAnswer.add(answer);
							/*if(alQuestion!=null){
								for(int i=0;i<alQuestion.size();i++){
									Question question=alQuestion.get(i);
									ArrayList<Answer> alAnswer= question.getAlAnswers();
									
									if(question.getQuestion_Id().equalsIgnoreCase(tableObj.optString("QuestionId"))){
										
										Answer answer=new Answer(tableObj.optString("Ans"), tableObj.optString("AnsId"), tableObj.optString("QuestionId"));
										if(alAnswer!=null)
										alAnswer.add(answer);
										else{
											alAnswer=new ArrayList<Answer>();
											alAnswer.add(answer);
										}
									}
									
									question.setAlAnswers(alAnswer);
									alQuestion.add(i, question);
								}
							}*/
						}
						
						if(NewDataSetJson.get("Table1") instanceof JSONArray){
							JSONArray tableObj=NewDataSetJson.getJSONArray("Table1");
							for(int i=0;i<tableObj.length();i++){
								Utils.log("Parse Table", "Json Array Answer");
								JSONObject table=tableObj.getJSONObject(i);
								Answer answer=new Answer(table.optString("Ans"), table.optString("AnsId"), table.optString("QuestionId"));
								Utils.log("AnswerText", ":"+table.optString("Ans"));
								Utils.log("AnswerId", ":"+table.optString("AnsId"));
								Utils.log("QuestionId", ":"+table.optString("QuestionId"));
								if(table.optString("AnsId").length()>0)
								alAnswer.add(answer);
								/*if(alQuestion!=null){
									for(int j=0;j<alQuestion.size();j++){
										Question question=alQuestion.get(j);
										ArrayList<Answer> alAnswer= question.getAlAnswers();
										if(question.getQuestion_Id().equalsIgnoreCase(table.optString("QuestionId"))){
											
											Answer answer=new Answer(table.optString("Ans"), table.optString("AnsId"), table.optString("QuestionId"));
											if(alAnswer!=null)
											alAnswer.add(answer);
											else{
												alAnswer=new ArrayList<Answer>();
												alAnswer.add(answer);
											}
										}
										
										question.setAlAnswers(alAnswer);
										alQuestion.add(i, question);
									}
								}*/
							}
						}
	
						}
					
					for(int m=0;m<alQuestion.size();m++){
						Question question=alQuestion.get(m);
						Utils.log("","/*********************************/");
						Utils.log("Question", ":"+question.getQuestion_Text());
						Utils.log("QuestionId", ":"+question.getQuestion_Id());
						Utils.log("AnswerType", ":"+question.getAnswerType());
						Utils.log("SolutionText", ":"+question.getSolution_Text());
						//Utils.log("Answer Size", ":"+question.getAlAnswers().size());
					}
					
					
					for(int m=0;m<alAnswer.size();m++){
						Answer answer=alAnswer.get(m);
						Utils.log("","/*********************************/");
						Utils.log("AnswerText", ":"+answer.getAns_Text());
						Utils.log("AnswerId", ":"+answer.getAns_Id());
						Utils.log("QuestionId", ":"+answer.getAns_Ques_Id());
					}
					
					if(alQuestion!=null){
						if(alQuestion.size()>0){
							Intent intent = new Intent("show_question_dialog");
							  // You can also include some extra data.
							  intent.putExtra("message", "This is my message!");
							  LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						}
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Utils.log("Parse Question Error", ":"+e);
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					Utils.log("Parse Question Error", ":"+e);
					e.printStackTrace();
				}
				catch (Error e) {
					// TODO Auto-generated catch block
					Utils.log("Parse Question Error", ":"+e);
					e.printStackTrace();
				}
			}
		}
	}
}
