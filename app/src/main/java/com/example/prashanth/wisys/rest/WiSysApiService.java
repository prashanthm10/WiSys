package com.example.prashanth.wisys.rest;

import com.example.prashanth.wisys.CommandResponse;
import com.example.prashanth.wisys.CommandStatusResponse;
import com.example.prashanth.wisys.Gateway;
import com.example.prashanth.wisys.Node;
import com.example.prashanth.wisys.SensorInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
Created by Prashanth on 7/23/2017.
 */

public interface WiSysApiService {
    @GET("gwlist")
    Call<List<Gateway>> getGatewayList();

    @GET("nodelist/{gid}")
    Call<List<Node>> getNodeList(@Path("gid") int gid);

    @GET("nodelist/{gid}/{nid}")
    Call<List<Node>> getNode(@Path("gid") int gid,@Path("nid") int nid);

    @GET("sensorlist/{gid}")
    Call<List<SensorInfo>> getSensorInfoList(@Path("gid") int gid);

    @POST("newcommand/{gid}/{sid}/{cmd}/{val}")
    Call<CommandResponse> setValue(@Path("gid") int gid,@Path("sid") int sid,@Path("cmd") String cmd,@Path("val") int val);

    @GET("commandstatus/{gwid}/{cmd_id}")
    Call<CommandStatusResponse> getTheStatus(@Path("gwid") int gwid, @Path("cmd_id") int cmd_id);

}
