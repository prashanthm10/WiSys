package com.example.prashanth.wisys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prashanth.wisys.rest.WiSysApiService;
import com.xw.repo.BubbleSeekBar;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SensorDetailActivity extends AppCompatActivity {

    ImageView alert_img;
    BubbleSeekBar seekBar;
    SwitchButton sensor_switch;
    final static String SENSOR_DETAIL="sensor_detail";
    SqliteHelper sqliteHelper;
    SensorInfo sensorInfo;
    boolean datachanged=false;
    Button saveBtn;
    CmdStatusHandler cmdStatusHandler;
    final static int EMPTY_RESPONSE=0,STATUS_NEW=1,STATUS_SUCCESS=2,STATUS_FAIL=3,QUERY_STATUS=4,ERROR=5;

    final String BASE_URL="http://139.59.4.45:8500/api/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail_2);
        alert_img=(ImageView)findViewById(R.id.alert_img);
        seekBar=(BubbleSeekBar) findViewById(R.id.seekBar);
        sensor_switch=(SwitchButton) findViewById(R.id.sensor_switch);


        sqliteHelper=new SqliteHelper(this,"Wisys",null,1);
        sensor_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked && seekBar.getProgress()==0){
                    seekBar.setProgress(50);
                }else if(!isChecked && seekBar.getProgress()>0){
                    seekBar.setProgress(0);
                }
            }
        });


        saveBtn=(Button)findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdStatusHandler=new CmdStatusHandler();
                saveToServer();
            }
        });
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                switch (progress) {
                    case 0:
                        alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_grey));
                        break;
                    case 100:
                        alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_green));
                        break;
                    case 101:
                        alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_red));
                        break;
                    default:
                        alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_yellow));
                }

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                datachanged=true;
                if (progress > 0 && !sensor_switch.isChecked()) sensor_switch.setChecked(true);
                else if (progress == 0 && sensor_switch.isChecked()) sensor_switch.setChecked(false);

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        initializeTheCurrentState(getIntent().getIntExtra(SENSOR_DETAIL,0));
    }

    @Override
    public void onBackPressed() {
        if(datachanged){
            Intent output=new Intent();
            output.putExtra("SensorValue",true);
            setResult(RESULT_OK,output);
            finish();
        }
        else{
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }

    }

    private void thermometer(){
        Thermometer thermometer=(Thermometer)findViewById(R.id.thermometer);
        thermometer.setVisibility(View.VISIBLE);
        thermometer.setCurrentTemp(50-sensorInfo.getVal());
        if(sensorInfo.getVal()>0)
            seekBar.setProgress(sensorInfo.getVal());
        seekBar.setEnabled(false);
        sensor_switch.setEnabled(false);
        saveBtn.setEnabled(false);
        saveBtn.setAlpha((float)0.3);
        seekBar.setAlpha((float)0.3);
        sensor_switch.setAlpha((float)0.3);


    }
    private void battery(){
        CardView cardView=(CardView)findViewById(R.id.card_img_container);
        cardView.setBackgroundColor(Color.argb(255,29,202,244));
        BatteryProgressView batteryProgressView=(BatteryProgressView)findViewById(R.id.cir_pb_battery);
        batteryProgressView.setVisibility(View.VISIBLE);
        batteryProgressView.setProgress(sensorInfo.getVal());
        seekBar.setEnabled(false);
        sensor_switch.setEnabled(false);
        saveBtn.setEnabled(false);
        saveBtn.setAlpha((float)0.3);
        seekBar.setAlpha((float)0.3);
        sensor_switch.setAlpha((float)0.3);
    }
    private void bulb(){
        alert_img.setVisibility(View.VISIBLE);
        if(sensorInfo.getVal()!=0)
            sensor_switch.setChecked(true);
        switch (sensorInfo.getVal()) {
            case 0:
                alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_grey));
                break;
            case 100:
                alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_green));
                break;
            case 101:
                alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_red));
                break;
            default:
                alert_img.setBackground(getResources().getDrawable(R.drawable.ic_bulb_filled_yellow));
        }

    }
    private void humidity(){
        ImageView humidity_img=(ImageView)findViewById(R.id.imageView);
        humidity_img.setImageResource(R.drawable.ic_water_drop);
        humidity_img.setVisibility(View.VISIBLE);
        TextView textView=(TextView)findViewById(R.id.tv_message);
        sensor_switch.setVisibility(View.GONE);
        String message;
        int i=sensorInfo.getVal();
        if(i>=0 && i<=20){
            message="UNCOMFORTABLY DRY";
        }
        else if(i>20 && i<=60){
            message="UNCOMFORTABLY DRY";
        }
        else{
            message="UNCOMFORTABLY WET";
        }
        textView.setText(message);
        seekBar.setEnabled(false);
        sensor_switch.setEnabled(false);
        saveBtn.setEnabled(false);
        saveBtn.setAlpha((float)0.3);
        seekBar.setAlpha((float)0.3);
        sensor_switch.setAlpha((float)0.3);
    }



    private void pds(){
        ImageView pds_img=(ImageView)findViewById(R.id.imageView);
        pds_img.setVisibility(View.VISIBLE);
        switch(sensorInfo.getVal()){
            case 0:
                pds_img.setImageResource(R.drawable.ic_people_outline_black_24dp);
                break;
            case 1:
                pds_img.setImageResource(R.drawable.ic_people_black_24dp);
                break;
            default:
                pds_img.setImageResource(R.drawable.ic_people_black_24dp);

        }
        seekBar.setEnabled(false);
        sensor_switch.setEnabled(false);
        saveBtn.setEnabled(false);
        saveBtn.setAlpha((float)0.3);
        seekBar.setAlpha((float)0.3);
        sensor_switch.setAlpha((float)0.3);
    }
    private void initializeTheCurrentState(int sid) {

        sensorInfo=sqliteHelper.getSensorInfo(sid);
        TextView textview=(TextView)findViewById(R.id.tv_nid);
        textview.setText(String.format(Locale.ENGLISH,"SID %d",sensorInfo.getSid()));
        textview=(TextView)findViewById(R.id.tv_sid);
        textview.setText(String.format(Locale.ENGLISH,"NID %d",sensorInfo.getNid()));
        textview=(TextView)findViewById(R.id.tv_epid);
        textview.setText(String.format(Locale.ENGLISH,"EPID %d",sensorInfo.getEpid()));

        switch(sensorInfo.getStype()){
            case "SENSOR_PWM":
                bulb();
                break;
            case "SENSOR_TEMPERATURE":
                thermometer();
                break;
            case "SENSOR_BATTERY":
                battery();
                break;
            case "SENSOR_HUMIDITY":
                humidity();
                break;
            case "SENSOR_PDS":
                pds();
                break;
            default:
                Toast.makeText(this, "UnKnown Sensor", Toast.LENGTH_SHORT).show();
        }
        seekBar.setProgress(sensorInfo.getVal());

    }

    void saveToServer(){
        final int progress=seekBar.getProgress();
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        WiSysApiService wiSysApiService=retrofit.create(WiSysApiService.class);
        Call<CommandResponse> call=wiSysApiService.setValue(sensorInfo.getGid(),sensorInfo.getSid(),progress);
        call.enqueue(new Callback<CommandResponse>() {
            @Override
            public void onResponse(Call<CommandResponse> call, Response<CommandResponse> response) {
                if(response.body()!=null){
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("cmd_id",response.body().cmd_id);
                    bundle.putInt("val",progress);
                    message.setData(bundle);
                    cmdStatusHandler.sendMessage(message);
                    cmdStatusHandler.sendEmptyMessage(STATUS_NEW);
                }
            }

            @Override
            public void onFailure(Call<CommandResponse> call, Throwable t) {
                cmdStatusHandler.sendEmptyMessage(ERROR);
            }
        });
    }


    void checkStatus(int cmd_id){
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        WiSysApiService wiSysApiService=retrofit.create(WiSysApiService.class);
        Call<CommandStatusResponse> call1=wiSysApiService.getTheStatus(sensorInfo.getGid(),cmd_id);
        call1.enqueue(new Callback<CommandStatusResponse>() {
            @Override
            public void onResponse(Call<CommandStatusResponse> call, Response<CommandStatusResponse> response) {
                if(response.body()!=null){
                    switch (response.body().getStatus()){
                        case "NEW":
                            cmdStatusHandler.sendEmptyMessage(STATUS_NEW);
                            break;
                        case "SUCCESS":
                            cmdStatusHandler.sendEmptyMessage(STATUS_SUCCESS);
                            break;
                        case "FAIL":
                            cmdStatusHandler.sendEmptyMessage(STATUS_FAIL);
                            break;
                        default:
                            cmdStatusHandler.sendEmptyMessage(100);
                    }

                }

            }

            @Override
            public void onFailure(Call<CommandStatusResponse> call, Throwable t) {
                cmdStatusHandler.sendEmptyMessage(ERROR);
            }
        });
    }

    private class CmdStatusHandler extends Handler{
        int cmd_id,val;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message;
            if(!msg.getData().isEmpty()){

                cmd_id=msg.getData().getInt("cmd_id");
                val=msg.getData().getInt("val");

            }

            switch (msg.what){
                case EMPTY_RESPONSE:
                    message="No Response";
                    break;
                case STATUS_NEW:
                    message="NEW";
                    break;
                case STATUS_SUCCESS:
                    sqliteHelper.setSensorValue(sensorInfo.getSid(),val);
                    message="SUCCESS";
                    break;
                case STATUS_FAIL:
                    message="FAIL";
                    break;
                case QUERY_STATUS:
                    checkStatus(cmd_id);
                default:
                    message="UnKnown Response";
            }


            Toast.makeText(SensorDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            if(Objects.equals(message, "NEW")){
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkStatus(cmd_id);
                    }
                }, 5000);
                Toast.makeText(SensorDetailActivity.this, "Checking again", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
