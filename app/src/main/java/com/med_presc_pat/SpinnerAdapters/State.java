package com.med_presc_pat.SpinnerAdapters;

/**
 * Created by Administrator on 17/05/2016.
 */
public class State {

    String State_Name,State_Id;
    public State(String State_Name,String State_Id)
    {
        this.State_Name=State_Name;
        this.State_Id=State_Id;
    }
    public String getStateName()
    {
        return State_Name;
    }


    public  String getStateId()
    {
        return State_Id;
    }

    public  void setStateId(String State_Id)
    {
        this.State_Id=State_Id;
    }
    public  void setStateName(String State_Name)
    {
        this.State_Name=State_Name;
    }

    public String toString() {
        return State_Name;
    }
}
