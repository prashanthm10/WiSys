package com.example.prashanth.wisys;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.prashanth.wisys.rest.WiSysApiService;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Build.VERSION_CODES.M;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="LOGIN_ACTIVITY" ;
    ProgressDialog progressDialog;
    Retrofit retrofit;
    SqliteHelper sqliteHelper;
    final String BASE_URL="http://139.59.4.45:8500/";
    final static String BROADCAST_LOGIN_ACTIVITY="com.example.prashanth.wisys.loginactivity";
    MyBroadcast myBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn=(Button) findViewById(R.id.btn_login);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.wisys_icon);
        }
        loginBtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Fetching Gateway List...");
        sqliteHelper=new SqliteHelper(this,"Wisys",null,1);

        IntentFilter intentFilter=new IntentFilter(BROADCAST_LOGIN_ACTIVITY);
        myBroadcast=new MyBroadcast();
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcast,intentFilter);
    }

    @Override
    public void onClick(View v) {
        progressDialog.show();
        //login();
        startService(new Intent(LoginActivity.this,InitializeLocalDb.class));

    }

    private void login() {
        NetworkHelper networkHelper = new NetworkHelper();
        String username="app_test";
        String password="123";
        String json = "{\"user\": \"" + username + "\", \"passwd\":\"" + password+ "\"}";

        networkHelper.post("http://139.59.4.45:8500", json, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
               Log.d(TAG,"FAILED!!!");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d(TAG,"Response code "+response.code());
                Log.d(TAG,"Response body "+response.body());
                Log.d(TAG,"Response message "+response.message());
                Log.d(TAG,"Response isSuccess?  "+response.isSuccessful());
            }
        });


    }

    void getGatewayList(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        WiSysApiService wiSysApiService=retrofit.create(WiSysApiService.class);
        Call<List<Gateway>> call=wiSysApiService.getGatewayList();
        call.enqueue(new Callback<List<Gateway>>() {
            @Override
            public void onResponse(Call<List<Gateway>> call, Response<List<Gateway>> response) {
                sqliteHelper.insertGateways(response.body());

            }

            @Override
            public void onFailure(Call<List<Gateway>> call, Throwable t) {
                Log.d("API_RESPONSE"," Failed");
                Toast.makeText(LoginActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcast);
    }

    class MyBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog.hide();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            SharedPreferences.Editor preferences=getSharedPreferences("wisys",MODE_PRIVATE).edit();
            preferences.putBoolean("login",true);
            preferences.apply();
            finish();
        }
    }
}
