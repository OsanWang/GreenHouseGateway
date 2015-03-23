package com.greenhousegateway.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
	public  static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 判断网络连接状态
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
		ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		String apName = "";
		if (info != null && info.isAvailable())
		{
			String name = info.getTypeName();
			apName =name;
		} 
		return apName;
	}
	/**
	 * 保持唤醒
	 * @param _context
	 */
	public static void acquireWakeLock(Context _context)
	{
		PowerManager pm = (PowerManager)_context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, _context.getClass().getCanonicalName());
		wl.acquire();
	}
}
