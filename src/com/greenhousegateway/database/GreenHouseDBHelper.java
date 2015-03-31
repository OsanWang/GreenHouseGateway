package com.greenhousegateway.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类，本类只提供数据库的创建和升级操作。 CRUD的操作应在Provider中进行。
 * 
 * @author RyanHu
 * 
 */
public final class GreenHouseDBHelper extends SQLiteOpenHelper
{
	public static final int DataBaseVersion = 1;//数据库版本
	public GreenHouseDBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, DataBaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String sql_create_table_detectordata = "create table IF NOT EXISTS  detectors (id integer primary key,did int,mac text,temperature real,humidity real,beam real ,logTime integer,delivered integer);";
		db.execSQL(sql_create_table_detectordata);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS detectors");
		onCreate(db);
	}
}