package com.xinlan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpen extends SQLiteOpenHelper
{
	public SQLiteOpen(Context context, String name, CursorFactory factory,int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table PAPERS (ID INTEGER primary key autoincrement," +
				"PAPER_ID int,TITLE varchar(150),SUBJECT_NUM int,FLAG int,STATUS int,EXTERN varchar(150))");
		db.execSQL("create table ITEMS (ID INTEGER primary key autoincrement," +
				"ITEM_TYPE int,ITEM_NUMBER int,ITEM_TITLE varchar(300)," +
				"SECTIONA varchar(300),SECTIONB varchar(300),SECTIONC varchar(300),SECTIOND varchar(300)," +
				"ANSWER varchar(10),FLAG int,ANSWERDESC varchar(300),EXTERN varchar(150))");
		db.execSQL("create table LOADPAPERS (ID INTEGER primary key autoincrement," +
				"PAPER_ID int,TITLE varchar(150),SUBJECT_NUM int,ISLOAD int,FILEPATH varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
	}

}//end class
