package com.greenhousegateway.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.controller.GatewayController;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.GreenHouseUtils;
import com.greenhousegateway.util.L;
import com.greenhousegateway.view.DetectorActivity;
import com.greenhousegateway.view.LoginActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class UploadDataService extends Service
{
	private GatewayController controller;
	private ExecutorService mSingleThreadExecutor;
	private TaskHandler mTaskHandler;
	public static volatile boolean isUploadWorking;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mSingleThreadExecutor = Executors.newSingleThreadExecutor();
		controller =GatewayController.getInstance(this);
		mTaskHandler = new TaskHandler();
		//首先去拿网关id
		controller.gatewayLogin(mTaskHandler);
		GreenHouseUtils.acquireWakeLock(this);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	//定时读取硬件数据
	private class UploadDataScheduledRunnable implements Runnable
	{
		@Override
		public void run()
		{
			L.d("UploadDataScheduledRunnable~~~~~");
			controller.getTestDataFromHardware(mTaskHandler);
			isUploadWorking = true;
		}
	}
	private class TaskHandler extends Handler 
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case TaskConstants.GATEWAY_LOGIN:
				if(msg.arg1 == TaskConstants.TASK_SUCCESS)
				{
					if(LoginActivity.getHandler()!=null)
					{
						LoginActivity.getHandler().sendEmptyMessage(Constants.LOGIN_SUCCESS);
					}
					LoginDataBean bean = (LoginDataBean) msg.obj;
					Toast.makeText(UploadDataService.this, "登录成功："+bean.toString()+",准备开始上传数据", Toast.LENGTH_LONG).show();
					mSingleThreadExecutor.execute(new UploadDataScheduledRunnable());
				}
				else{
					Toast.makeText(UploadDataService.this, "登录失败,请检查网关在后台有无注册！：", Toast.LENGTH_LONG).show();
				}
				break;

			case TaskConstants.GATEWAY_READHARDWARE:
				//读取硬件数据成功，准备上传
				L.d("GATEWAY_READHARDWARE");

				double[] readResult = (double[]) msg.obj;
				double temp =readResult[0];
				double humi = readResult[1];
				double beam = readResult[2];
				//把数据放入DataKeeper
				UploadDataBean dataBean = new UploadDataBean();
				dataBean.temperature = temp;
				dataBean.humidity =humi;
				dataBean.beam  = beam;
				DataKeeper.dataKeeper_hour.add(dataBean);
				while(DataKeeper.dataKeeper_hour.size()>60)
				{
					DataKeeper.dataKeeper_hour.remove(0);
				}
				if(LoginActivity.getHandler()!=null)
				{
					LoginActivity.getHandler().sendEmptyMessage(TaskConstants.GATEWAY_READHARDWARE);
				}
				if(DetectorActivity.getHandler()!=null)
				{
					DetectorActivity.getHandler().sendEmptyMessage(TaskConstants.GATEWAY_READHARDWARE);
				}
				//上传数据
				Toast.makeText(UploadDataService.this, "硬件数据读取成功！温度："+temp+",湿度："+humi+",照度："+beam+"！准备上传！", Toast.LENGTH_LONG).show();
				controller.gatewayUpload(this, dataBean);
				break;
			case TaskConstants.GATEWAY_UPLOAD:
				if(msg.arg1 == TaskConstants.TASK_SUCCESS)
				{
					Toast.makeText(UploadDataService.this,"上传数据成功！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(UploadDataService.this,"上传数据失败！", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
		
	}
}
