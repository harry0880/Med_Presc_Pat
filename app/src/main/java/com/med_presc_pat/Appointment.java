package com.med_presc_pat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.med_presc_pat.GetSet.RunningNumberGetSet;
import com.med_presc_pat.SpinnerAdapters.Doctor;
import com.med_presc_pat.SpinnerAdapters.InstituteName;
import com.med_presc_pat.SpinnerAdapters.Speciality;
import com.med_presc_pat.Utilities.DbConstant;
import com.med_presc_pat.Utilities.DbHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;



public class Appointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,View.OnClickListener {
  /*  Button btn_slot1,btn_slot2,btn_slot3,btn_slot4;*/
    View viewpreviousselected;
    MaterialSpinner spInstitute,spSpeciality,spDocName;
    MaterialEditText etDOA;
    ArrayAdapter<InstituteName> instituteNameAdapter;
    ArrayAdapter<Speciality> specialityAdapter;
    ArrayAdapter<Doctor> doctorAdapter;
    DbHandler db;
    Context context;
    RunningNumberGetSet getset;
    LinearLayout llSlots;
    Button btn_slots[];
    String DateofArrival,ShiftId;
    static int cnt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        initialize();
        setInstName();
        setSpSpeciality();
    /*    ContentValues cv=new ContentValues();
        setSlots(cv);
*/
       // setSlots();

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etDOA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            Appointment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(true);
                    dpd.setAccentColor(Color.parseColor("#3F51B5"));
                    dpd.setMinDate(now);
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
    db=new DbHandler(context);

    etDOA=(MaterialEditText) findViewById(R.id.etDOA);
    spSpeciality=(MaterialSpinner)findViewById(R.id.spSpeciality);
    spInstitute=(MaterialSpinner)findViewById(R.id.spInstitute);
    llSlots=(LinearLayout) findViewById(R.id.ll_Slots);
    spDocName=(MaterialSpinner)findViewById(R.id.spDoctor);
    getset=new RunningNumberGetSet();
    btn_slots=new Button[4];
}



    void changecolor(View v)
    {
        if(!(viewpreviousselected==null))
        viewpreviousselected.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        viewpreviousselected=v;
        v.setBackgroundColor(getResources().getColor(R.color.colorSelected));
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(++monthOfYear)+"-"+ dayOfMonth;
        etDOA.setText(date);
        DateofArrival=date;
        new getslots().execute();
    }

   public void setSlots(ContentValues values[])
    {
        llSlots.removeAllViewsInLayout();
        int i=0;
        for (ContentValues val:values) {
            btn_slots[i]=new Button(Appointment.this);
            btn_slots[i]=setButtonLayout(btn_slots[i]);
            if(val.get(DbConstant.ShiftAvailableslots).equals("0"))
            {
                btn_slots[i].setEnabled(false);
            }
            btn_slots[i].setText(val.get(DbConstant.ShiftTime).toString());
            btn_slots[i].setId(Integer.parseInt((String) val.get(DbConstant.ShiftId.toString())));
            btn_slots[i].setOnClickListener(this);
            btn_slots[i].setPadding(20,0,20,0);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);

            llSlots.addView(btn_slots[i],params);
            i++;
        }
    }

    Button setButtonLayout(Button button)
    {
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setGravity(Gravity.CENTER_HORIZONTAL);
      /*  button.setBorderColor(getResources().getColor(R.color.white));
        button.setFocusBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setRadius(50);*/
        return button;
    }

    @Override
    public void onClick(View v) {
       changecolor(v);
        ShiftId=("0"+v.getId()).trim();
       final SweetAlertDialog dialog= new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE).setContentText("Are you sure you want to book appointment in the selected slot")
                .setTitleText("Confirm").setCancelText("Cancel").setContentText("Yes");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                new Book_Appointment().execute();
                dialog.dismiss();
            }
        });
    dialog.show();

    }

    private class getslots extends AsyncTask<Void,Void,ContentValues[]>
    {
        ProgressDialog dialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait!!!");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
           // dialog.show();
            super.onPreExecute();
        }

        @Override
        protected ContentValues[] doInBackground(Void... params) {
            return db.CallWebService_GetSlots(getset,DateofArrival);
        }

        @Override
        protected void onPostExecute(ContentValues[] cv) {
            dialog.dismiss();
           if(cv==null) {
               new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Error").show();
           }
            else
               setSlots(cv);
        }
    }


    private class Book_Appointment extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences preferences= context.getSharedPreferences(DbConstant.T_User_Info,MODE_PRIVATE);
            return   db.call_WebService_Appointment(getset,DateofArrival,ShiftId,preferences.getString(DbConstant.C_User_Phone,"null"));

        }

        @Override
        protected void onPostExecute(String s) {
        if(!s.equals("Error"))
         new  SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE).setTitleText("Successful").setContentText(s).show();
            else
        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE).setTitleText("Error").show();
            super.onPostExecute(s);
        }
    }
}
