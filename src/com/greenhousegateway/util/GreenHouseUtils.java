package com.greenhousegateway.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.greenhousegateway.GreenHouseApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;

public class GreenHouseUtils
{
	/**
	 * 获取wifi mac地址
	 * 
	 * @param _context
	 * @return
	 */
	public static String getMac(Context _context)
	{

		WifiManager wifi = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress();

	}

	/**
	 * 获取手机IMEI
	 * 
	 * @param _context
	 * @return
	 */
	public static String getIMEI(Context _context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;

	}

	/**
	 * 获取版本名称
	 */
	public static String getAppVersionName(Context context)
	{
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 判断网络连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String getAccessPointName(Context _context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		String apName = "";
		if (info != null && info.isAvailable())
		{
			String name = info.getTypeName();
			apName = name;
		}
		return apName;
	}

	/**
	 * 保持唤醒
	 * 
	 * @param _context
	 */
	public static void acquireWakeLock(Context _context)
	{
		PowerManager pm = (PowerManager) _context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, _context.getClass().getCanonicalName());
		wl.acquire();
	}

	/**
	 * 获取计算后的服务器时间
	 * 
	 * @return
	 */
	public static long getServerTime()
	{
		return GreenHouseApplication.SERVER_TIME + new Date().getTime();
	}

	public static Object setValue(Class<?> clazz, Cursor cursor, int index)
	{
		if (clazz == String.class || clazz == Character.class || clazz == char.class)
			return cursor.getString(index);

		if (clazz == Integer.class || clazz == int.class || Boolean.class == clazz || boolean.class == clazz)
			return cursor.getInt(index);

		if (clazz == Double.class || clazz == double.class)
			return cursor.getDouble(index);

		if (clazz == Short.class || clazz == short.class)
			return cursor.getShort(index);

		if (clazz == Long.class || clazz == long.class)
			return cursor.getLong(index);

		if (clazz == Float.class || clazz == float.class)
			return cursor.getFloat(index);
		return null;

	}

	public static Map<String, String> parseSerialNumber(String SerialNumber)
	{
		if (SerialNumber.length() != 36)
		{// 序列号不符合定义
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		String MainType = SerialNumber.substring(0, 4);
		String SubType = SerialNumber.substring(4, 8);
		String factoryType = SerialNumber.substring(8, 10);
		String date = SerialNumber.substring(10, 16);
		String serial = SerialNumber.substring(16, 20);
		char[] mac = SerialNumber.substring(20, 36).toCharArray();
		// 再对mac进行一次转换
		StringBuffer sb_mac = new StringBuffer();
		for (int i = 0; i < mac.length; i++)
		{
			sb_mac.append(mac[i]);
			if (i % 2 == 1 && i > 0 && i < mac.length - 1)
			{
				sb_mac.append(":");
			}
		}

		result.put("MainType", MainType);
		result.put("SubType", SubType);
		result.put("factoryType", factoryType);
		result.put("date", date);
		result.put("serial", serial);
		result.put("dmac", sb_mac.toString());
		return result;

	}

}
