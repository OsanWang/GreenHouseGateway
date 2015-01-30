package com.greenhousegateway;

import com.greenhousegateway.controller.GatewayController;
import com.greenhousegateway.util.L;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;

/**
 * 继承了Application类，为整个应用唯一的application类。
 * 应用全局级别的filed和method应在此或者此类的拓展接口有所体现,比如SharedPreferences什么的。
 * 本类不应涉及界面或者业务。
 * @author RyanHu
 *
 */
public class GreenHouseApplication extends Application
{
	public static String IMEI ;
	public static String Mac;
	public static String Ua;
	public static String Ap;
	public static String Ver;
	public static String SysVer;
	public static int gwid  = 0;
	public static String gwToken;
	public static String apkPath ;
	public static int UploadTime=5;
	/**控制器**/
	private GatewayController mClientController;
	@Override
	public void onCreate()
	{
		super.onCreate();
		init();
	}
	
	/**
	 * 初始化函数，进入程序时调用。
	 */
	private void init ()
	{
		L.isOpenLog(true);
		mClientController = GatewayController.getInstance(this);
		initFiled();
	}
	/**
	 * 拿到控制器
	 * @return
	 */
	public GatewayController getClientController()
	{
		return mClientController;
	}
	/**
	 * 这里对Http的几个透传参数进行赋值
	 * 因为是在程序一开始的地方进行初始化，只进行一次赋值操作
	 */
	private void initFiled()
	{
		IMEI = getIMEI();
		Mac = getMac();
		Ua = android.os.Build.MODEL;
		SysVer=android.os.Build.VERSION.RELEASE;
		Ver = getAppVersionName(this);
		Ap ="wifi";
	}

	private String getMac()
	{

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress();

	}

	private String getIMEI()
	{
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		// 需要在manifest中加入 <uses-permission
		// android:name="android.permission.READ_PHONE_STATE"/>
		return imei;

	}
	/**
	 * 获取版本名称
	 */
	public  String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
