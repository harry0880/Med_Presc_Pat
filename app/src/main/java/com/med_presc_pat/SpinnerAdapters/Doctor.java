package com.med_presc_pat.SpinnerAdapters;

/**
 * Created by Administrator on 24/05/2016.
 */
public class Doctor {
    public Doctor(String docId, String docName) {
        DocId = docId;
        DocName = docName;
    }

    String DocId;

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    String DocName;

    public String toString()
    {
        return DocName;
    }
}
