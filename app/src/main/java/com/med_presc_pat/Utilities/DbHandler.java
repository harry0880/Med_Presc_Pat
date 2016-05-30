package com.med_presc_pat.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.med_presc_pat.Appointment;
import com.med_presc_pat.GetSet.PatientRegistrationGetSet;
import com.med_presc_pat.GetSet.RunningNumberGetSet;
import com.med_presc_pat.SpinnerAdapters.District;
import com.med_presc_pat.SpinnerAdapters.Doctor;
import com.med_presc_pat.SpinnerAdapters.InstituteName;
import com.med_presc_pat.SpinnerAdapters.Speciality;
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
    Appointment app=new Appointment();
    Context context;

    final String NameSpace="http://tempuri.org/";
    //String URL="http://192.168.0.100/Service.asmx";
    String URL="http://10.88.229.42:85/Service.asmx";

    String LoadMasterMathod = "Patientmaster";
    String SoapLinkMaster="http://tempuri.org/Patientmaster";

    String SendDoctorRegistration = "PatientRegistration";
    String SoapLinkSendDoctorRegistration="http://tempuri.org/PatientRegistration";

    String SendRunningNo="GenerateCurrentAppoinmentNumber";
    String SoapSend_RunningNo="http://tempuri.org/GenerateCurrentAppoinmentNumber";

    String GetSlots="GetSlots";
    String Soap_GetSlots="http://tempuri.org/GetSlots";

    String GenerateAppointmentNumber="GenerateAppoinmentNumber";
    String Soap_GenerateAppointmentNumber="http://tempuri.org/GenerateAppoinmentNumber";

    public DbHandler(Context context) {
        super(context, DbConstant.DBNAME, null, DbConstant.DBVERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstant.CREATE_TABLE_District_Master);
        db.execSQL(DbConstant.CREATE_TABLE_STATE_MASTER);
        db.execSQL(DbConstant.CREATE_TABLE_SPECIALITY_MASTER);
        db.execSQL(DbConstant.CREATE_TABLE_Institute_MASTER);
        db.execSQL(DbConstant.CREATE_TABLE_DOC_INFO);
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

        String[] Response= res.split("#"),JsonNames={"State","District","InstituteName","Doctor","Speciality"};
        int lengthJsonArr ;
        try {
            for (int i = 0; i < 5; i++) {
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
                    if(i==2)
                    {
                        values.put(DbConstant.C_Doc_Inst_ID,jsonChildNode.optString("Instid").toString());
                        values.put(DbConstant.C_Doc_Inst_Detail,jsonChildNode.optString("Instname").toString());
                        values.put(DbConstant.C_Doc_Inst_Dcode,jsonChildNode.optString("HealthInstituteDCode").toString());
                        values.put(DbConstant.C_Doc_Inst_Scode,jsonChildNode.optString("HealthInstituteSCode").toString());
                        SQLiteDatabase writeableDB = getWritableDatabase();
                        writeableDB.insert(DbConstant.T_Doc_Inst, null, values);
                        writeableDB.close();
                    }
                    if(i==3)
                    {
                        values.put(DbConstant.C_Doc_Id,jsonChildNode.optString("Doctorid").toString());
                        values.put(DbConstant.C_Doc_Name,jsonChildNode.optString("Doctorname").toString());
                        values.put(DbConstant.C_Doc_Spl_ID,jsonChildNode.optString("specialityid").toString());
                        values.put(DbConstant.C_Doc_Inst_ID,jsonChildNode.optString("instituteid").toString());
                        SQLiteDatabase writeableDB = getWritableDatabase();
                        writeableDB.insert(DbConstant.T_Doc_Details, null, values);
                        writeableDB.close();
                    }
                    if(i==4)
                    {
                        values.put(DbConstant.C_Doc_Spl_ID,jsonChildNode.optString("splid").toString());
                        values.put(DbConstant.C_Doc_Spl_Detail,jsonChildNode.optString("spldesc").toString());
                        SQLiteDatabase writeableDB = getWritableDatabase();
                        writeableDB.insert(DbConstant.T_Doc_Spl_Type, null, values);
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

    public String SendPatinetRegistartion(PatientRegistrationGetSet obj)
    {  String res= null;
        SoapObject request=new SoapObject(NameSpace, SendDoctorRegistration);
        PropertyInfo pi = new PropertyInfo();

        pi.setName("name");
        pi.setValue(obj.getName());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Dob");
        pi.setValue(obj.getDob());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("email");
        pi.setValue(obj.getEmail());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("mobile");
        pi.setValue(obj.getMobile());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("address");
        pi.setValue(obj.getAddress());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("stateid");
        pi.setValue(obj.getState());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("districtid");
        pi.setValue(obj.getDtsrict());
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envolpe=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envolpe.dotNet=true;
        envolpe.setOutputSoapObject(request);
        HttpTransportSE androidHTTP= new HttpTransportSE(URL);

        try {
            androidHTTP.call(SoapLinkSendDoctorRegistration, envolpe);
            SoapPrimitive response = (SoapPrimitive)envolpe.getResponse();
            res=response.toString();
            //System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

        return res;
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

    public ArrayList<InstituteName> getInstName()
    {
        SharedPreferences preferences =context.getSharedPreferences(DbConstant.T_User_Info,Context.MODE_PRIVATE);
        String state=preferences.getString(DbConstant.C_User_State,"null");
        String district =preferences.getString(DbConstant.C_User_District,"null");

        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+DbConstant.T_Doc_Inst+" where "+ DbConstant.C_Doc_Inst_Scode +"='"+state+"' and "+DbConstant.C_Doc_Inst_Dcode+"='"+district+"';",null);
        int cnt=cr.getCount();
        cr.moveToFirst();
        ArrayList<InstituteName> instituteNames=new ArrayList<InstituteName>();
        do {
            instituteNames.add(new InstituteName(cr.getString(0),cr.getString(1)));
        }while (cr.moveToNext());
        return instituteNames;
    }

    public ArrayList<Speciality> getSpecName()
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+DbConstant.T_Doc_Spl_Type+";",null);
        cr.moveToFirst();
        ArrayList<Speciality> specialities=new ArrayList<Speciality>();
        do {
            specialities.add(new Speciality(cr.getString(0),cr.getString(1)));
        }while (cr.moveToNext());
        return specialities;
    }

    public ArrayList<Doctor> getDocList(String specid,String instid)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("select * from "+DbConstant.T_Doc_Details+" where "+DbConstant.C_Doc_Spl_ID+"="+specid+" and "+DbConstant.C_Doc_Inst_ID+"="+instid+";",null);
        cr.moveToFirst();
        ArrayList<Doctor> doctors=new ArrayList<Doctor>();
        if(cr.getCount()<=0)
        {
            doctors.add(new Doctor("0","No Doctors Available"));
            return doctors;

        }
        do {
            doctors.add(new Doctor(cr.getString(0),cr.getString(1)));
        }while (cr.moveToNext());
        return doctors;
    }




    public void insert_Patient_Info(ContentValues cv)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.insert(DbConstant.T_User_Info,null,cv);
    }

    public void update_Patient_Info(ContentValues cv)
    {
        SQLiteDatabase db=getReadableDatabase();
        db.update(DbConstant.T_User_Info,cv,null,null);
    }

    public String CallWebService_Running_no(RunningNumberGetSet obj,String phone)
    {
        String res= null;
        SoapObject request=new SoapObject(NameSpace, SendRunningNo);
        PropertyInfo pi = new PropertyInfo();

        pi.setName("Mobile");
        pi.setValue(phone);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Instituteid");
        pi.setValue(obj.getInstId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("specialityid");
        pi.setValue(obj.getSpecialityId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DoctorId");
        pi.setValue(/*obj.getDocId()*/"11002");
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Createdby");
        pi.setValue(phone);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envolpe=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envolpe.dotNet=true;
        envolpe.setOutputSoapObject(request);
        HttpTransportSE androidHTTP= new HttpTransportSE(URL);

        try {
            androidHTTP.call(SoapSend_RunningNo, envolpe);
            SoapPrimitive response = (SoapPrimitive)envolpe.getResponse();
            res=response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return res;
    }


    public ContentValues[] CallWebService_GetSlots(RunningNumberGetSet getset,String Date)
    {

        String res=null ;
        SoapObject request=new SoapObject(NameSpace, GetSlots);
        PropertyInfo pi = new PropertyInfo();

        pi.setName("InstituteId");
        pi.setValue(getset.getInstId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("specialityId");
        pi.setValue(getset.getSpecialityId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DoctorId");
        pi.setValue(/*getset.getDocId()*/"11002");
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("SlotDate");
        pi.setValue(Date);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envolpe=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envolpe.dotNet=true;
        envolpe.setOutputSoapObject(request);
        HttpTransportSE androidHTTP= new HttpTransportSE(URL);

        try {
            androidHTTP.call(Soap_GetSlots, envolpe);
            SoapPrimitive response = (SoapPrimitive)envolpe.getResponse();
            res=response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int lengthJsonArr;
        ContentValues[] values;
        try {
            jsonResponse = new JSONObject(res);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("Slots");
            lengthJsonArr = jsonMainNode.length();
            values=new ContentValues[lengthJsonArr];
            for(int j=0; j < lengthJsonArr; j++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(j);
               values[j]=new ContentValues();
                values[j].put(DbConstant.ShiftId, jsonChildNode.optString("Shiftid").toString());
                values[j].put(DbConstant.ShiftTime, jsonChildNode.optString("Shifttime").toString());
                values[j].put(DbConstant.ShiftAvailableslots, jsonChildNode.optString("Shiftavailableslots").toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return values;
    }


    public String call_WebService_Appointment(RunningNumberGetSet getset,String Date,String Id,String Createdby)
    {

        String res=null ;
        SoapObject request=new SoapObject(NameSpace, GenerateAppointmentNumber);
        PropertyInfo pi = new PropertyInfo();

        pi.setName("Mobile");
        pi.setValue(Createdby);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("Instituteid");
        pi.setValue(getset.getInstId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("specialityid");
        pi.setValue(getset.getSpecialityId());
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DoctorId");
        pi.setValue(/*getset.getDocId()*/"11002");
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Createdby");
        pi.setValue(Createdby);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Shiftid");
        pi.setValue(Id);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("AppoinmentDate");
        pi.setValue(Date);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envolpe=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envolpe.dotNet=true;
        envolpe.setOutputSoapObject(request);
        HttpTransportSE androidHTTP= new HttpTransportSE(URL);

        try {
            androidHTTP.call(Soap_GenerateAppointmentNumber, envolpe);
            SoapPrimitive response = (SoapPrimitive)envolpe.getResponse();
            res=response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return res;
    }
}
