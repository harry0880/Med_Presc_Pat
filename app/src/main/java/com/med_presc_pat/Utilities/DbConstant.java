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

    public static final String sp_OTP="OTP";
    public static final String sp_OTP_otp="otp";

    public static final String T_User_Info="UserInfo";
    public static final String C_User_Name="Name";
    public static final String C_User_Phone="Phone";
    public static final String C_User_Email="Email";
    public static final String C_User_District="District";
    public static final String C_User_State="State";
    public static final String C_User_isverified="isVerified";

    public  static final String T_Doc_Spl_Type="TBLDocSplMaster";
    public  static final String C_Doc_Spl_ID="DocSplID";
    public  static final String C_Doc_Spl_Detail="DocSplName";

    public  static final String T_Doc_Inst="TBLInsMaster";
    public  static final String C_Doc_Inst_ID="InstID";
    public  static final String C_Doc_Inst_Detail="DocInstName";
    public  static final String C_Doc_Inst_Dcode="InstDcode";
    public  static final String C_Doc_Inst_Scode="DocInstScode";

    public  static final String T_Doc_Details="TBLDoc";
    public  static final String C_Doc_Id="Doc_ID";
    public  static final String C_Doc_Name="DocName";


    public static final String ShiftId="ShiftId";
    public static final String ShiftTime="ShiftTime";
    public static final String ShiftAvailableslots="ShiftAvailableSlots";

/*
    public  static final String ShiftTime=*/


    public static final String CREATE_TABLE_STATE_MASTER = "CREATE TABLE "+ T_State_Master + "(" + C_Scode/*0*/ + " TEXT,"
            + C_Sname/*1*/ +" TEXT);";

    public static final String CREATE_TABLE_District_Master= "CREATE TABLE "+ T_District_Master + "(" + C_Dist_Scode/*0*/ + " TEXT,"
            + C_Dist_Code/*1*/ + " TEXT,"
            + C_Dist_Name/*2*/+ " TEXT);";

    public static final String CREATE_TABLE_SPECIALITY_MASTER = "CREATE TABLE "+ T_Doc_Spl_Type + "(" + C_Doc_Spl_ID/*0*/ + " TEXT,"
            + C_Doc_Spl_Detail/*1*/ +" TEXT);";

    public static final String CREATE_TABLE_Institute_MASTER = "CREATE TABLE "+ T_Doc_Inst + "(" + C_Doc_Inst_ID/*0*/ + " TEXT,"
            + C_Doc_Inst_Detail/*1*/ +" TEXT,"
            + C_Doc_Inst_Dcode +" TEXT,"
            + C_Doc_Inst_Scode +" TEXT);";

    public static final String CREATE_TABLE_DOC_INFO="CREATE TABLE "+ T_Doc_Details + " (" + C_Doc_Id + " TEXT,"
            + C_Doc_Name +" TEXT,"
            +C_Doc_Spl_ID +" TEXT,"
            + C_Doc_Inst_ID+" TEXT);";

    public static final String Create_Table_User_Info= "CREATE TABLE "+T_User_Info + " (" + C_User_Name +" TEXT,"
            + C_User_Phone + " TEXT,"
            + C_User_Email + " TEXT,"
            + C_User_District + " TEXT,"
            + C_User_State+ " TEXT,"
            + C_User_isverified + " TEXT);";


}
