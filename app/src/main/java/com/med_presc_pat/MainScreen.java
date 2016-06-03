package com.med_presc_pat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import com.google.firebase.crash.FirebaseCrash;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.med_presc_pat.Utilities.DbConstant;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainScreen extends AppCompatActivity {
Context context;
    String glbl_otp;
    FancyButton btn_newReg,btn_Running,btn_Appointment;
    private static final String TAG = "MainScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        FirebaseCrash.logcat(Log.INFO, TAG, "Crash button clicked");
        initialize();
        if(!glbl_otp.equals("notreceived"))
        {
            ManualOTP();
        }
        btn_newReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,MainActivity.class));
            }
        });

        btn_Running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,Running_no.class));
            }
        });
        btn_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,Appointment.class));
            }
        });

    }

    void initialize()
    {
        SharedPreferences prefs = context.getSharedPreferences(DbConstant.sp_OTP, MODE_PRIVATE);
        glbl_otp = prefs.getString(DbConstant.sp_OTP_otp,"notreceived");
        btn_newReg=(FancyButton) findViewById(R.id.btn_newReg);
        btn_Running=(FancyButton) findViewById(R.id.btn_Runningno);
        btn_Appointment=(FancyButton) findViewById(R.id.btn_Appointment);
    }

    void ManualOTP()
    {
        new MaterialDialog.Builder(context)
                .title("OTP")
                .content("Verify the OTP to continue")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("OTP","", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                }).neutralText("Enter OTP").positiveText("Submit").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                
            }
        }).show();
    }

}
