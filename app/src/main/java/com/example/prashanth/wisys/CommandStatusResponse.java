package com.example.prashanth.wisys;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ${SHASHIKANt} on 02-08-2017.
 */

public class CommandStatusResponse {
    @SerializedName("gid")
    int gid;
    @SerializedName("cmd")
    String cmd;
    @SerializedName("cmd_id")
    int cmd_id;
    @SerializedName("status")
    String status;

    public int getGid() {
        return gid;
    }

    public String getCmd() {
        return cmd;
    }

    public int getCmd_id() {
        return cmd_id;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CMD_ID "+cmd_id+"\nSTATUS "+status;
    }
}
