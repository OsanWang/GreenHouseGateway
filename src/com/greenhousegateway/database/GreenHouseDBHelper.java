package com.greenhousegateway.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类，本类只提供数据库的创建和升级操作。
 * CRUD的操作应在Provider中进行。
 * @author RyanHu
 *
 */
public final class GreenHouseDBHelper extends SQLiteOpenHelper
{

	public GreenHouseDBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
}