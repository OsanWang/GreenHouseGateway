package com.greenhousegateway.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.hardware.SerialPortUSBManager;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;
import com.greenhousegateway.util.Lw;

import android.content.Context;
import android.os.Handler;

/**
 * 从硬件读数据
 * 
 * @author RyanHu
 * 
 */
public class ReadHardwareDataTask extends BaseTask
{
	private static final int TASK_TAG = TaskConstants.GATEWAY_READHARDWARE_TASK;
	private SerialPortUSBManager usbReader;
	public static int SLEEP_TIME = 50;// 每两秒读取数据

	public static volatile Object CreateSerialLock = new Object();

	public ReadHardwareDataTask(Handler handler, Context _context)
	{
		super(handler, _context);
		try
		{
			usbReader = SerialPortUSBManager.getInstance(context);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		try
		{
			createSerial: while (true)
			{
				synchronized (CreateSerialLock)
				{
					boolean isCreate = usbReader.create();
					if (isCreate)
					{
						L.d("创建控制器成功");
						boolean isOpen = usbReader.openUsbSerial();
						if (isOpen)
						{
							L.d("打开控制器成功");
							break createSerial;
						} else
						{
							L.d("打开控制器失败");
							// 打开失败
							sendResultMessage(TASK_TAG, null, TaskConstants.OPEN_SERIAL_FAILED, 0);
							CreateSerialLock.wait();
						}

					} else
					{
						// 创建失败
						L.d("创建串口控制器失败");
						sendResultMessage(TASK_TAG, null, TaskConstants.CREATE_SERIAL_FAILED, 0);
						CreateSerialLock.wait();
					}
				}
				Thread.sleep(1500);
			}
			// 至此 成功打开设备
			Thread.sleep(10 * 1000);
			while (UploadDataService.isUploadWorking)
			{
				// L.d("读取数据线程运行中->" + new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
				// " , 读取时间间隔 = " + SLEEP_TIME + "毫秒");
				// Lw.WriteLog(context, "读取数据线程运行中->" + new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				try
				{
					// L.d("线程即将睡眠->" + new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
					// Date()));
					Lw.WriteLog(context, "线程即将睡眠->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					Thread.sleep(SLEEP_TIME);
					// L.d("睡眠结束->" + new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
					// Date()));
					// Lw.WriteLog(context, "睡眠结束->" + new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
					// Date()));
					List<String> result_list = usbReader.readDataFromSerial();
					if (result_list != null && result_list.size() > 0)
					{
						String result = result_list.get(0);
						Lw.WriteLog(context, "读取硬件数据成功，读取到->" + result + ",开始解析数据");
						L.d("读取硬件数据成功，读取到数据->" + result);
						String sub1[] = result.split("mac:");
						String sub2[] = sub1[1].split("temp:");
						String sub3[] = sub2[1].split("humi:");
						String sub4[] = sub3[1].split("light:");
						String sub5[] = sub4[1].split("power:");
						String mac = sub2[0].substring(0, sub2[0].length() - 1);
						String temp = sub3[0].substring(0, sub3[0].length() - 1);
						String humi = sub4[0].substring(0, sub4[0].length() - 1);
						String light = sub5[0].substring(0, sub5[0].length() - 1);
						String power = sub5[1];

						L.d("mac is ->" + mac + "~");
						L.d("temp is ->" + temp + "~");
						L.d("humi is ->" + humi + "~");
						L.d("light is ->" + light + "~");
						L.d("power is ->" + power + "~");
						Lw.WriteLog(context, "mac is ->" + mac + "~" + "temp is ->" + temp + "~" + "humi is ->" + humi + "~" + "light is ->" + light + "~" + "power is ->" + power);

						HardwareDataBean uploadDataBean = new HardwareDataBean();
						uploadDataBean.dmac = mac;
						uploadDataBean.temperature = Double.parseDouble(temp);
						uploadDataBean.humidity = Double.parseDouble(humi);
						uploadDataBean.beam = Double.parseDouble(light);
						uploadDataBean.power = Double.parseDouble(power);
						uploadDataBean.logTime = new Date().getTime() + GreenHouseApplication.SERVER_TIME;
						uploadDataBean.delivered = -1;
						sendResultMessage(TASK_TAG, uploadDataBean, TaskConstants.TASK_SUCCESS, 0);
					}
				} catch (Exception e)
				{
					// 非法数据s
					e.printStackTrace();
					L.d("从硬件读取数据的过程出现异常，请检查！");
					Lw.WriteLog(context, "从硬件读取数据的过程出现异常，请检查！异常类型为 -->" + e.getMessage());

				}
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			L.d("异常!初始化休眠被打断！");
			Lw.WriteLog(context, "异常!初始化休眠被打断！");
		}

	}
}
