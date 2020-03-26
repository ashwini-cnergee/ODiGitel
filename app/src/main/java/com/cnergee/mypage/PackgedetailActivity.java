package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import cnergee.krishna.ookla.odigitel.R;
import com.cnergee.mypage.SOAP.AreaWiseSettingSOAP;
import com.cnergee.mypage.adapter.CustomGridAdapter;
import com.cnergee.mypage.adapter.SpinnerDaysAdapter;
import com.cnergee.mypage.caller.PackgeCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.PackageList;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.PackageListParsing;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PackgedetailActivity extends Activity {

    Boolean is_activity_running=false;
    public static boolean isVaildUser=false;

    Context context;
    private String sharedPreferences_name;
    Utils utils = new Utils();
    String MemberId,memberloginid,otp_password="";
    ProgressDialog mainDialog;
    public static String rslt = "";
    public static String settingResult="";
    public static String strXML = "";
    boolean check_intent=false;


    PackageListWebService packageListWebService = null;
    static String extStorageDirectory = Environment
            .getExternalStorageDirectory().toString();
    final static String xml_folder = "mypage";
    final static String TARGET_BASE_PATH = extStorageDirectory + "/"
            + xml_folder + "/";

    public String xml_file_postFix = "PackageList.xml";
    public String xml_file;
    public String xml_file_with_path;

    public static Map<String, PackageList> mapPackageList;

    Spinner planList;
    GridView gridview,gridview1;
    HashMap<String,ArrayList<PackageList>> hm_packge_list = new HashMap<>();
    ImageView btnhome,btnprofile,btnnotification,btnhelp;
    SpinnerDaysAdapter spinnerDaysAdapter;
    private int previousSelectedPosition = -1;
    boolean is_selected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packgedetail);

        xml_file_with_path = TARGET_BASE_PATH + xml_file_postFix;
        File file = new File(xml_file_with_path);
        file.delete();
        is_activity_running=true;

        context = this;
        is_activity_running=true;
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);
        mainDialog= new ProgressDialog(PackgedetailActivity.this);
        MemberId = utils.getMemberId();
        mapPackageList = new HashMap<String, PackageList>();
        xml_file = xml_file_postFix;
        xml_file_with_path =  TARGET_BASE_PATH + xml_file;
        memberloginid = utils.getMemberLoginID();
        otp_password= sharedPreferences.getString("otp_password", "0");

        planList=(Spinner)findViewById(R.id.planList);
        gridview=(GridView) findViewById(R.id.gridview);
        gridview1=(GridView) findViewById(R.id.gridview1);

        btnhome = (ImageView)findViewById(R.id.btnhome);
        btnprofile = (ImageView)findViewById(R.id.btnprofile);
        btnnotification = (ImageView)findViewById(R.id.btnnotification);
        btnhelp = (ImageView)findViewById(R.id.btnhelp);
        check_intent=getIntent().getBooleanExtra("check_intent", false);

        btnhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PackgedetailActivity.this.finish();
                //Intent i = new Intent(PackgedetailActivity.this,IONHome.class);
                //startActivity(i);
                //PackgedetailActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
                //BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PackgedetailActivity.this.finish();
                Intent i = new Intent(PackgedetailActivity.this,Profile.class);
                startActivity(i);
                PackgedetailActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

            }
        });

        btnnotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PackgedetailActivity.this.finish();
                Intent i = new Intent(PackgedetailActivity.this,NotificationListActivity.class);
                startActivity(i);
                PackgedetailActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

            }
        });
        btnhelp = (ImageView)findViewById(R.id.btnhelp);

        btnhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PackgedetailActivity.this.finish();
                Intent i = new Intent(PackgedetailActivity.this,HelpActivity.class);
                startActivity(i);
                PackgedetailActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

            }
        });


        planList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setGrid("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(Utils.isOnline(PackgedetailActivity.this)){
            //packageListWebService = new PackageListWebService();
            //packageListWebService.execute((String)null);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                Utils.log("Both Thread"," executed");
                new ValidUserWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
            }
            else{
                Utils.log("ValidUser Thread"," executed");
                new ValidUserWebService().execute();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    PackgedetailActivity.this);
            builder.setMessage(
                    "Please connect to internet and try again!!")
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {

                                    PackgedetailActivity.this.finish();
                                    Intent intent = new Intent(
                                            PackgedetailActivity.this,
                                            IONHome.class);

                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }




    protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        ProgressHUD mProgressHUD;
        protected void onPreExecute() {

            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(PackgedetailActivity.this,getString(R.string.app_please_wait_label), true,true, this);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                Utils.log("Valid User","Progress Not Showing");
            }
            else{

            }

        }

        @SuppressLint("CommitPrefEdits")
        protected void onPostExecute(Void unused) {
            if(is_activity_running)
                mProgressHUD.dismiss();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            }
            else{


            }
            if (rslt.trim().equalsIgnoreCase("ok")) {

                if (isVaildUser) {
                    //new PackageListWebService().execute();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        new SettingResultAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
                    }
                    else{
                        new SettingResultAsyncTask().execute();
                    }
                } else {
                    //mProgressHUD.dismiss();
                    SharedPreferences sharedPreferences1 = getApplicationContext()
                            .getSharedPreferences(sharedPreferences_name, 0); // 0
                    // -

                    SharedPreferences.Editor edit1 = sharedPreferences1.edit();
                    edit1.clear();

                    SharedPreferences sharedPreferences2 = getApplicationContext()
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_renewal),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit2 = sharedPreferences2.edit();
                    edit2.clear();

                    SharedPreferences sharedPreferences3 = getApplicationContext()
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_notification_list),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit3 = sharedPreferences3.edit();
                    edit3.clear();

                    SharedPreferences sharedPreferences4 = getApplicationContext()
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_payment_history),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit4 = sharedPreferences4.edit();
                    edit4.clear();

                    SharedPreferences sharedPreferences5 = getApplicationContext()
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_profile),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit5 = sharedPreferences5.edit();
                    edit5.clear();
                    //	Utils.log("Data","cleared");
                    sharedPreferences1.edit().clear().commit();

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //tell the Dialog to use the dialog.xml as it's layout description
                    dialog.setContentView(R.layout.dialog_box);
                    int width = 0;
                    int height =0;


                    Point size = new Point();
                    WindowManager w =getWindowManager();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                        w.getDefaultDisplay().getSize(size);
                        width = size.x;
                        height = size.y;
                    } else {
                        Display d = w.getDefaultDisplay();
                        width = d.getWidth();
                        height   = d.getHeight();;
                    }


                    TextView dtv = (TextView) dialog.findViewById(R.id.tv1);

                    TextView txt = (TextView) dialog.findViewById(R.id.tv);

                    txt.setText(Html.fromHtml("You are allowed to use app only on one device"));
                    dtv.setText(Html.fromHtml("Alert"));
                    Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PackgedetailActivity.this.finish();
                            Intent intent = new Intent(
                                    PackgedetailActivity.this,
                                    LoginFragmentActivity.class);
                            intent.putExtra("from", "2");
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    //(width/2)+((width/2)/2)
                    //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);

                }
            } else {
                //mProgressHUD.dismiss();
            }

        }



        @Override
        protected Void doInBackground(String... params) {
            try {

                //	Log.i("START",">>>>>>>START<<<<<<<<");
                SMSAuthenticationCaller smsCaller = new SMSAuthenticationCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_GET_SMS_AUTHENTICATION)
                );
                smsCaller.PhoneUniqueId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                smsCaller.MemberId = MemberId;
                smsCaller.OneTimePwd=otp_password;
                smsCaller.setCallData("changepack");
                smsCaller.join();
                smsCaller.start();
                rslt = "START";

                while (rslt.equalsIgnoreCase("START")){
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {

            }
            return null;
        }
        @Override
        protected void onCancelled() {
            //mProgressHUD.dismiss();
            mProgressHUD.dismiss();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

/*		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}*/

    }





    public class SettingResultAsyncTask extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        String Arearslt="";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(PackgedetailActivity.this,getString(R.string.app_please_wait_label), true,false, this);
            super.onPreExecute();
            settingResult="";
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            AreaWiseSettingSOAP areaWiseSettingSOAP= new AreaWiseSettingSOAP(
                    getApplicationContext().getResources().getString(
                            R.string.WSDL_TARGET_NAMESPACE),
                    getApplicationContext().getResources().getString(
                            R.string.SOAP_URL), getApplicationContext()
                    .getResources().getString(
                            R.string.METHOD_GET_AREA_SETTING)
            );
            SharedPreferences sharedPreferences1 = getApplicationContext()
                    .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
            String areaCode=sharedPreferences1.getString("AreaCode", "0");

            try {
                Arearslt=	areaWiseSettingSOAP.getAreaWiseSetting(areaCode, "UP");
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

            if(is_activity_running)
                mProgressHUD.dismiss();
            super.onPostExecute(result);
            if(Arearslt.length()>0){
                if(Arearslt.equalsIgnoreCase("ok")){
                    if(settingResult.equalsIgnoreCase("0")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                PackgedetailActivity.this);
                        builder.setMessage(
                                "This Facility is not available !! ")
                                .setTitle("Alert")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                PackgedetailActivity.this.finish();
                                                Intent intent = new Intent(
                                                        PackgedetailActivity.this,
                                                        IONHome.class);
                                                startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                            new PackageListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                            new PackageListWebService().execute();
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            PackgedetailActivity.this);
                    builder.setMessage(
                            "Please try again!! ")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // Toast.makeText(NotificationListActivity.this,
                                            // ""+selectedFromList.getNotifyId(),
                                            // Toast.LENGTH_SHORT).show();

                                            PackgedetailActivity.this.finish();
                                            Intent intent = new Intent(
                                                    PackgedetailActivity.this,
                                                    IONHome.class);

                                            startActivity(intent);
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PackgedetailActivity.this);
                builder.setMessage(
                        "Please try again!!  ")
                        .setTitle("Alert")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // Toast.makeText(NotificationListActivity.this,
                                        // ""+selectedFromList.getNotifyId(),
                                        // Toast.LENGTH_SHORT).show();

                                        PackgedetailActivity.this.finish();
                                        Intent intent = new Intent(
                                                PackgedetailActivity.this,
                                                IONHome.class);

                                        startActivity(intent);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }



    }

    private class PackageListWebService extends AsyncTask<String, Void, Void>implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        String ConnectionTypeId="",AreaId="";

        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(PackgedetailActivity.this,getString(R.string.app_please_wait_label),true,true,this);
            SharedPreferences sharedPreferences1 = getApplicationContext()
                    .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
            ConnectionTypeId=sharedPreferences1.getString("ConnectionTypeId", "0");
            AreaId=sharedPreferences1.getString("AreaCode", "0");
        }
        @Override
        protected void onCancelled() {
            if(is_activity_running)
                mProgressHUD.dismiss();
            packageListWebService = null;
        }
        protected void onPostExecute(Void unused) {


            packageListWebService = null;
            if(is_activity_running)
                mProgressHUD.dismiss();

          /*  if (rslt.trim().equalsIgnoreCase("ok")) {
                writeXMLFile();
            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }*/
            //Log.e("rslt",":"+rslt);
            Log.e("XML File",":"+ PackgedetailActivity.strXML);
            setPackageList(PackgedetailActivity.strXML);


            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                PackgeCaller caller = new PackgeCaller(getApplicationContext()
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_PACKAGELIST_BY_CONNECTIONTYPEID));
                caller.setMemberId(Long.parseLong(MemberId));
                caller.setConnectionTypeId(ConnectionTypeId);
                caller.setAreaCode(AreaId);
                caller.join();
                caller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
            }
            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
        }
    }



    private void writeXMLFile() {
        try {
            File direct = new File(extStorageDirectory + "/" + xml_folder);
            if (!direct.exists()) {
                direct.mkdirs(); // directory is created;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        FileOutputStream fOut = null;
        OutputStreamWriter xmlOutWriter = null;

        try {
            File xmlFile = new File(xml_file_with_path);
            xmlFile.createNewFile();
            fOut = new FileOutputStream(xmlFile);
            xmlOutWriter = new OutputStreamWriter(fOut);
            xmlOutWriter.append(strXML);


        }catch(IOException io){
            io.printStackTrace();
            Toast.makeText(getBaseContext(), io.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } finally {
            try {
                if(xmlOutWriter != null){
                    xmlOutWriter.flush();
                    xmlOutWriter.close();
                }
                if(xmlOutWriter != null){
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException io) {
            }
            strXML = null;
        }
        //setPackageList();
    }

    private void setPackageList(String str_xml ) {

        try {
            //String str_xml = readPackageListXML();
            //Utils.log("Parsing String XML", ":"+str_xml);
            PackageListParsing packageList = new PackageListParsing(str_xml);
            Utils.log("packageList", ":"+packageList);
            mapPackageList = packageList.getMapPackageList();
            Utils.log("mapPackageList", ":"+mapPackageList.size());

            Iterator it = mapPackageList.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //Integer key = (Integer) pair.getKey();
                PackageList package_List = (PackageList) pair.getValue();
                ArrayList<PackageList> al_sorted ;

                if(hm_packge_list.containsKey(package_List.getPackagevalidity())){
                    ArrayList<PackageList> al_pkg_list = hm_packge_list.get(package_List.getPackagevalidity());
                    al_pkg_list.add(package_List);

                   /* Collections.sort(al_pkg_list, new Comparator<PackageList>() {
                        @Override
                        public int compare(PackageList lhs, PackageList rhs) {
                            return lhs.getPackageRate().compareTo(rhs.getPackageRate());
                        }
                    });*/
                    hm_packge_list.put(package_List.getPackagevalidity(),al_pkg_list) ;
                }else{
                    ArrayList<PackageList> al_package_list = new ArrayList<>();
                    al_package_list.add(package_List);

                    /*Collections.sort(al_package_list, new Comparator<PackageList>() {
                        @Override
                        public int compare(PackageList lhs, PackageList rhs) {
                            return lhs.getPackageRate().compareTo(rhs.getPackageRate());
                        }
                    });*/
                    hm_packge_list.put(package_List.getPackagevalidity(),al_package_list) ;
                }


//                Iterator iterator = hm_packge_list.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry me2 = (Map.Entry) iterator.next();
//                    //System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
//                    String key =  (String) me2.getKey();
//                    ArrayList<PackageList> al_apckage =  ( ArrayList<PackageList>) me2.getValue();
//
//                    Log.e("key",":"+key);
//                    for (int i = 0; i <al_apckage.size() ; i++) {
//                        Log.e("value",":"+al_apckage.get(i).getPlanName());
//                        Log.e("value",":"+al_apckage.get(i).getPackageRate());
//                        Log.e("value",":"+al_apckage.get(i).getPackagevalidity());
//
//                    }
//
//                }


                it.remove(); // avoids a ConcurrentModificationException
            }


            final ArrayAdapter adapterForSpinner = new ArrayAdapter(
                    PackgedetailActivity.this,
                    android.R.layout.simple_spinner_item);
            // adapterForSpinner.add("Select Days");

            Set<String> keys = hm_packge_list.keySet();
            Iterator<String> i = keys.iterator();
            final List<Integer> unsortList  = new ArrayList<Integer>();

            while (i.hasNext()) {
                String key = (String) i.next();
                if(!unsortList.contains(key))
                    unsortList.add(Integer.parseInt(key));
            }
            Collections.sort(unsortList);
            for(Integer temp: unsortList){
                CharSequence textHolder = "" + temp;
                adapterForSpinner.add(textHolder);
            }

            planList.setAdapter(adapterForSpinner);
            gridViewSetting(unsortList);
            setGrid("");
            final List<Integer> final_pkg_list1 = unsortList;


            gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CardView tv = (CardView) view.findViewById(R.id.card_view);
                    tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));

                    LinearLayout previousSelectedView = (LinearLayout) gridview1.getChildAt(previousSelectedPosition);
                    gridview1.getChildAt(previousSelectedPosition);

                    if (previousSelectedPosition != -1)
                    {
                        // previousSelectedView.setSelected(false);
                        CardView cardView = (CardView) previousSelectedView.findViewById(R.id.card_view);
                        cardView.setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
                        tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
                    }

                    previousSelectedPosition = position;

                    if(position!=0) {
                        is_selected = false;
                    }
                    else
                        is_selected =true;


                    if(is_selected)
                        gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.pkg6));
                    else
                        gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
                    setGrid(((TextView) view.findViewById( R.id.tv_days )) .getText().toString());
                }

            });



        } catch (Exception e) {
            e.printStackTrace();
            AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
                    context);
        }
    }


    private void gridViewSetting(List<Integer> unsortList) {

        // this is size of your list with data
        int size = unsortList.size();
        // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
        int width = 100 ;

        // than just calculate sizes for layout params and use it
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview1.setLayoutParams(params);
        gridview1.setColumnWidth(singleItemWidth);
        gridview1.setHorizontalSpacing(2);
        gridview1.setStretchMode(GridView.STRETCH_SPACING);
        gridview1.setNumColumns(size);
        SpinnerDaysAdapter adapter = new SpinnerDaysAdapter(PackgedetailActivity.this, unsortList);
        gridview1.setAdapter(adapter);



       /* LinearLayout linear = (LinearLayout) gridview1.getChildAt(0);
        CardView cardView = (CardView) linear.findViewById(R.id.card_view);
        cardView.setBackgroundColor(context.getResources().getColor(R.color.pkg6));*/
    }


    public void setGrid(String days){
        String final_day_count;
        if(days.length()>0){
            String selecte_plan = days;
            String array[] = selecte_plan.split(" ");
            final_day_count = array[0];
        }else{
            String selecte_plan = planList.getSelectedItem().toString();
            String array[] = selecte_plan.split(" ");
            final_day_count = array[0];
        }

        Iterator it1 = hm_packge_list.entrySet().iterator();
        ArrayList<PackageList> package_List,final_pkg_list = null,last_final_pkg = new ArrayList<>();

        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it1.next();
            String key = (String) pair.getKey();
            package_List = (ArrayList<PackageList>) pair.getValue();

            if(key.equals(final_day_count)){
                final_pkg_list = package_List;
            }
        }

        List<Integer> list  = new ArrayList<>();
        for (int i = 0; i < final_pkg_list.size(); i++) {
            list.add(Integer.parseInt(String.valueOf(final_pkg_list.get(i).getSrno())));
        }

        System.out.println("List in Java sorted in  order: " + list);
        Collections.sort(list);
        for (int j = 0; j < list.size(); j++) {
            System.out.println("List in Java sorted in ascending order: " + list);
        }

        for (int k = 0; k < list.size(); k++) {
            for (int i = 0; i < final_pkg_list.size(); i++) {
                if (list.get(k).equals(Integer.parseInt(String.valueOf(final_pkg_list.get(i).getSrno())))) {
                    last_final_pkg.add(final_pkg_list.get(i));
                }
            }
        }

        for (int i = 0; i < last_final_pkg.size(); i++) {
            Log.e("price", ":" +last_final_pkg.get(i).getSrno() +"----"+ last_final_pkg.get(i).getPackageId() + "--------" + last_final_pkg.get(i).getPackageRate() + "--------" + last_final_pkg.get(i).getPackagevalidity());
        }

        gridview.setAdapter(  new CustomGridAdapter( this, last_final_pkg));

        final ArrayList<PackageList> final_pkg_list1 = last_final_pkg;
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(),((TextView) v.findViewById( R.id.tv_pkg_name )) .getText(), Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(PackgedetailActivity.this,ChangePackage_NewActivity.class);
                intent.putExtra("Selected_Pkg", (Serializable) final_pkg_list1.get(position));
                startActivity(intent);
            }
        });

    }

    private String readPackageListXML() {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String str_xml = "";

        try {
            br = new BufferedReader(new FileReader(new File(xml_file_with_path)));
            // br = new BufferedReader(new
            // InputStreamReader(getAssets().open("xml/PackageList.xml")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
            str_xml = sb.toString();
            // Log.i(">>>XML <<< ", str_xml);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PackgedetailActivity.this,
                    "PackageList File Not Found.", Toast.LENGTH_LONG).show();
            //Toast.makeText(PackgedetailActivity.this,"Create file mypage/PackageList.xml", Toast.LENGTH_LONG).show();

        } finally {
            try {
                if(br != null)
                    br.close(); // stop reading
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return str_xml;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //this.finish();
        is_activity_running=false;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        is_activity_running=true;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        PackgedetailActivity.this.finish();
        if(check_intent){
            this.finish();
            //Intent i = new Intent(ChangePackage_NewActivity.this,RenewPackage.class);
            //startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else{

            Intent i = new Intent(PackgedetailActivity.this,IONHome.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

//*******************************Check Valid User Web Service*********ends here**************************

}



