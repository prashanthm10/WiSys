package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;



public class SensorInfo {
    @SerializedName("gid")
    int gid;
    @SerializedName("nid")
    int nid;
    @SerializedName("epid")
    int epid;
    @SerializedName("value")
    int value;
    @SerializedName("stype")
    String stype;
    @SerializedName("sid")
    int sid;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }



    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getEpid() {
        return epid;
    }

    public void setEpid(int epid) {
        this.epid = epid;
    }

    public int getVal() {
        return value;
    }

    public void setVal(int val) {
        this.value = val;
    }

    public static String getSensorType(int i){
        switch (i){
            case 1:
                return "SENSOR_PWM";
            case 2:
                return "SENSOR_TEMPERATURE";
            case 4:
                return "SENSOR_HUMIDITY";
            case 8:
                return "SENSOR_CO2";
            case 16:
                return "SENSOR_PDS";
            default:
                return "SENSOR_INVALID";
        }
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }
}
/*
JSon:
        [
        {
        "stype":"16",
        "gid" : "1",
        "nid" : "1",
        "epid" : "8",
        "val" : "100"
        }
        ]*/
