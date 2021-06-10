package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context) {
        super(context,"Userdata.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, notes TEXT)");
        Log.i("Database","Database Created Successfully" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        Log.i("Database","Database Updated Successfully" );
        onCreate(db);
    }
    public Boolean InsertNote(String name,String txt)
    { String Total_notes="";

        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from user where name = ?",new String[] {name});
        Cursor cursor2=DB.rawQuery("select * from user",null);
        while (cursor2.moveToNext())
        {
            if((cursor2.getString(1).trim().equals(name)))
            {   if(cursor2.getString(3).trim().equals(""))
                    Total_notes=txt;
                else
                    Total_notes= cursor2.getString(3).trim() + "\n" + txt;
            }
        }
        ContentValues content = new ContentValues();
        content.put("notes",Total_notes);
        if(cursor.getCount()>0) {
            long res = DB.update("user", content,"name=?", new String[]{name});
            if (res == -1)
                return false;
            else return true;
        }else return false;
    }
    public Boolean UpdateNote(String name)
    {
        String Total_notes="";
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from user where name = ?",new String[] {name});
        ContentValues content = new ContentValues();
        content.put("notes",Total_notes);
        if(cursor.getCount()>0) {
            long res = DB.update("user", content,"name=?", new String[]{name});
            if (res == -1)
                return false;
            else return true;
        }else return false;
    }

    public Boolean InsertUser(String name, String password,String txt)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("name",name);
        content.put("password",password);
        content.put("notes",txt);
        long result =DB.insert("user",null,content);
        if(result==-1)
            return false;
            else
                return true;

    }
    public Cursor getData()
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from user",null);
        return cursor;
    }
    public String getNotes(String Name)
    {   String Total_Notes="";
        Cursor res= getData();
        while (res.moveToNext())
        {
            if((res.getString(1).trim().equals(Name)))
            {
                Total_Notes= res.getString(3);
                Log.i("Database",Total_Notes );

            }
        }
        return Total_Notes;
    }
    public Boolean deleteData(String name)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from user where name = ?",new String[] {name});
        if(cursor.getCount()>0) {
            long res = DB.delete("user", "name=?", new String[]{name});
            if (res == -1)
                return false;
            else return true;
        }else return false;
    }
}
