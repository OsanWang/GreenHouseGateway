package com.greenhousegateway.controller;

import java.util.Map;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.RegDetectorBean;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.GreenHouseUtils;

import android.content.Context;
import android.os.Handler;

/**
 * @author RyanHu
 * @Description: 网关添加一个探头的TASK
 * @date Mar 17, 2015 10:09:21 PM
 */
public class GatewayAddDetectorTask extends BaseTask
{

	private static final int TAG = TaskConstants.ADD_DETECTOR_TASK;
	private String SerialNumber;

	public GatewayAddDetectorTask(Handler handler, Context _context, String _serialNumber)
	{
		super(handler, _context);
		SerialNumber = _serialNumber;
	}

	@Override
	public void run()
	{
		Map<String, String> parseSerial = GreenHouseUtils.parseSerialNumber(SerialNumber);
		String dmac = "";
		if (parseSerial != null)
		{
			dmac = parseSerial.get("dmac");
		}
		RegDetectorBean detectorBean = new RegDetectorBean();
		detectorBean.gwid = GreenHouseApplication.gwid;
		detectorBean.gwToken = GreenHouseApplication.gwToken;
		detectorBean.dmac = dmac;
		detectorBean.action = 1;// 添加探头的action是1
		detectorBean = (RegDetectorBean) httpManager.requestServer(Constants.RegDetector_Url, detectorBean, true);

		if (detectorBean == null)
		{
			sendResultMessage(TAG, Constants.REQUEST_SERVER_RETURN_NULL, TaskConstants.TASK_FAILED, 0);
			return;
		} else
		{
			if (detectorBean.status.equals(Constants.Status_Success))
			{
				sendResultMessage(TAG, detectorBean, TaskConstants.TASK_SUCCESS, 0);
			} else
			{
				sendResultMessage(TAG, detectorBean.status, TaskConstants.TASK_FAILED, 0);
			}
		}
	}

}
