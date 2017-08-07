package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

/**
 * Created by Prashanth on 7/5/2017.
 */

public class Gateway {
    @SerializedName("id")
    int id;
    @SerializedName("z_alive")
    int z_alive;
    @SerializedName("g_alive")
    int g_alive;
    @SerializedName("node_count")
    int node_cnt;
    @SerializedName("gid")
    String gid;
    @SerializedName("vid")
    String vid;

    Gateway(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZ_alive() {
        return z_alive;
    }

    public void setZ_alive(int z_alive) {
        this.z_alive = z_alive;
    }

    public int getG_alive() {
        return g_alive;
    }

    public void setG_alive(int g_alive) {
        this.g_alive = g_alive;
    }

    public int getNode_cnt() {
        return node_cnt;
    }

    public void setNode_cnt(int node_cnt) {
        this.node_cnt = node_cnt;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
/*"id" : "1",
        "gid" : "b8-27-eb-e9-14-4e",
        "vid" : "3-0-0-a9",
        "node_cnt" : "1",
        "z_alive" : "0",
        "g_alive" : "1"*/