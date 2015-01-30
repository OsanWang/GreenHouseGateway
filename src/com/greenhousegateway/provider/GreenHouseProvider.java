package com.greenhousegateway.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * ContentProvider的实现类，对数据库CRUD时应当使用Provider进行操作。
 * 本类中应当封装数据库CRUD的具体实现。
 * @author RyanHu
 *
 */
public class GreenHouseProvider extends ContentProvider
{

	@Override
	public boolean onCreate()
	{
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		return null;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		return 0;
	}

}
