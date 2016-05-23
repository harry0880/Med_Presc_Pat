package com.med_presc_pat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.med_presc_pat.Utilities.DbConstant;

public class MainScreen extends AppCompatActivity {
Context context;
    String glbl_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        initialize();
        if(!glbl_otp.equals("notreceived"))
        {
            ManualOTP();
        }



    }

    void initialize()
    {
        SharedPreferences prefs = context.getSharedPreferences(DbConstant.sp_OTP, MODE_PRIVATE);
        glbl_otp = prefs.getString(DbConstant.sp_User_Info,"notreceived");
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
