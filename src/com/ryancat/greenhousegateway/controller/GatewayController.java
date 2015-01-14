package com.ryancat.greenhousegateway.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.ryancat.greenhousegateway.util.L;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * 业务逻辑的控制类。 该业务函数负责提供界面所需数据函数的API
 * 
 * @author RyanHu
 */
public class GatewayController 
{

	private static GatewayController sInstance;// 单例
	private ExecutorService mExecutorService;//线程池
	private final static int THREAD_NUM = 1;//线程数
	private Context mContext;

	private GatewayController()
	{
		mExecutorService = Executors.newFixedThreadPool(THREAD_NUM);
	}
	public static GatewayController getInstance(Context _context)
	{
		if(sInstance == null)
		{
			sInstance = new GatewayController();
		}
		sInstance.mContext = _context;
		return sInstance;
	}
	/**
	 * 网关登录的外部API
	 */
	public void gatewayLogin(Handler taskHandler)
	{
		mExecutorService.execute(new GatewayLoginTask(taskHandler,mContext));
	}
	public void gatewayUpload(Handler taskHandler, int _temperature , int _humidity,int _beam )
	{
		mExecutorService.execute(new GatewayUploadTask(taskHandler,mContext, _temperature, _humidity, _beam));
	}
	
}
