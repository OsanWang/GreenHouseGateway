package com.greenhousegateway.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.L;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

/**
 * 业务逻辑的控制类。 该业务函数负责提供界面所需数据函数的API
 * 
 * @author RyanHu
 */
public class GatewayController 
{

	private static GatewayController sInstance;// 单例
	private ExecutorService mExecutorService;//线程池
	private final static int THREAD_NUM = 5;//线程数
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
	/**
	 * 网关上传数据的API
	 * @param taskHandler
	 * @param _temperature
	 * @param _humidity
	 * @param _beam
	 */
	public void gatewayUpload(Handler taskHandler,UploadDataBean uploadDataBean )
	{

		mExecutorService.execute(new GatewayUploadTask(taskHandler,mContext, uploadDataBean));
	}
	public void gatewayAddDetector()
	{
		
	}
	
	/**
	 * 从硬件读取数据
	 */
	public void getTestDataFromHardware(Handler taskHandler)
	{
		mExecutorService.execute(new ReadHardwareDataTask(taskHandler, mContext));
	}
}
