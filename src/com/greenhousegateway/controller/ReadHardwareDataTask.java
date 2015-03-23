package com.greenhousegateway.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
	private static final int TASK_TAG = TaskConstants.GATEWAY_READHARDWARE;
	private SerialPortUSBManager usbReader;
	public static int SLEEP_TIME = 2 * 1000;// 每两秒读取数据
	private static final int UPLOAD_TIME = 60 * 1000;// 60秒确认读取一次数据

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
			Thread.sleep(10 * 1000);
			int currentTime = 0;
			while (UploadDataService.isUploadWorking)
			{
				L.d("读取数据线程运行中->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" , 读取时间间隔 = "+SLEEP_TIME+"毫秒");
				Lw.WriteLog(context,"读取数据线程运行中->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				try
				{
					L.d("线程即将睡眠->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					Lw.WriteLog(context,"线程即将睡眠->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					Thread.sleep(SLEEP_TIME);
					L.d("睡眠结束->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					Lw.WriteLog(context,"睡眠结束->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					String result = usbReader.readDataFromSerial();
					L.d("读取硬件数据成功，读取到->" + result);
					
					Lw.WriteLog(context,"读取硬件数据成功，读取到->" + result+",开始解析数据");
//					String str[] = result.split("\t");
//					result = str[1].split("\n")[0];
					String sub1[] = result.split("mac:");
					String sub2[] = sub1[1].split("temp:");
					String sub3[] = sub2[1].split("humi:");
					String sub4[] = sub3[1].split("light:");
					String sub5[] = sub4[1].split("power:");
					String mac = sub2[0].substring(0, sub2[0].length() - 1);
					String temp = sub3[0].substring(0, sub3[0].length() - 1);
					String humi = sub4[0].substring(0, sub4[0].length() - 1);
					String light = sub5[0].substring(0, sub5[0].length()-1);
					String power = sub5[1].replace("\n", "");

					 L.d("mac is ->" + mac + "~");
					 L.d("temp is ->" + temp + "~");
					 L.d("humi is ->" + humi + "~");
					 L.d("light is ->" + light + "~");
					Lw.WriteLog(context,"mac is ->" + mac + "~"+"temp is ->" + temp + "~"+"humi is ->" + humi + "~"+"light is ->" + light + "~");

					double[] readHardwareData = new double[]
					{ Double.parseDouble(temp), Double.parseDouble(humi), Double.parseDouble(light) };
					if (currentTime >= UPLOAD_TIME)
					{
						currentTime = 0;
					}
					if (currentTime == 0)
					{
						sendResultMessage(TASK_TAG, readHardwareData, TaskConstants.TASK_SUCCESS, 0);
					}
					else
					{
						sendResultMessage(TASK_TAG, readHardwareData, TaskConstants.TASK_PROCESS, 0);
					}
					currentTime = currentTime + SLEEP_TIME;
				} catch (Exception e)
				{
					// 非法数据s
					L.d("从硬件读取数据的过程出现异常，请检查！");
					Lw.WriteLog(context,"从硬件读取数据的过程出现异常，请检查！异常类型为 -->" +e.getMessage());

				}
			}

		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
			L.d("异常!初始化休眠被打断！");
			Lw.WriteLog(context,"异常!初始化休眠被打断！");
		}

	}
	private String getDouble(int _)
	{
		String str  = new Random().nextInt(_)+".00";
		return str;
	}
}
