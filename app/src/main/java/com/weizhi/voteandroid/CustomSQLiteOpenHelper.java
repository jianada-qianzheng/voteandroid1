package com.weizhi.voteandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;

import static android.content.ContentValues.TAG;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;



    private static final String DATABASE_NAME = "vote.db";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号
    private static final String CREATE_TABLE_CANDIDATE = "create table candidate ("
            + "id integer ,"
            + "name text, "
            + "vote double, "
            + "color text, "
            + "partner integer, "
            + "active integer)";//数据库里的表

    private static final String CREATE_TABLE_USER = "create table user ("
            + "id integer primary key,"
            + "guid text ,"

            + "name text)";//数据库里的表

    public CustomSQLiteOpenHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private CustomSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
        Log.d(TAG,"New CustomSQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        db.execSQL(CREATE_TABLE_CANDIDATE);
        db.execSQL(CREATE_TABLE_USER);
        String  userGUID = UUID.randomUUID().toString();
        Log.i("guid",userGUID+"");

        db.execSQL("insert into user values(1,'"+userGUID+"','匿名用戶');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public String getGUID(){
//
//        Cursor cursor=db.rawQuery("select * from user ",null);
//
//        String userGUID="";
//
//        if(cursor.moveToNext()){
//
//            userGUID=cursor.getString(1);
//
//        }
//
//        return userGUID;
//
//    }
//
//    public String getUsername(){
//
//        Cursor cursor=db.rawQuery("select * from user ",null);
//
//        String username="";
//
//        if(cursor.moveToNext()){
//
//            username=cursor.getString(2);
//
//        }
//
//        return username;
//
//    }
}
