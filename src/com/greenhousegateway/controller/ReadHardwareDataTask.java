package com.greenhousegateway.controller;

import java.util.ArrayList;
import java.util.Random;

import com.greenhousegateway.util.Constants;

import android.content.Context;
import android.os.Handler;

/**
 * 从硬件读数据
 * @author RyanHu
 *
 */
public class ReadHardwareDataTask extends BaseTask
{
	private static final int TASK_TAG = TaskConstants.GATEWAY_READHARDWARE;

	public ReadHardwareDataTask(Handler handler, Context _context)
	{
		super(handler, _context);
	}

	@Override
	public void run()
	{
		int[] readHardwareData = readHardware();
		sendResultMessage(TASK_TAG, readHardwareData,TaskConstants.TASK_SUCCESS,0);
	}

	private int[] readHardware()
	{
		int temp = new Random().nextInt(Constants.TEMP_MAX);
		int humi= new Random().nextInt(Constants.HUMI_MAX);
		int beam = new Random().nextInt(Constants.BEAM_MAX);
		int[] readResult = new int[]{temp,humi,beam};
		return readResult;
	}
}
