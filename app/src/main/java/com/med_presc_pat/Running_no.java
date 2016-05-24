package com.med_presc_pat;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.med_presc_pat.SpinnerAdapters.InstituteName;
import com.med_presc_pat.SpinnerAdapters.Speciality;
import com.med_presc_pat.Utilities.DbHandler;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Running_no extends AppCompatActivity {

    MaterialSpinner spInstitute,spSpeciality,spDocName;
    ArrayAdapter<InstituteName> instituteNameAdapter;
    ArrayAdapter<Speciality> specialityAdapter;
    DbHandler db;
    Context context;
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
                if(position>0)
                {
                    InstituteName instituteName=(InstituteName) spInstitute.getSelectedItem();
                    setSpSpeciality(instituteName.getInstituteId());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    Speciality spec=(Speciality) spSpeciality.getSelectedItem();

                }
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

    }
    void initialize()
    {
        db=new DbHandler(context);
        spSpeciality=(MaterialSpinner)findViewById(R.id.spSpeciality);
        spInstitute=(MaterialSpinner)findViewById(R.id.spInstitute);
        setInstName();

    }

    void setInstName()
    {
        instituteNameAdapter=new ArrayAdapter<InstituteName>(this,android.R.layout.simple_spinner_item,db.getInstName());
        instituteNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInstitute.setAdapter(instituteNameAdapter);
    }

    void setSpSpeciality(String Speciality)
    {
        specialityAdapter=new ArrayAdapter<Speciality>(this,android.R.layout.simple_spinner_item,db.getSpecName(Speciality));
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpeciality.setAdapter(specialityAdapter);
    }

}
