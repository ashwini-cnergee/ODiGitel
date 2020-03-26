package com.cnergee.mypage;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avenues.lib.utility.AvenuesParams;
import com.cnergee.mypage.SOAP.GetAtomSignatureSoap;
import com.cnergee.mypage.caller.BeforePaymentInsertCaller;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.caller.PaymentGatewayCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import all.interface_.IError;
import cnergee.krishna.ookla.odigitel.R;


public class MakeMyPayment_Atom extends BaseActivity implements OnCancelListener {

    LinearLayout linnhome, linnprofile, linnnotification, linnhelp, llClickDetails, ll_addtional_details;
    TextView txtloginid, txtemailid, txtcontactno, txtnewpackagename, txtnewamount, txtnewvalidity, tvDiscountLabel;

    CheckBox privacy, terms;
    private String sharedPreferences_name;
    Button btnnb;
    String ServiceTax, UpdateFrom, discount, ClassName;
    public boolean is_member_details = false, is_activity_running = false, trackid_check = false;
    public static boolean Changepack;
    public boolean is_payemnt_detail = false;
    public boolean isDetailShow = false;
    boolean isLogout = false;
    public long memberid;
    private ScrollView payNowView, responseScrollLayout;
    String TrackId;

    public static String adjTrackval = "";
    public static String responseMsg = ""; public static String rslt = "";
    public static Map<String, MemberDetailsObj> mapMemberDetails;
    private String customername, Email_id;

    AdditionalAmount additionalAmount;
    MemberDetailsObj memberDetails;
    Bundle bundle;

    private PaymentGateWayDetails getpaymentgatewaysdetails = null;
    private InsertBeforePayemnt InsertBeforePayemnt = null;
    private GetMemberDetailWebService getMemberDetailWebService = null;

    public static Context context;
    Utils utils = new Utils();

    private static int ACC_ID = 0000;
    private static String SECRET_KEY = "";
    private static String HOST_NAME = "";
    //private static final double PER_UNIT_PRICE = 12.34;
    ArrayList<HashMap<String, String>> custom_post_parameters;
    String atom_url ="",atom_acces_code ="",atom_error="",atom_track_id= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makemypayments);
        iError = (IError) this;
        initControls();
        Utils.log("ClassName", ":" + MakeMyPayment_Atom.class.getSimpleName());
    }

    public void initControls() {
        linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
        linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
        linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
        linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);

        llClickDetails = (LinearLayout) findViewById(R.id.llClickDetails);
        ll_addtional_details = (LinearLayout) findViewById(R.id.ll_addtional_details);

        txtloginid = (TextView) findViewById(R.id.txtloginid);
        txtemailid = (TextView) findViewById(R.id.txtemailid);
        txtcontactno = (TextView) findViewById(R.id.txtcontactno);
        txtnewpackagename = (TextView) findViewById(R.id.txtnewpackagename);
        txtnewamount = (TextView) findViewById(R.id.txtnewamount);
        txtnewvalidity = (TextView) findViewById(R.id.txtnewvalidity);

        tvDiscountLabel = (TextView) findViewById(R.id.tvDiscountLabel);

        privacy = (CheckBox) findViewById(R.id.privacy);
        terms = (CheckBox) findViewById(R.id.terms);

        btnnb = (Button) findViewById(R.id.btnnb);

        payNowView = (ScrollView) findViewById(R.id.payNowLayout);
        responseScrollLayout = (ScrollView) findViewById(R.id.responseScrollLayout);

        btnnb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    if (Double.parseDouble(txtnewamount.getText().toString()) > 0) {
                        if (terms.isChecked() == true
                                && privacy.isChecked() == true) {

                            if (Utils.isOnline(MakeMyPayment_Atom.this)) {
                                if (trackid_check) {
                                    is_member_details = false;
                                    // TrackId Generated Successfully.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        new InsertBeforePayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                                    } else {
                                        new InsertBeforePayemnt().execute((String) null);
                                    }
                                } else {
                                    // TrackId Failed to Generate.
                                    is_member_details = true;
                                    if (Utils.isOnline(MakeMyPayment_Atom.this)) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                            getpaymentgatewaysdetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                                        } else {
                                            getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                            getpaymentgatewaysdetails.execute((String) null);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(MakeMyPayment_Atom.this,
                                        getString(R.string.app_please_wait_label),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MakeMyPayment_Atom.this,
                                    "Please accept the terms and condition",
                                    Toast.LENGTH_LONG).show();
                            return;

                        }

                    } else {
                        if (is_activity_running)
                            AlertsBoxFactory
                                    .showAlert(
                                            "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                            MakeMyPayment_Atom.this);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    if (is_activity_running)
                        AlertsBoxFactory
                                .showAlert(
                                        "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                        MakeMyPayment_Atom.this);
                }

            }
        });


        linnhome.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MakeMyPayment_Atom.this.finish();
                Intent i = new Intent(MakeMyPayment_Atom.this, IONHome.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });

        linnprofile.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MakeMyPayment_Atom.this.finish();
                Intent i = new Intent(MakeMyPayment_Atom.this, Profile.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });

        linnnotification.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MakeMyPayment_Atom.this.finish();
                Intent i = new Intent(MakeMyPayment_Atom.this,
                        NotificationListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });


        linnhelp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MakeMyPayment_Atom.this.finish();
                Intent i = new Intent(MakeMyPayment_Atom.this, HelpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });


        Intent intent = getIntent();
        bundle = intent.getExtras();

        txtnewpackagename.setText(bundle.getString("PackageName"));
        txtnewvalidity.setText(bundle.getString("PackageValidity"));
        ServiceTax = bundle.getString("ServiceTax");
        UpdateFrom = bundle.getString("updateFrom");
        discount = bundle.getString("discount");
        ClassName = bundle.getString("ClassName");
        additionalAmount = (AdditionalAmount) bundle.getSerializable("addtional_amount");

        if (bundle.getString("datafrom").equalsIgnoreCase("changepack")) {
            Changepack = true;
            tvDiscountLabel.setVisibility(View.GONE);
        } else {
            Changepack = false;
            Utils.log("Renew", "account");
            tvDiscountLabel.setVisibility(View.VISIBLE);
        }

        if (additionalAmount != null) {
            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    // tvDiscountLabel.setText("You have got "+additionalAmount.getDiscountPercentage()+"% discount for online payment.");
                    tvDiscountLabel.setText("Avail of a "
                            + additionalAmount.getDiscountPercentage()
                            + "% discount by paying online.");
                } else {
                    tvDiscountLabel.setVisibility(View.GONE);
                }
            } else {
                tvDiscountLabel.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                txtnewamount.setText(additionalAmount.getFinalcharges());
            }
            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (is_payemnt_detail) {
                txtnewamount.setText(additionalAmount.getFinalcharges());
                llClickDetails.setVisibility(View.VISIBLE);
            } else {
                llClickDetails.setVisibility(View.GONE);
            }
        } else {
            tvDiscountLabel.setVisibility(View.GONE);
        }


        txtnewamount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (is_payemnt_detail) {
                    showPaymentDetailsDialog(additionalAmount);
                }
            }
        });


        llClickDetails.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (is_payemnt_detail) {
                    if (isDetailShow) {
                        ll_addtional_details.setVisibility(View.GONE);
                        isDetailShow = false;
                    } else {
                        ll_addtional_details.setVisibility(View.VISIBLE);
                        isDetailShow = true;
                    }
                    showPaymentDetails(additionalAmount);

                } else {
                    ll_addtional_details.setVisibility(View.GONE);
                }
            }
        });

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        utils.setSharedPreferences(sharedPreferences);
        memberid = Long.parseLong(utils.getMemberId());


        if (Utils.isOnline(MakeMyPayment_Atom.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

            } else {
                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);
            }

/*
			if (Utils.isOnline(MakeMyPayment_EBS.this)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					getpaymentgatewaysdetails = new PaymentGateWayDetails();
					getpaymentgatewaysdetails.executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

				} else {
					getpaymentgatewaysdetails = new PaymentGateWayDetails();
					getpaymentgatewaysdetails.execute((String) null);
				}
			} else {
				if (is_activity_running)
					AlertsBoxFactory.showAlert("Please connect to internet !!", MakeMyPayment_EBS.this);
			}*/

            payNowView.setVisibility(View.VISIBLE);
            responseScrollLayout.setVisibility(View.GONE);
        } else {
            if (is_activity_running)
                AlertsBoxFactory.showAlert("Please connect to internet !!", MakeMyPayment_Atom.this);
        }

    }


    public void showPaymentDetailsDialog(AdditionalAmount additionalAmount) {
        if (additionalAmount != null) {
            final Dialog dialog = new Dialog(MakeMyPayment_Atom.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_additional_amount);

            int width = 0;
            int height = 0;

            Point size = new Point();
            WindowManager w = ((Activity) MakeMyPayment_Atom.this)
                    .getWindowManager();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                w.getDefaultDisplay().getSize(size);
                width = size.x;
                height = size.y;
            } else {
                Display d = w.getDefaultDisplay();
                width = d.getWidth();
                height = d.getHeight();
                ;
            }

            LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

            TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

            ll_package_rate = (LinearLayout) dialog
                    .findViewById(R.id.ll_package_rate);
            ll_add_amt = (LinearLayout) dialog.findViewById(R.id.ll_add_amt);
            ll_add_reason = (LinearLayout) dialog
                    .findViewById(R.id.ll_add_reason);
            ll_discount_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_discount_amt);
            ll_fine_amount = (LinearLayout) dialog
                    .findViewById(R.id.ll_fine_amt);
            ll_days_fine_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_days_fine_amt);
            ll_discount_per = (LinearLayout) dialog
                    .findViewById(R.id.ll_discount_per);
            ll_final_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_final_amount);

            tv_package_rate = (TextView) dialog.findViewById(R.id.tv_package_rate);
            tv_add_amt = (TextView) dialog.findViewById(R.id.tv_add_amt);
            tv_add_reason = (TextView) dialog.findViewById(R.id.tv_add_reason);
            tv_discount_amt = (TextView) dialog.findViewById(R.id.tv_discount_amt);
            tv_fine_amount = (TextView) dialog.findViewById(R.id.tv_fine_amt);
            tv_days_fine_amt = (TextView) dialog.findViewById(R.id.tv_days_fine_amt);
            tv_discount_per = (TextView) dialog.findViewById(R.id.tv_discount_per);
            tv_final_amt = (TextView) dialog.findViewById(R.id.tv_final_amount);

            if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
                ll_package_rate.setVisibility(View.VISIBLE);
                tv_package_rate.setText(additionalAmount.getPackageRate());
            } else {
                ll_package_rate.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
                ll_add_amt.setVisibility(View.VISIBLE);
                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
            } else {
                ll_add_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getAdditionalAmountType().length() > 0) {
                ll_add_reason.setVisibility(View.GONE);
                tv_add_reason.setText(additionalAmount.getAdditionalAmountType());
            } else {
                ll_add_reason.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                ll_discount_amt.setVisibility(View.VISIBLE);
                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
            } else {
                ll_discount_amt.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                ll_fine_amount.setVisibility(View.VISIBLE);
                tv_fine_amount.setText(additionalAmount.getFineAmount());
            } else {
                ll_fine_amount.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                ll_days_fine_amt.setVisibility(View.VISIBLE);
                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
            } else {
                ll_days_fine_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    ll_discount_per.setVisibility(View.VISIBLE);
                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
                } else {
                    ll_discount_per.setVisibility(View.GONE);
                }
            } else {
                ll_discount_per.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                ll_final_amt.setVisibility(View.VISIBLE);
                tv_final_amt.setText(additionalAmount.getFinalcharges());
            } else {
                ll_final_amt.setVisibility(View.GONE);
            }
            Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
                    LayoutParams.WRAP_CONTENT);
            dialog.show();

        }
    }


    public void showPaymentDetails(AdditionalAmount additionalAmount) {
        if (additionalAmount != null) {
            // ll_addtional_details.setVisibility(View.VISIBLE);
            LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

            TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

            ll_package_rate = (LinearLayout) findViewById(R.id.ll_package_rate);
            ll_add_amt = (LinearLayout) findViewById(R.id.ll_add_amt);
            ll_add_reason = (LinearLayout) findViewById(R.id.ll_add_reason);
            ll_discount_amt = (LinearLayout) findViewById(R.id.ll_discount_amt);
            ll_fine_amount = (LinearLayout) findViewById(R.id.ll_fine_amt);
            ll_days_fine_amt = (LinearLayout) findViewById(R.id.ll_days_fine_amt);
            ll_discount_per = (LinearLayout) findViewById(R.id.ll_discount_per);
            ll_final_amt = (LinearLayout) findViewById(R.id.ll_final_amount);

            tv_package_rate = (TextView) findViewById(R.id.tv_package_rate);
            tv_add_amt = (TextView) findViewById(R.id.tv_add_amt);
            tv_add_reason = (TextView) findViewById(R.id.tv_add_reason);
            tv_discount_amt = (TextView) findViewById(R.id.tv_discount_amt);
            tv_fine_amount = (TextView) findViewById(R.id.tv_fine_amt);
            tv_days_fine_amt = (TextView) findViewById(R.id.tv_days_fine_amt);
            tv_discount_per = (TextView) findViewById(R.id.tv_discount_per);
            tv_final_amt = (TextView) findViewById(R.id.tv_final_amount);

            if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
                ll_package_rate.setVisibility(View.VISIBLE);
                tv_package_rate.setText(additionalAmount.getPackageRate());
            } else {

                ll_package_rate.setVisibility(View.GONE);

            }

            if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
                ll_add_amt.setVisibility(View.VISIBLE);
                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
            } else {
                ll_add_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getAdditionalAmountType().length() > 0) {
                ll_add_reason.setVisibility(View.GONE);
                tv_add_reason.setText(additionalAmount
                        .getAdditionalAmountType());
            } else {
                ll_add_reason.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                ll_discount_amt.setVisibility(View.VISIBLE);
                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
            } else {
                ll_discount_amt.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                ll_fine_amount.setVisibility(View.VISIBLE);
                tv_fine_amount.setText(additionalAmount.getFineAmount());
            } else {
                ll_fine_amount.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                ll_days_fine_amt.setVisibility(View.VISIBLE);
                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
            } else {
                ll_days_fine_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    ll_discount_per.setVisibility(View.VISIBLE);
                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
                } else {
                    ll_discount_per.setVisibility(View.GONE);
                }
            } else {
                ll_discount_per.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                ll_final_amt.setVisibility(View.VISIBLE);
                tv_final_amt.setText(additionalAmount.getFinalcharges());
            } else {
                ll_final_amt.setVisibility(View.GONE);
            }
        } else {
            ll_addtional_details.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }


    private class GetMemberDetailWebService extends
            AsyncTask<String, Void, Void> implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakeMyPayment_Atom.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            Utils.log("1 Progress", "start");
        }

        @Override
        protected Void doInBackground(String... params) {
            {
                try {
                    MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
                            getApplicationContext().getResources().getString(
                                    R.string.WSDL_TARGET_NAMESPACE),
                            getApplicationContext().getResources().getString(
                                    R.string.SOAP_URL), getApplicationContext()
                            .getResources().getString(
                                    R.string.METHOD_SUBSCRIBER_DETAILS));

                    memberdetailCaller.memberid = memberid;

                    memberdetailCaller.setAllData(false);
                    memberdetailCaller.setTopup_flag(false);
                    memberdetailCaller.join();
                    memberdetailCaller.start();
                    rslt = "START";

                    while (rslt == "START") {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }


        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            if (is_activity_running)
                mProgressHUD.dismiss();
            Utils.log("1 Progress", "end");
            try {
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (mapMemberDetails != null) {

                        Set<String> keys = mapMemberDetails.keySet();
                        Utils.log("KEY_SET", ":" + keys.size());

                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                            str_keyVal = (String) i.next();
                        }
                        String selItem = str_keyVal.trim();
                        isLogout = false;
                        // finish();
                        memberDetails = mapMemberDetails.get(selItem);
                        txtloginid.setText(memberDetails.getMemberLoginId());
                        txtemailid.setText(memberDetails.getEmailId());
                        txtcontactno.setText(memberDetails.getMobileNo());
                        customername = memberDetails.getMemberName();
                        Utils.log("customername", ":" + customername);

						/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							getpaymentgatewaysdetails = new PaymentGateWayDetails();
							getpaymentgatewaysdetails.executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
						} else {
							getpaymentgatewaysdetails = new PaymentGateWayDetails();
							getpaymentgatewaysdetails.execute((String) null);
						}*/
                    }
                } else if (rslt.trim().equalsIgnoreCase("not")) {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert("Subscriber Not Found !!! ", context);
                } else {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert(rslt, context);
                }
            } catch (Exception e) {
                if (is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
            }
        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();
            getMemberDetailWebService = null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }


    private class PaymentGateWayDetails extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakeMyPayment_Atom.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            Utils.log("2 Progress", "start");
            Utils.log("Atom", ":" + Utils.is_atom);

            TrackId = "";
        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();

            getpaymentgatewaysdetails = null;

        }

        protected void onPostExecute(Void unused) {
            Utils.log("2 Progress", "end");

            if (is_activity_running) mProgressHUD.dismiss();
            getpaymentgatewaysdetails = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                try {
                    TrackId = adjTrackval;
                    Utils.log("TrackId", ":" + TrackId);
                    if (TrackId.length() > 0) {
                        trackid_check = true;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new InsertBeforePayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                        } else {
                            new InsertBeforePayemnt().execute((String) null);
                        }
                    }

                    // Log.i(">>>>TrackId<<<<", TrackId);
                    Utils.log("trackid_check", ":" + trackid_check);
                    if (is_member_details) {

                    }
                } catch (NumberFormatException nue) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
                    radioGroup.clearCheck();
                    // Log.i(">>>>>New PLan Rate<<<<<<", adjTrackval);
                }

            } else {
                if (is_activity_running)
                    iError.display();
            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                PaymentGatewayCaller adjCaller = new PaymentGatewayCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_GET_TRANSACTIONID_WITH_BANK_NAME), "AT");
                adjCaller.setMemberId(utils.getMemberId());
                // adjCaller.setAreaCode(AreaCode);
                // adjCaller.setAreaCodeFilter(AreaCodeFilter);
                adjCaller.setTopup_falg(false);

                adjCaller.join();
                adjCaller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {
				/*
				 * AlertsBoxFactory.showErrorAlert("Error web-service response "
				 * + e.toString(), context);
				 */
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }


    private class InsertBeforePayemnt extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        protected void onPreExecute() {
            Utils.log("3 Progress", "start");
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakeMyPayment_Atom.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);

        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();
            InsertBeforePayemnt = null;
            // submit.setClickable(true);
        }

        protected void onPostExecute(Void unused) {
            if (is_activity_running)
           mProgressHUD.dismiss();
            Utils.log("3 Progress", "end");
            Utils.log("Response", ":" + rslt);
            // submit.setClickable(true);
            InsertBeforePayemnt = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new Get_Atom_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                } else {
                    new Get_Atom_Signature().execute();
                }

            } else {
                if (is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
                return;
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());

                BeforePaymentInsertCaller caller = new BeforePaymentInsertCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(R.string.METHOD_BEFORE_MEMBER_PAYMENTS_NEW), true);

                paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
                paymentsObj.setTrackId(TrackId);
                paymentsObj.setAmount(txtnewamount.getText().toString().trim());
                paymentsObj.setPackageName(txtnewpackagename.getText().toString());
                paymentsObj.setServiceTax(ServiceTax);
                paymentsObj.setDiscount_Amount(additionalAmount.getDiscountAmount());
                if (Utils.pg_sms_request) {
                    if (Utils.pg_sms_uniqueid.length() > 0) {
                        paymentsObj.setPg_sms_unique_id(Utils.pg_sms_uniqueid);
                    } else {
                        paymentsObj.setPg_sms_unique_id(null);
                    }
                } else {
                    paymentsObj.setPg_sms_unique_id(null);
                }
                caller.setPaymentdata(paymentsObj);

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
				/* AlertsBoxFactory.showAlert(rslt,context ); */
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }



    private class Get_Atom_Signature extends AsyncTask<String , Void , Void> implements OnCancelListener{

        GetAtomSignatureSoap getAtomSignatureSoap;
        String getatomResult = "";
        String response = "";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD
                    .show(MakeMyPayment_Atom.this,
                            getString(R.string.app_please_wait_label), true,
                            true, this);
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressHUD.dismiss();
            Utils.log("OnPostAtom","OnPostAtom");
            if(getatomResult!=null&&getatomResult.length()>0){
                if(response!=null&&response.length()>0){

                    try {
                        JSONObject mainobjectJson = new JSONObject(response);
                        if(mainobjectJson.has("NewDataSet")){
                            JSONObject newsetJsonobject = mainobjectJson.getJSONObject("NewDataSet");
                            if(newsetJsonobject.has("Table1")){
                                JSONObject tableJson=newsetJsonobject.getJSONObject("Table1");
                                if (tableJson.has("TrackId"))
                                    TrackId = tableJson.getString("TrackId");
                                else
                                    atom_acces_code = tableJson.getString("AccessCode");
                                atom_url = tableJson.getString("EncRequest");
                                atom_error = tableJson.getString("IsError");
                                atom_track_id = tableJson.getString("TrackId");

                                Utils.log("Atom URL",""+atom_url);
                                Utils.log("Atom AccessCode",""+atom_acces_code);
                                Utils.log("Atom error", ""+atom_error);
                                Utils.log("Atom TrackId",""+atom_track_id);
                            }

                            if (atom_error.equalsIgnoreCase("1")) {
                                if (utils.EmailId == null) {
                                    AlertsBoxFactory.showAlert("Please Update your EmailId from Update Profile.", MakeMyPayment_Atom.this);
                                }
                                AlertsBoxFactory.showAlert("We are facing some technical problem. Please Try Again !!!", MakeMyPayment_Atom.this);

                            }else{
                                MakeMyPayment_Atom.this.finish();
                                Intent i = new Intent(MakeMyPayment_Atom.this,WebView_AtomPayments.class);
                                i.putExtra("returnUrl",atom_url);
                                i.putExtra("trackId",atom_track_id);
                                i.putExtra("Error",atom_error);
                                i.putExtra("AccessCode", atom_acces_code);
                                i.putExtra(AvenuesParams.ACCESS_CODE, atom_acces_code);
                                i.putExtra(AvenuesParams.AMOUNT, txtnewamount.getText().toString());
                                i.putExtra("TrackId", TrackId);
                                i.putExtra("additional_amount", additionalAmount);
                                i.putExtra("Changepack", Changepack);
                                i.putExtra("UpdateFrom", UpdateFrom);
                                i.putExtra("PackageName", txtnewpackagename.getText().toString());
                                startActivity(i);
                            }
                        }else{
                            AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakeMyPayment_Atom.this);
                        }

                    } catch (Exception e) {
                        Utils.log("Error",":"+e);
                    }

                }else{
                    AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakeMyPayment_Atom.this);
                }
            }else {
                AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakeMyPayment_Atom.this);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                Utils.log("Amount", "" + txtnewamount.getText().toString());
                getAtomSignatureSoap = new GetAtomSignatureSoap(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.SOAP_URL),getString(R.string.METHOD_GET_ATOM_SIGNATURE_LIVE));
                getatomResult = getAtomSignatureSoap.GetAtomSignatureResult(memberid,txtnewamount.getText().toString(),TrackId);
                response = getAtomSignatureSoap.getResponse();
                Utils.log("Amount",""+txtnewamount.getText().toString());
            }catch(Exception e){
                Utils.log("Error",":"+e);
            }
            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }

/*
    public  void showExitAlert(String message, Context ctx){
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.custom_dialog_box);
			*//*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);*//*
        int width = 0;
        int height =0;
        dialog.setCancelable(false);

        Point size = new Point();
        WindowManager w =((Activity)ctx).getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height   = d.getHeight();;
        }

        TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

        TextView txt = (TextView) dialog.findViewById(R.id.tv);

        txt.setText(Html.fromHtml(message));

        Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeMyPayment_Atom.this.finish();

					*//*Intent i = new Intent(MakeMyPaymentsAtom.this,IONHome.class);
					dialog.dismiss();
					startActivity(i);*//*
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        dialog.show();
        //(width/2)+((width/2)/2)
        //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
    }*/

}
