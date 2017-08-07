package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prashanth on 7/10/2017.
 */

class SensorStatus {
    @SerializedName("gid")
    int gid;
    @SerializedName("nid")
    int nid;
    @SerializedName("epid")
    int epid;
    @SerializedName("sid")
    int sid;
    @SerializedName("data")
    int data;
    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("stype")
    String stype;

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

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        "id" : "1",
        "gid" : "1",
        "nid" : "1",
        "epid" : "12",
        "stype" : "1",
        "sdata" : "100",
        "timestamp" : "2014-07-18 08:59:22"
        }
        ]*/
