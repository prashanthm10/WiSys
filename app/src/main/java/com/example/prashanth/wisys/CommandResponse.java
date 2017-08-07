package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ${SHASHIKANt} on 02-08-2017.
 */

public class CommandResponse {
    @SerializedName("gid")
    int gid;
    @SerializedName("sid")
    int sid;
    @SerializedName("cmd_id")
    int cmd_id;
    @SerializedName("status")
    String status;
    @SerializedName("cmd")
    String cmd;

    @Override
    public String toString() {
        return "SID "+sid+"\nCMD_ID"+cmd_id+"\nSTATUS"+status;
    }
}
