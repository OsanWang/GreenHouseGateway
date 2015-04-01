package com.greenhousegateway.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.annotation.Coloumn;
import com.greenhousegateway.controller.GatewayController;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.database.GreenHouseDBHelper;
import com.greenhousegateway.databean.DetectorBean;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.GreenHouseUtils;
import com.greenhousegateway.util.L;
import com.greenhousegateway.view.ShowChartActivity;
import com.greenhousegateway.view.ShowDetectorActivity;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

/**
 * 
 * @author RyanHu
 * @Description: 网关Service
 * @date Mar 25, 2015 8:16:56 PM
 */
public class UploadDataService extends Service
{
	private GatewayController controller;
	private ExecutorService mSingleThreadExecutor;
	private ScheduledExecutorService mScheduledExecutorService;
	private static TaskHandler mTaskHandler;
	public static volatile boolean isUploadWorking;
	public static GreenHouseDBHelper dbHelper;

	@Override
	public void onCreate()
	{
		super.onCreate();
		dbHelper = new GreenHouseDBHelper(this, "detectorDataBase", null, 1);
		mSingleThreadExecutor = Executors.newSingleThreadExecutor();
		mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		controller = GatewayController.getInstance(this);
		mTaskHandler = new TaskHandler();
		// 首先去拿网关id
		GreenHouseUtils.acquireWakeLock(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		mSingleThreadExecutor.execute(new UploadDataScheduledRunnable());
		mScheduledExecutorService.scheduleAtFixedRate(new SaveDate2SqlAndUpload(),Constants.MINUTES,Constants.MINUTES, TimeUnit.MILLISECONDS);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	// 定时读取硬件数据
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

	private class SaveDate2SqlAndUpload implements Runnable
	{

		@Override
		public void run()
		{
			L.d("开始执行入库上传任务！！！！");
			// 先入库
			Set<String> macSet = DataKeeper.detectorDataMap.keySet();
			Iterator<String> iterator = macSet.iterator();
			while (iterator.hasNext())
			{
				try
				{
					String mac = iterator.next();
					L.d("正在入库！！！！mac--->" + mac);
					List<HardwareDataBean> beanList = DataKeeper.detectorDataMap.get(mac);
					HardwareDataBean dataBean = beanList.get(beanList.size() - 1);
					Iterator<DetectorBean> it2 = DataKeeper.dataKeeper_detectorList.iterator();
					while (it2.hasNext())
					{
						DetectorBean t_Bean = it2.next();
						if (t_Bean.dmac.equals(mac))
						{
							dataBean.did = t_Bean.did;
							break;
						}
					}
					L.d("即将入库的数据--->" + dataBean.toString());
					ContentValues cv = new ContentValues();
					cv.put("did", dataBean.did);
					cv.put("dmac", dataBean.dmac);
					cv.put("temperature", dataBean.temperature);
					cv.put("humidity", dataBean.humidity);
					cv.put("beam", dataBean.beam);
					cv.put("power", dataBean.power);
					cv.put("delivered", dataBean.delivered);
					cv.put("logTime", dataBean.logTime);
					dbHelper.getWritableDatabase().insert("detectors", "mac", cv);
					L.d("入库完毕！！！！");
				} catch (Exception e)
				{
					e.printStackTrace();
					L.d("入库出现问题！！！");
				}
			}
			
			L.d("开始删除过期数据！！！！");

			dbHelper.getWritableDatabase().delete("detectors", "logTime<" + ((new Date().getTime() + GreenHouseApplication.SERVER_TIME) - 3 * Constants.DAY), null);
			L.d("开始删除过期数据完成！！！！开始检索未发送信息！！！！");

			List<HardwareDataBean> datas = new ArrayList<HardwareDataBean>();
			Cursor cursor_nodelivered = dbHelper.getReadableDatabase().query("detectors", null, "delivered = -1", null, null, null, null);
			if (cursor_nodelivered != null)
			{
				// L.d("游标不为空！！！！");
				Class<HardwareDataBean> javabeanClass = HardwareDataBean.class;
				Field[] fields = javabeanClass.getDeclaredFields();
				while (cursor_nodelivered.moveToNext())
				{
					HardwareDataBean bean;
					try
					{
						bean = javabeanClass.newInstance();
						for (int i = 0; i < fields.length; ++i)
						{
							Coloumn coloumn = fields[i].getAnnotation(Coloumn.class);
							if (coloumn != null)
							{
								fields[i].setAccessible(true);
								if (!coloumn.name().equals(""))
									fields[i].set(bean, GreenHouseUtils.setValue(fields[i].getType(), cursor_nodelivered, cursor_nodelivered.getColumnIndexOrThrow(coloumn.name())));
								else
									fields[i].set(bean, GreenHouseUtils.setValue(fields[i].getType(), cursor_nodelivered, cursor_nodelivered.getColumnIndexOrThrow(fields[i].getName())));
							}
						}
						if (bean != null)
						{
							L.d("检索到数据---->" + bean.toString());
							datas.add(bean);
							// dataIdList.add(bean.did);
						}
					} catch (InstantiationException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}
			L.d("准备上传数据~~~~~~~~");
			controller.gatewayUpload(mTaskHandler, datas);
		}
	}

	class TaskHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case TaskConstants.GATEWAY_READHARDWARE_TASK:
				// 读取硬件数据成功，准备上传
				if (msg.arg1 == TaskConstants.CREATE_SERIAL_FAILED)
				{// 创建失败的处理，需要通知界面
					sendMessage(ShowDetectorActivity.getHandler(), TaskConstants.GATEWAY_READHARDWARE_TASK, null, TaskConstants.CREATE_SERIAL_FAILED, 0);
				} else if (msg.arg1 == TaskConstants.OPEN_SERIAL_FAILED)
				{
					sendMessage(ShowDetectorActivity.getHandler(), TaskConstants.GATEWAY_READHARDWARE_TASK, null, TaskConstants.OPEN_SERIAL_FAILED, 0);
				} else if (msg.arg1 == TaskConstants.TASK_SUCCESS)
				{
					// 把数据写入缓存
					HardwareDataBean bean = (HardwareDataBean) msg.obj;
					List<HardwareDataBean> t_list = DataKeeper.detectorDataMap.get(bean.dmac);
					L.d("正在把数据写入缓存---->dmac ->" + bean.dmac);
					if (t_list == null)
					{// 有数据才去new 一个LIST 确保 dataMap里面不会出现空值，如果没有接到数据，就不会走这一步
						t_list = new ArrayList<HardwareDataBean>();
					}
					t_list.add(bean);
					while (t_list.size() > 1000)
					{
						t_list.remove(0);
					}
					DataKeeper.detectorDataMap.put(bean.dmac, t_list);
					// 读到数据成功，通知界面刷新
					if (ShowDetectorActivity.getHandler() != null)
					{
						sendMessage(ShowDetectorActivity.getHandler(), TaskConstants.GATEWAY_READHARDWARE_TASK, msg.obj, TaskConstants.TASK_SUCCESS, 0);
					}
					if (ShowChartActivity.getHandler() != null)
					{
						sendMessage(ShowChartActivity.getHandler(), TaskConstants.GATEWAY_READHARDWARE_TASK, msg.obj, TaskConstants.TASK_SUCCESS, 0);
					}
				}
				break;
			case TaskConstants.GATEWAY_UPLOAD_TASK:
				if (msg.arg1 == TaskConstants.TASK_SUCCESS)
				{
					Toast.makeText(UploadDataService.this, "上传数据成功！", Toast.LENGTH_LONG).show();
					new Thread()
					{
						public void run()
						{
							L.d("上传服务器成功，更新数据库！");
							ContentValues contentValues = new ContentValues();
							contentValues.put("delivered", 0);
							UploadDataService.dbHelper.getReadableDatabase().update("detectors", contentValues, "delivered = -1", null);

						};
					}.start();
				} else
				{
					Toast.makeText(UploadDataService.this, "上传数据失败！", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}

		private void sendMessage(Handler _handler, int what, Object obj, int arg1, int arg2)
		{
			Message msg = Message.obtain();
			msg.obj = obj;
			msg.what = what;
			msg.arg1 = arg1;
			msg.arg2 = arg2;
			_handler.sendMessage(msg);
		}
	}

	public static Handler getHandler()
	{
		return mTaskHandler;
	}
}
