package com.example.prashanth.wisys;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.prashanth.wisys.rest.WiSysApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${SHASHIKANt} on 28-07-2017.
 */

public class InitializeLocalDb extends IntentService {


    Retrofit retrofit;
    SqliteHelper sqliteHelper;
    final String BASE_URL="http://139.59.4.45:8500/api/";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * param name Used to name the worker thread, important only for debugging.
     */

    public InitializeLocalDb() {
        super("initialize.local.db");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sqliteHelper=new SqliteHelper(this,"Wisys",null,1);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WiSysApiService wiSysApiService=retrofit.create(WiSysApiService.class);
        Call<List<Gateway>> call=wiSysApiService.getGatewayList();
        call.enqueue(new Callback<List<Gateway>>() {
            @Override
            public void onResponse(Call<List<Gateway>> call, Response<List<Gateway>> response) {
                sqliteHelper.insertGateways(response.body());
                for(Gateway gateway:response.body()){
                    getSensorInfoList(gateway.getId());
                }
                //GetBack to UI
                Intent intent1=new Intent(LoginActivity.BROADCAST_LOGIN_ACTIVITY);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);

            }

            @Override
            public void onFailure(Call<List<Gateway>> call, Throwable t) {
                Log.d("API_RESPONSE"," Failed");
            }
        });
    }

    void getSensorInfoList(final int gid){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        WiSysApiService wiSysApiService=retrofit.create(WiSysApiService.class);
        Call<List<SensorInfo>> call=wiSysApiService.getSensorInfoList(gid);
        call.enqueue(new Callback<List<SensorInfo>>() {
            @Override
            public void onResponse(Call<List<SensorInfo>> call, Response<List<SensorInfo>> response) {
                sqliteHelper.insertSensorInfo(response.body());
            }

            @Override
            public void onFailure(Call<List<SensorInfo>> call, Throwable t) {
            }
        });
    }
}
