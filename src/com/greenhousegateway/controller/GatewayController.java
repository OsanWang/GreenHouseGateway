package com.greenhousegateway.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.greenhousegateway.databean.HardwareDataBean;
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
		startTask(new GatewayLoginTask(taskHandler,mContext));
	}
	
	/**
	 * 读取网关带有的探头列表
	 * @param taskHandler
	 */
	public void getDetectorList(Handler taskHandler)
	{
		startTask(new RequestDetectorListTask(taskHandler, mContext));

	}
	
	/**
	 * 网关上传数据的API
	 * @param taskHandler
	 * @param _temperature
	 * @param _humidity
	 * @param _beam
	 */
	public void gatewayUpload(Handler taskHandler,List<HardwareDataBean> uploadDataBeans )
	{
		startTask(new GatewayUploadTask(taskHandler,mContext, uploadDataBeans));

	}
	
	/**
	 * 添加探头
	 * @param taskHandler
	 */
	public void addDetector(Handler taskHandler,String _dmac)
	{
		startTask(new GatewayAddDetectorTask(taskHandler, mContext,_dmac));
	}
	/**
	 * 删除探头
	 * @param taskHandler
	 */
	public void delDetector(Handler taskHandler, String _dmac)
	{
		startTask(new GatewayDelDetectorTask(taskHandler, mContext ,_dmac));
	}

	/**
	 * 获取服务器时间
	 * @param taskHandler
	 */
	public void getServerTime(Handler taskHandler)
	{
		startTask(new GetServerTimeTask(taskHandler,mContext));
	}
	/**
	 * 从硬件读取数据
	 */
	public void getTestDataFromHardware(Handler taskHandler)
	{
		startTask(new ReadHardwareDataTask(taskHandler, mContext));
	}
	
	/**
	 * 开始任务
	 * @param task
	 */
	private void startTask (BaseTask task)
	{
		L.d("start task --->"+task.getClass().getSimpleName());
		mExecutorService.execute(task);
	}
}
