package com.example.prashanth.wisys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class SqliteHelper extends SQLiteOpenHelper {

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE GATEWAY(id INTEGER PRIMARY KEY, gid TEXT,vid TEXT,node_cnt INTEGER,z_alive INTEGER,g_alive INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE NODE(nid INTEGER PRIMARY KEY,gid INTEGER,nw_addr INTEGER,ieee_addr TEXT,capab INTEGER,status INTEGER,SENSORS INTEGER,interval INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE SENSORINFO(sid INTEGER PRIMARY KEY,nid INTEGER,gid INTEGER,stype TEXT,epid INTEGER,value REAL)");

        sqLiteDatabase.execSQL("CREATE TABLE SENSOR(sid INTEGER PRIMARY KEY,gid INTEGER,nid INTEGER,epid INTEGER,stype TEXT,data INTEGER,timestamp TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertGateways(List<Gateway> gateways){
        SQLiteDatabase db=this.getWritableDatabase();
        if(db==null){
            Log.d("SQLITE","db is null");
            return;
        }
        db.delete("GATEWAY",null,null);
        int n=gateways.size();
        for(int i=0;i<n;i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",gateways.get(i).getId());
            contentValues.put("gid",gateways.get(i).getGid());
            contentValues.put("vid",gateways.get(i).getVid());
            contentValues.put("node_cnt",gateways.get(i).getNode_cnt());
            contentValues.put("z_alive",gateways.get(i).getZ_alive());
            contentValues.put("g_alive",gateways.get(i).getG_alive());
            db.insert("GATEWAY",null,contentValues);
        }
    }

    public void insertNodes(List<Node> nodes){
        SQLiteDatabase db=this.getWritableDatabase();
        int n=nodes.size();
        db.delete("NODE",null,null);
        for(int i=0;i<n;i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put("nid",nodes.get(i).getNid());
            contentValues.put("gid",nodes.get(i).getGid());
            contentValues.put("nw_addr",nodes.get(i).getNw_addr());
            contentValues.put("ieee_addr",nodes.get(i).getIeee_addr());
            contentValues.put("capab",nodes.get(i).getCapab());
            contentValues.put("status",nodes.get(i).getStatus());
            contentValues.put("SENSOR",nodes.get(i).getSensors());
            contentValues.put("interval",nodes.get(i).getInterval());
            db.insert("NODE",null,contentValues);

        }
    }

    public void insertSensorInfo(List<SensorInfo> sensorInfos){
        SQLiteDatabase db=this.getWritableDatabase();
//        CREATE TABLE SENSORINFO(sid INTEGER PRIMARY KEY,nid INTEGER,gid INTEGER,stype TEXT,epid INTEGER,value INTEGER
        int n=sensorInfos.size();
        db.delete("SENSORINFO",null,null);
        for(int i=0;i<n;i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put("nid",sensorInfos.get(i).getNid());
            contentValues.put("epid",sensorInfos.get(i).getEpid());
            contentValues.put("gid",sensorInfos.get(i).getGid());
            contentValues.put("stype",sensorInfos.get(i).getStype());
            contentValues.put("value",sensorInfos.get(i).getVal());
            contentValues.put("sid",sensorInfos.get(i).getSid());
            db.insert("SENSORINFO",null,contentValues);
        }
    }

    public void insertSensorStatus(List<SensorStatus> sensorStatuses){
        SQLiteDatabase db=this.getWritableDatabase();
//    CREATE TABLE SENSOR(sid INTEGER PRIMARY KEY,gid INTEGER,nid INTEGER,epid INTEGER,stype TEXT,data INTEGER,timestamp NUMERIC)
        int n=sensorStatuses.size();
        for(int i=0;i<n;i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put("sid",sensorStatuses.get(i).getSid());
            contentValues.put("gid",sensorStatuses.get(i).getGid());
            contentValues.put("nid",sensorStatuses.get(i).getNid());
            contentValues.put("epid",sensorStatuses.get(i).getEpid());
            contentValues.put("stype",sensorStatuses.get(i).getStype());
            contentValues.put("data",sensorStatuses.get(i).getData());
            contentValues.put("timestamp",sensorStatuses.get(i).getTimestamp());
            db.insert("SENSOR",null,contentValues);

        }

    }



    Gateway getGateway(int id){
        Gateway gateway=new Gateway();
        SQLiteDatabase db=this.getReadableDatabase();
        String query;
        if(id==0)
            query="SELECT * FROM GATEWAY";
        else
            query="SELECT * FROM GATEWAY WHERE ID="+id;
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            gateway.setId(c.getInt(c.getColumnIndex("id")));
            gateway.setVid(c.getString(c.getColumnIndex("vid")));
            gateway.setGid(c.getString(c.getColumnIndex("gid")));
            gateway.setNode_cnt(c.getInt(c.getColumnIndex("node_cnt")));
            gateway.setG_alive(c.getInt(c.getColumnIndex("g_alive")));
            gateway.setZ_alive(c.getInt(c.getColumnIndex("z_alive")));
        }
        else{
            query="SELECT * FROM GATEWAY";
            c=db.rawQuery(query,null);
            gateway.setId(c.getInt(c.getColumnIndex("id")));
            gateway.setVid(c.getString(c.getColumnIndex("vid")));
            gateway.setGid(c.getString(c.getColumnIndex("gid")));
            gateway.setNode_cnt(c.getInt(c.getColumnIndex("node_cnt")));
            gateway.setG_alive(c.getInt(c.getColumnIndex("g_alive")));
            gateway.setZ_alive(c.getInt(c.getColumnIndex("z_alive")));
        }
        c.close();
        return gateway;

    }
    List<Gateway> getGateways() {
        SQLiteDatabase db=this.getReadableDatabase();
        List<Gateway> gateways = new ArrayList<>();
        String query = "SELECT * FROM GATEWAY";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() != 0) {
            if (c.moveToFirst()) {
                do {
                    Gateway gateway = new Gateway();
                    gateway.setId(c.getInt(c.getColumnIndex("id")));
                    gateway.setVid(c.getString(c.getColumnIndex("vid")));
                    gateway.setGid(c.getString(c.getColumnIndex("gid")));
                    gateway.setNode_cnt(c.getInt(c.getColumnIndex("node_cnt")));
                    gateway.setG_alive(c.getInt(c.getColumnIndex("g_alive")));
                    gateway.setZ_alive(c.getInt(c.getColumnIndex("z_alive")));
                    gateways.add(gateway);
                } while (c.moveToNext());
            }
        }
        if(c!=null)
            c.close();
        return gateways;
    }

    List<SensorInfo> getSensorInfoList(int gatewayId) {
        SQLiteDatabase db=this.getReadableDatabase();
        List<SensorInfo> sensorInfos = new ArrayList<>();
        String query = "SELECT * FROM SENSORINFO WHERE gid="+gatewayId;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() != 0) {
            if (c.moveToFirst()) {
                do {
                    SensorInfo sensorInfo = new SensorInfo();
                    sensorInfo.setSid(c.getInt(c.getColumnIndex("sid")));
                    sensorInfo.setNid(c.getInt(c.getColumnIndex("nid")));
                    sensorInfo.setGid(c.getInt(c.getColumnIndex("gid")));
                    sensorInfo.setStype(c.getString(c.getColumnIndex("stype")));
                    sensorInfo.setEpid(c.getInt(c.getColumnIndex("epid")));
                    sensorInfo.setVal(c.getInt(c.getColumnIndex("value")));
                    sensorInfos.add(sensorInfo);
                } while (c.moveToNext());
            }
        }
        if(c!=null)
            c.close();
        return sensorInfos;
    }

    SensorInfo getSensorInfo(int sid){
        SQLiteDatabase db=this.getReadableDatabase();
        SensorInfo sensorInfo=new SensorInfo();
        String query="SELECT * FROM SENSORINFO WHERE sid="+sid;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() != 0) {
            if (c.moveToFirst()) {
                sensorInfo.setSid(c.getInt(c.getColumnIndex("sid")));
                sensorInfo.setNid(c.getInt(c.getColumnIndex("nid")));
                sensorInfo.setGid(c.getInt(c.getColumnIndex("gid")));
                sensorInfo.setStype(c.getString(c.getColumnIndex("stype")));
                sensorInfo.setEpid(c.getInt(c.getColumnIndex("epid")));
                sensorInfo.setVal(c.getInt(c.getColumnIndex("value")));
            }
        }
        if(c!=null)
            c.close();
        return sensorInfo;
    }
    void setSensorValue(int sid,int value){
        ContentValues sensorValue=new ContentValues();
        sensorValue.put("value",value);
        SQLiteDatabase db=this.getWritableDatabase();
        db.update("SENSORINFO",sensorValue,"sid=?",new String[]{String.valueOf(sid)});
    }
 }