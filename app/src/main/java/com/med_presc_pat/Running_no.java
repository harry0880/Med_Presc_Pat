package com.med_presc_pat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.med_presc_pat.GetSet.RunningNumberGetSet;
import com.med_presc_pat.SpinnerAdapters.Doctor;
import com.med_presc_pat.SpinnerAdapters.InstituteName;
import com.med_presc_pat.SpinnerAdapters.Speciality;
import com.med_presc_pat.Utilities.DbConstant;
import com.med_presc_pat.Utilities.DbHandler;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;

public class Running_no extends AppCompatActivity {

    MaterialSpinner spInstitute,spSpeciality,spDocName;
    ArrayAdapter<InstituteName> instituteNameAdapter;
    ArrayAdapter<Speciality> specialityAdapter;
    ArrayAdapter<Doctor> doctorAdapter;

    DbHandler db;
    Context context;
    FancyButton btn_bookAppointment;
    RunningNumberGetSet getset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_no);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        initialize();

        spInstitute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1)
                {
                    InstituteName instituteName=(InstituteName) spInstitute.getSelectedItem();
                    getset.setInstId(instituteName.getInstituteId());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>-1)
                {
                    Speciality spec=(Speciality) spSpeciality.getSelectedItem();
                    getset.setSpecialityId(spec.getSpecialityId());
                    setSpDocName();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDocName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Doctor doctor=(Doctor) spDocName.getSelectedItem();
                getset.setDocId(doctor.getDocId());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new callwebService().execute();
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

    }
    void initialize()
    {
        getset=new RunningNumberGetSet();
        db=new DbHandler(context);
        spSpeciality=(MaterialSpinner)findViewById(R.id.spSpeciality);
        spInstitute=(MaterialSpinner)findViewById(R.id.spInstitute);
        spDocName=(MaterialSpinner)findViewById(R.id.spDoctor);

        setInstName();
        setSpSpeciality();
        btn_bookAppointment=(FancyButton) findViewById(R.id.btn_Submit);

    }

    void setInstName()
    {
        instituteNameAdapter=new ArrayAdapter<InstituteName>(this,android.R.layout.simple_spinner_item,db.getInstName());
        instituteNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInstitute.setAdapter(instituteNameAdapter);
    }

    void setSpSpeciality()
    {
        specialityAdapter=new ArrayAdapter<Speciality>(this,android.R.layout.simple_spinner_item,db.getSpecName());
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpeciality.setAdapter(specialityAdapter);
    }

    void setSpDocName()
    {
        doctorAdapter=new ArrayAdapter<Doctor>(this,android.R.layout.simple_spinner_item,db.getDocList(getset.getSpecialityId(),getset.getInstId()));
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDocName.setAdapter(doctorAdapter);
    }

    void disableButton()
    {
        btn_bookAppointment.setEnabled(false);
    }

    void enableButton()
    {
        btn_bookAppointment.setEnabled(true);
    }

    private class callwebService extends AsyncTask<Void,Void,String>
    {
        SharedPreferences preferences= context.getSharedPreferences(DbConstant.T_User_Info,MODE_PRIVATE);
        MaterialDialog progressDialog=new MaterialDialog.Builder(context).progress(true,100).show();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return  db.CallWebService_Running_no(getset,preferences.getString(DbConstant.C_User_Phone,"null"));

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE).setTitleText("Done").setContentText(s).show();
            super.onPostExecute(s);
        }
    }

}
