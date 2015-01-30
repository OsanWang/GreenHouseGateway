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
	private ScheduledExecutorService mScheduledService;
	private TaskHandler mTaskHandler;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mScheduledService = Executors.newSingleThreadScheduledExecutor();
		controller =GatewayController.getInstance(this);
		mTaskHandler = new TaskHandler();
		//首先去拿网关id
		controller.gatewayLogin(mTaskHandler);
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
			controller.getTestDataFromHardware(mTaskHandler);
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
					mScheduledService.scheduleWithFixedDelay(new UploadDataScheduledRunnable(), 0, GreenHouseApplication.UploadTime, TimeUnit.MINUTES);
				}
				else{
					Toast.makeText(UploadDataService.this, "登录失败,请检查网关在后台有无注册！：", Toast.LENGTH_LONG).show();
				}
				break;

			case TaskConstants.GATEWAY_READHARDWARE:
				//读取硬件数据成功，准备上传
				int[] readResult = (int[]) msg.obj;
				int temp =readResult[0];
				int humi = readResult[1];
				int beam = readResult[2];
				Toast.makeText(UploadDataService.this, "硬件数据读取成功！温度："+temp+",湿度："+humi+",照度："+beam+"！准备上传！", Toast.LENGTH_LONG).show();
				//把数据放入DataKeeper
				UploadDataBean dataBean = new UploadDataBean();
				dataBean.temperature = temp;
				dataBean.humidity =humi;
				dataBean.beam  = beam;
				DataKeeper.dataKeeper_hour.add(dataBean);
				while(DataKeeper.dataKeeper_hour.size()>12)
				{
					DataKeeper.dataKeeper_hour.remove(0);
				}
				LoginActivity.getHandler().sendEmptyMessage(TaskConstants.GATEWAY_READHARDWARE);
				//上传数据
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
