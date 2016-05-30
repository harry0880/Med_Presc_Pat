package com.med_presc_pat.Utilities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.med_presc_pat.MainActivity;
import com.med_presc_pat.MainScreen;
import com.med_presc_pat.R;
import com.med_presc_pat.Running_no;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;

public class splash extends AppCompatActivity {

    DbHandler dbh;
    Context context;
    String instanceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_splash);
        dbh=new DbHandler(splash.this);
      //  instanceId= FirebaseInstanceId.getInstance().getToken();
        if(!doesDatabaseExist(getApplicationContext(),DbConstant.DBNAME))
        new LoadMaster().execute();
        else
            startActivity(new Intent(splash.this,MainScreen.class));
    }

    private class LoadMaster extends AsyncTask<Void,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            return dbh.Load_Master_tables();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if(bool)
            {
                startActivity(new Intent(splash.this,MainScreen.class));
            }
            else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Opps").setContentText("Please check Internet Connection and try again!!!").show();
            }
        }
    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
