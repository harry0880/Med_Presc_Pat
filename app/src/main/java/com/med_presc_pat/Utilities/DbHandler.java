package com.med_presc_pat.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.med_presc_pat.SpinnerAdapters.District;
import com.med_presc_pat.SpinnerAdapters.State;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Administrator on 05/05/2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    JSONObject jsonResponse ;

    final String NameSpace="http://tempuri.org/";
    String URL="http://192.168.0.100/Service.asmx";

    String LoadMasterMathod = "Patientmaster";
    String SoapLinkMaster="http://tempuri.org/Patientmaster";



    public DbHandler(Context context) {
        super(context, DbConstant.DBNAME, null, DbConstant.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstant.CREATE_TABLE_District_Master);
        db.execSQL(DbConstant.CREATE_TABLE_STATE_MASTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean Load_Master_tables()
    {
        String res= null;
        SoapObject request=new SoapObject(NameSpace, LoadMasterMathod);
        SoapSerializationEnvelope envolpe=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envolpe.dotNet=true;
        envolpe.setOutputSoapObject(request);
        HttpTransportSE androidHTTP= new HttpTransportSE(URL);

        try {
            androidHTTP.call(SoapLinkMaster, envolpe);
            SoapPrimitive response = (SoapPrimitive)envolpe.getResponse();
            res=response.toString();
            //System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String[] Response= res.split("#"),JsonNames={"State","District"};
        int lengthJsonArr ;
        try {
            for (int i = 0; i < 2; i++) {
                Response[i]="{ \""+JsonNames[i]+"\" :"+Response[i]+" }";
                jsonResponse = new JSONObject(Response[i]);
                JSONArray jsonMainNode = jsonResponse.optJSONArray(JsonNames[i]);
                lengthJsonArr = jsonMainNode.length();
                for(int j=0; j < lengthJsonArr; j++)
                {
                    ContentValues values = new ContentValues();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(j);
                    if(i==0)
                    {

                        values.put(DbConstant.C_Scode,jsonChildNode.optString("scode").toString());
                        values.put(DbConstant.C_Sname,jsonChildNode.optString("sname").toString());
                        SQLiteDatabase writeableDB = getWritableDatabase();
                        writeableDB.insert(DbConstant.T_State_Master, null, values);
                        writeableDB.close();
                    }
                    if(i==1)
                    {
                        values.put(DbConstant.C_Dist_Scode,jsonChildNode.optString("scode_ds").toString());
                        values.put(DbConstant.C_Dist_Code,jsonChildNode.optString("dcode_ds").toString());
                        values.put(DbConstant.C_Dist_Name,jsonChildNode.optString("ds_detail").toString());
                        SQLiteDatabase writeableDB = getWritableDatabase();
                        writeableDB.insert(DbConstant.T_District_Master, null, values);
                        writeableDB.close();
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return  true;
    }

        public ArrayList<State> getState()
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+DbConstant.T_State_Master+";",null);
        cr.moveToFirst();
        ArrayList<State> statelist=new ArrayList<>();
        do {
        statelist.add(new State(cr.getString(1),cr.getString(0)));
        }while (cr.moveToNext());
    return statelist;
    }

    public ArrayList<District> getDistrict(String Statecode)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+DbConstant.T_District_Master+" where "+DbConstant.C_Dist_Scode+"='"+Statecode+"'",null);
        cr.moveToFirst();
        ArrayList<District> districtlist=new ArrayList<District>();
        do {
            districtlist.add(new District(cr.getString(1),cr.getString(2)));
        }while (cr.moveToNext());
        return districtlist;
    }


//Viewing DB
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
