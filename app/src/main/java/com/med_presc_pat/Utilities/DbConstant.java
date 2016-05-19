package com.med_presc_pat.Utilities;

/**
 * Created by Administrator on 05/05/2016.
 */
public class DbConstant {

    public static final String DBNAME = "Master";
    public static final int DBVERSION = 1;

    public static final String T_State_Master="TBLStateMaster";
    public  static final String C_Scode="Scode";
    public  static final String C_Sname="Sname";

    public static final String T_District_Master="TBLDistrictMaster";
    public  static final String C_Dist_Scode="DScode";
    public  static final String C_Dist_Code="Dcode";
    public  static final String C_Dist_Name="Dname";


    public static final String CREATE_TABLE_STATE_MASTER = "CREATE TABLE "+ T_State_Master + "(" + C_Scode/*0*/ + " TEXT,"
            + C_Sname/*1*/ +" TEXT);";

    public static final String CREATE_TABLE_District_Master= "CREATE TABLE "+ T_District_Master + "(" + C_Dist_Scode/*0*/ + " TEXT,"
            + C_Dist_Code/*1*/ + " TEXT,"
            + C_Dist_Name/*2*/+ " TEXT);";



}
