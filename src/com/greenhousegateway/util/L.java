package com.greenhousegateway.util;

import android.util.Log;

/**
 * 一个Log类，没别的什么
 * @author RyanHu
 *
 */
public class L
{

	private static boolean isOpenLog = false;
	private static String TAG = "GreenHouse";

	public static void v(String log)
	{
		if (isOpenLog)
		{
			Log.v(TAG, log);
		}
	}

	public static void d(String log)
	{
		if (isOpenLog)
		{
			Log.d(TAG, log);
		}
	}

	public static void i(String log)
	{
		if (isOpenLog)
		{
			Log.i(TAG, log);
		}
	}

	public static void w(String log)
	{
		if (isOpenLog)
		{
			Log.w(TAG, log);
		}
	}

	public static void e(String log)
	{
		if (isOpenLog)
		{
			Log.e(TAG, log);
		}
	}

	public static void isOpenLog(boolean isOpen)
	{
		isOpenLog = isOpen;
	}
}
