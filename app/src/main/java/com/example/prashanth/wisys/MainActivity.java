package com.example.prashanth.wisys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prashanth.wisys.rest.WiSysApiService;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,AdapterView.OnItemClickListener {

    ExpandableLayout expandtitle,expandgatewaylist;
    TextView selectedGatewayTv;
    ListView sensorLv;
    ProgressBar gatewayListPb;
    ImageView refreshIv;
    Retrofit retrofit;
    ListView gatewayLv;
    Gateway gateway;
    SqliteHelper sqliteHelper;
    final String BASE_URL="http://139.59.4.45:8500/api/";
    final static  String LAST_SELECTED_GID="last_selected_gid";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("wisys",MODE_PRIVATE);
        Boolean login=preferences.getBoolean("login",false);
        if(!login){
            Log.d("LOGIN","login"+login);
            startActivity(new Intent(this,LoginActivity.class));
            finishAffinity();
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialze views
        selectedGatewayTv =(TextView)findViewById(R.id.tv_selected_gateway);
        expandtitle= (ExpandableLayout) findViewById(R.id.expand_gateway_title);
        expandgatewaylist=(ExpandableLayout)findViewById(R.id.expand_gateway_list);
        gatewayListPb =(ProgressBar)findViewById(R.id.pb_gateway_list);
        sensorLv =(ListView)findViewById(R.id.lv_sensor);
        refreshIv=(ImageView)findViewById(R.id.iv_refresh);
        gatewayLv =(ListView)findViewById(R.id.lv_gateway);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        refreshIv.setOnClickListener(this);
        selectedGatewayTv.setOnClickListener(this);
        findViewById(R.id.tv_select_gateway).setOnClickListener(this);
        sqliteHelper=new SqliteHelper(this,"Wisys",null,1);
        gateway=sqliteHelper.getGateway(preferences.getInt(LAST_SELECTED_GID,0));
        gatewayLv.setAdapter(new GatewayAdapter(this,0, sqliteHelper.getGateways()));
        sensorLv.setAdapter(new SensorAdapter(this,0,sqliteHelper.getSensorInfoList(gateway.getId())));

        sensorLv.setOnItemClickListener(this);
        gatewayLv.setOnItemClickListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        restoreTheLastSetup();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void restoreTheLastSetup(){
        if(gateway!=null){
            sensorLv.setAdapter(new SensorAdapter(this,0,sqliteHelper.getSensorInfoList(gateway.getId())));
            selectedGatewayTv.setText(String.format(Locale.ENGLISH,"Gateway %d",gateway.getId()));
            Drawable zAlive,gAlive;

            if(gateway.z_alive==0||gateway.z_alive==2)
                zAlive= getResources().getDrawable(R.drawable.dot_grey,null);
            else
                zAlive=getResources().getDrawable(R.drawable.dot_green,null);
            if(gateway.g_alive==0)
                gAlive=getResources().getDrawable(R.drawable.dot_grey,null);
            else
                gAlive=getResources().getDrawable(R.drawable.dot_green,null);
            selectedGatewayTv.setCompoundDrawablesWithIntrinsicBounds(zAlive,null,gAlive,null);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.lv_gateway:
                Log.d("GATEWAY","Item pos"+(i+1));
                gateway= (Gateway) gatewayLv.getItemAtPosition(i);
                selectedGatewayTv.setText(String.format(Locale.ENGLISH,"Gateway %d",gateway.getId()));
                Drawable zAlive,gAlive;

                if(gateway.z_alive==0||gateway.z_alive==2)
                    zAlive= getResources().getDrawable(R.drawable.dot_grey,null);
                else
                    zAlive=getResources().getDrawable(R.drawable.dot_green,null);
                if(gateway.g_alive==0)
                    gAlive=getResources().getDrawable(R.drawable.dot_grey,null);
                else
                    gAlive=getResources().getDrawable(R.drawable.dot_green,null);

                selectedGatewayTv.setCompoundDrawablesWithIntrinsicBounds(zAlive,null,gAlive,null);
                sensorLv.setAdapter(new SensorAdapter(MainActivity.this,0,sqliteHelper.getSensorInfoList(gateway.getId())));
                SharedPreferences.Editor preferences=getSharedPreferences("wisys",MODE_PRIVATE).edit();
                preferences.putInt(LAST_SELECTED_GID,gateway.getId());
                preferences.apply();
                collapse();
                break;
            case R.id.lv_sensor:
                SensorInfo sensorInfo= (SensorInfo) sensorLv.getItemAtPosition(i);
                Intent intent=new Intent(this,SensorDetailActivity.class);
                intent.putExtra(SensorDetailActivity.SENSOR_DETAIL,sensorInfo.getSid());
                startActivityForResult(intent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MAIN_ACTIVITY","Datachanged");
        if(resultCode==RESULT_OK && requestCode==2 ){
            Log.d("MAIN_ACTIVITY","Datachanged1");
            if(data.getBooleanExtra("SensorValue",true)){
                Log.d("MAIN_ACTIVITY","Datachanged=true");
                try{

                    sensorLv.setAdapter(new SensorAdapter(this,0,sqliteHelper.getSensorInfoList(gateway.getId())));
                }
                catch(NullPointerException e){
                    //
                }
            }
        }
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
                gatewayLv.setAdapter(new GatewayAdapter(MainActivity.this,0,sqliteHelper.getGateways()));
                gatewayListPb.setVisibility(View.GONE);
                refreshIv.setBackgroundResource(R.drawable.ic_refresh_black_24dp);

            }

            @Override
            public void onFailure(Call<List<Gateway>> call, Throwable t) {
                Log.d("API_RESPONSE"," Failed");
                Toast.makeText(MainActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
                gatewayListPb.setVisibility(View.GONE);
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
                sensorLv.setAdapter(new SensorAdapter(MainActivity.this,0,sqliteHelper.getSensorInfoList(gid)));
            }

            @Override
            public void onFailure(Call<List<SensorInfo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void collapse(){
        expandtitle.collapse();
        expandgatewaylist.collapse();
        selectedGatewayTv.setVisibility(View.VISIBLE);
        sensorLv.setVisibility(View.VISIBLE);
    }
    void expand(){
        selectedGatewayTv.setVisibility(View.INVISIBLE);
        sensorLv.setVisibility(View.GONE);
        expandtitle.expand();
        expandgatewaylist.expand();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(expandtitle.isExpanded()) {
            collapse();
        }
        else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_refresh:
                getSensorInfoList(gateway.getId());
                break;
            case R.id.menu_change_gateway:
                if(!expandtitle.isExpanded())
                    expand();
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_appinfo) {
            // Handle the camera action
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_settings) {
            Intent i= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_refresh:
                gatewayListPb.setVisibility(View.VISIBLE);
                refreshIv.setBackground(null);
                getGatewayList();
                break;
            case R.id.tv_selected_gateway:
                expand();
                break;
            case R.id.tv_select_gateway:
                collapse();
                break;
        }
    }


}
