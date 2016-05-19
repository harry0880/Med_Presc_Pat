package com.med_presc_pat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.med_presc_pat.GetSet.PatientRegistrationGetSet;
import com.med_presc_pat.SpinnerAdapters.State;
import com.med_presc_pat.SpinnerAdapters.District;
import com.med_presc_pat.Utilities.DbHandler;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {
    DbHandler db;
    EditText etName,etPhone,etEmail,etDOB,etAddress;
    MaterialSpinner sp_District,sp_State,spDistrict;
    Context context;
    ActionProcessButton btnSignIn;
    ArrayAdapter<District> districtAdapter;
    ArrayAdapter<State> stateAdapter;
    String[] initDistrict = {"District"};
    Boolean state_spinner_flag=false,District_spinnewr_flag=false;
    PatientRegistrationGetSet getset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
        context=this;
        setStateSpinner();
        setDistrictSpinner(initDistrict);
        getset=new PatientRegistrationGetSet();
        sp_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(state_spinner_flag) {
                    State state=((State) sp_State.getSelectedItem());
                    getset.setState(state.getStateId());
                    setDistrictSpinner(state.getStateId());
                    District_spinnewr_flag=false;
                }
                state_spinner_flag=true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(District_spinnewr_flag)
                {
                    District district=((District) spDistrict.getSelectedItem());
                    getset.setDtsrict(district.getDistrict_Id());
                }
                District_spinnewr_flag=true;
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
                new SubmitPatientDetails().execute(getset);
            }
        });


    }

    void initialize()
    {
        etName=(EditText)findViewById(R.id.etName);
        etEmail=(EditText)findViewById(R.id.etMail);
        etDOB=(EditText)findViewById(R.id.etDOB);
        etAddress=(EditText)findViewById(R.id.etAddres);
        etPhone=(EditText) findViewById(R.id.etContact);
        sp_District=(MaterialSpinner) findViewById(R.id.spDistrict);
        sp_State=(MaterialSpinner) findViewById(R.id.spState);
        db=new DbHandler(context);
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
        spDistrict.setAdapter(districtAdapter);
    }

    private class SubmitPatientDetails extends AsyncTask<PatientRegistrationGetSet,Void,Boolean>
    {
    ProgressDialog dialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait!!!");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(PatientRegistrationGetSet... params) {
            return db.SendPatinetRegistartion(params[0]);
            
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(dialog.isShowing() && aBoolean)
            {
                dialog.dismiss();
                new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE).setTitleText("Submitted").setContentText("Your have been registered. Shortly you will receive an OTP ").setConfirmClickListener(null).show();
            }
            else
            {
                dialog.dismiss();
                new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE).setTitleText("Error!!!").show();

            }
            super.onPostExecute(aBoolean);
        }
    }
}
