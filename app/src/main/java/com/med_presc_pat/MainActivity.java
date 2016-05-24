package com.med_presc_pat;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.med_presc_pat.GetSet.PatientRegistrationGetSet;
import com.med_presc_pat.SpinnerAdapters.State;
import com.med_presc_pat.SpinnerAdapters.District;
import com.med_presc_pat.Utilities.DbConstant;
import com.med_presc_pat.Utilities.DbHandler;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.NumberFormat;
import java.util.Calendar;
import android.os.Handler;
import android.widget.Toast;

import java.util.logging.LogRecord;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;
import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,OTPListener {
    DbHandler db;
    MaterialEditText etName,etPhone,etEmail,etDOB,etAddress;

    MaterialSpinner sp_District,sp_State;
    Context context;
    ActionProcessButton btnSignIn;
    ArrayAdapter<District> districtAdapter;
    ArrayAdapter<State> stateAdapter;
    String[] initDistrict = {"District"};
    Boolean state_spinner_flag=false,District_spinnewr_flag=false;
    PatientRegistrationGetSet getset;
    MaterialDialog processdialog;
    SharedPreferences prefs;
    Boolean otpaccepted=false;
    String glbl_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initialize();
        setStateSpinner();
        setDistrictSpinner(initDistrict);
        getset = new PatientRegistrationGetSet();
        OtpReader.bind(this,"MEDEPR");



        sp_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setDistrictSpinner(initDistrict);
                if (position>0) {
                    State state = ((State) sp_State.getSelectedItem());
                    getset.setState(state.getStateId());
                    setDistrictSpinner(state.getStateId());
                    District_spinnewr_flag = false;
                }
                state_spinner_flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0 && District_spinnewr_flag) {
                    District district = ((District) sp_District.getSelectedItem());
                    getset.setDtsrict(district.getDistrict_Id());
                }
                District_spinnewr_flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getset.setName(etName.getText().toString());
                getset.setDob(etDOB.getText().toString());
                getset.setAddress(etAddress.getText().toString());
                getset.setMobile(etPhone.getText().toString());
                getset.setEmail(etEmail.getText().toString());
                new SubmitPatientDetails().execute(getset);
            }
        });



        etDOB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            MainActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(true);
                    dpd.setAccentColor(Color.parseColor("#3F51B5"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dpd.setAllowEnterTransitionOverlap(true);
                        dpd.setAllowReturnTransitionOverlap(true);
                    }
                    // dpd.dismissOnPause(dismissDate.isChecked());
                    dpd.showYearPickerFirst(true);
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
                return true;
            }
        });
    }


    void initialize()
    {
        etName=(MaterialEditText) findViewById(R.id.etName);
        etEmail=(MaterialEditText)findViewById(R.id.etMail);
        etDOB=(MaterialEditText)findViewById(R.id.etDOB);

        etAddress=(MaterialEditText)findViewById(R.id.etAddres);
        etPhone=(MaterialEditText) findViewById(R.id.etContact);
        etPhone.setMaxCharacters(10);
        etPhone.setErrorColor(Color.RED);
        sp_District=(MaterialSpinner) findViewById(R.id.spDistrict);
        sp_State=(MaterialSpinner) findViewById(R.id.spState);
        db=new DbHandler(MainActivity.this);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,AndroidDatabaseManager.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setDistrictSpinner(String[] initdistrict)
    {
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, initdistrict);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_District.setAdapter(districtAdapter);
    }
    void setStateSpinner()
    {
        stateAdapter = new ArrayAdapter<State>(this, android.R.layout.simple_spinner_item,db.getState());
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_State.setAdapter(stateAdapter);
    }
    void setDistrictSpinner(String scode)
    {
        districtAdapter = new ArrayAdapter<District>(context, android.R.layout.simple_spinner_item, db.getDistrict(scode));
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_District.setAdapter(districtAdapter);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"/"+(++monthOfYear)+"/"+ dayOfMonth;
        etDOB.setText(date);
    }

    @Override
    public void otpReceived(String messageText) {
        String otp[]=messageText.split(" ");
        SharedPreferences prefs = context.getSharedPreferences("OTP", MODE_PRIVATE);
       glbl_otp = prefs.getString("otp","notreceived");

        if(glbl_otp.equals(otp[6])) {
            processdialog.dismiss();
           context.getSharedPreferences("OTP", MODE_PRIVATE).edit().clear().commit();
            OTPSccessful();
            /*ContentValues cv=new ContentValues();
            cv.put(DbConstant.C_User_isverified,"true");
            db.update_Patient_Info(cv);*/
        }
    }

    private class SubmitPatientDetails extends AsyncTask<PatientRegistrationGetSet,Void,String>
    {
    ProgressDialog dialog=new ProgressDialog(context);
    PatientRegistrationGetSet getset;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait!!!");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(PatientRegistrationGetSet... params) {
            getset=params[0];
            return db.SendPatinetRegistartion(params[0]);

        }

        @Override
        protected void onPostExecute(String otp) {
            SharedPreferences.Editor prefs = context.getSharedPreferences(DbConstant.sp_OTP, MODE_PRIVATE).edit();
           prefs.putString(DbConstant.sp_OTP_otp,otp);
            prefs.commit();
            if(dialog.isShowing() && !otp.equals("Error"))
            {

                 prefs = context.getSharedPreferences(DbConstant.T_User_Info, MODE_PRIVATE).edit();
                prefs.putString(DbConstant.C_User_Name,getset.getName());
                prefs.putString(DbConstant.C_User_Phone,getset.getMobile());
                prefs.putString(DbConstant.C_User_Email,getset.getEmail());
                prefs.putString(DbConstant.C_User_District,getset.getDtsrict());
                prefs.putString(DbConstant.C_User_State,getset.getState());
                prefs.commit();

               /* ContentValues cv=new ContentValues();
                cv.put(DbConstant.C_User_Name,getset.getName());
                cv.put(DbConstant.C_User_Phone,getset.getMobile());
                cv.put(DbConstant.C_User_Email,getset.getEmail());
                cv.put(DbConstant.C_User_District,getset.getDtsrict());
                cv.put(DbConstant.C_User_State,getset.getState());
                cv.put(DbConstant.C_User_isverified,"false");
                db.insert_Patient_Info(cv);*/


                dialog.dismiss();
              processdialog = new MaterialDialog.Builder(context)
                        .progress(true,100)
                .progressNumberFormat("%1d/%2d").title("OTP").neutralText("Resend").content("Please wait!!!").canceledOnTouchOutside(false).positiveText("Enter Manually").onPositive(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                              processdialog.dismiss();
                              ManualOTP();
                          }
                      })
                .show();

                new Handler().postDelayed(new Runnable() {
                    public void run() {

                       processdialog.dismiss();
                    }
                }, 60*1000);
            }
            else
            {
                dialog.dismiss();
                new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE).setTitleText("Error!!!").show();
            }
            super.onPostExecute(otp);
        }

    }

    void OTPSccessful()
    {
        SweetAlertDialog di=new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
        di.setTitleText("Registered and Verified");
        di.show();
    }

    void ManualOTP()
    {
        new MaterialDialog.Builder(context)
                .title("")
                .content("")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("OTP","", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Toast.makeText(context,input,Toast.LENGTH_SHORT).show();
                    }
                }).neutralText("Enter OTP").title("OTP").positiveText("Submit").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
               /* if(glbl_otp.equals())*/
            }
        }).show();
    }
}
