package com.software.abs.videotext;

/**
 * Created by sanny.nagveker on 06/02/2018.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyNotes.db";
    public static final String  TABLE_NAME = "notes";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "title";
    public static final String CONTACTS_COLUMN_TIME = "dateTimeMillis";
    public static final String CONTACTS_COLUMN_PATH = "path";
    public static final String CONTACTS_COLUMN_NOTE = "note";
    //public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table  " + TABLE_NAME+
                        "(id integer primary key, title text,dateTimeMillis text,path text, note text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public boolean insertNotes (VideoData mvidModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, mvidModel.getTitle());
        contentValues.put(CONTACTS_COLUMN_TIME, mvidModel.getDateTimeMillis());
        contentValues.put(CONTACTS_COLUMN_PATH, mvidModel.getPath());
        contentValues.put(CONTACTS_COLUMN_NOTE, mvidModel.getNote());
        //contentValues.put("place", mvidModel);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<VideoData>  getData(String name) {
        ArrayList<VideoData> array_list = new ArrayList<VideoData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes where title='/"+name+"'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            VideoData mVidData = new VideoData();
            mVidData.setNote(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NOTE)));
            mVidData.setDateTimeMillis(res.getLong(res.getColumnIndex(CONTACTS_COLUMN_TIME)));
            mVidData.setTitle(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            mVidData.setPath(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PATH)));
         //   mVidData.setNote(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NOTE)));

            array_list.add(mVidData);
            res.moveToNext();
        }

        return array_list;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}