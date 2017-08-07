package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;



public class Node {
    @SerializedName("nid")
    private
    int nid;
    @SerializedName("gid")
    private
    int gid;
    @SerializedName("status")
    private
    int status;
    @SerializedName("sensors")
    private
    int sensors;
    @SerializedName("interval")
    private
    int interval;
    @SerializedName("capab")
    private
    int capab;
    @SerializedName("nw_addr")
    private
    String nw_addr;
    @SerializedName("ieee_addr")
    private
    String ieee_addr;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSensors() {
        return sensors;
    }

    public void setSensors(int sensors) {
        this.sensors = sensors;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getNw_addr() {
        return nw_addr;
    }

    public void setNw_addr(String nw_addr) {
        this.nw_addr = nw_addr;
    }

    public String getIeee_addr() {
        return ieee_addr;
    }

    public void setIeee_addr(String ieee_addr) {
        this.ieee_addr = ieee_addr;
    }

    public int getCapab() {
        return capab;
    }

    public void setCapab(int capab) {
        this.capab = capab;
    }
}
/*
JSon:
        [
        {
        "nid" : "1",
        "gid" : "1",
        "src_addr" : "45451",
        "nw_addr" : "45451",
        "ieee_addr" : "50-aa-45-1-0-4b-12-0",
        "capab" : "142",
        "status" : "1",
        "sensors" : "16",
        "lqi" : "78",
        "pktloss" : "0",
        "interval" : "60"
        }
        ]*/
