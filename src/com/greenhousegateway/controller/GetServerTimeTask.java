package com.greenhousegateway.controller;

import java.util.Date;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.GetServerTimeBean;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

import android.content.Context;
import android.os.Handler;

public class GetServerTimeTask extends BaseTask
{
	private static final int TAG = TaskConstants.GET_SERVER_TIME_TASK;

	public GetServerTimeTask(Handler handler, Context _context)
	{
		super(handler, _context);
	}

	@Override
	public void run()
	{
		GetServerTimeBean getServerTimeBean = new GetServerTimeBean();
		getServerTimeBean.gwid = GreenHouseApplication.gwid;
		getServerTimeBean.gwToken = GreenHouseApplication.gwToken;
		getServerTimeBean = (GetServerTimeBean) httpManager.requestServer(Constants.ServerTime_Url, getServerTimeBean, true);
		if (getServerTimeBean == null)
		{
			sendResultMessage(TAG, Constants.REQUEST_SERVER_RETURN_NULL, TaskConstants.TASK_FAILED, 0);
			return;
		} else
		{
			if (getServerTimeBean.status.equals(Constants.Status_Success))
			{
				GreenHouseApplication.SERVER_TIME = getServerTimeBean.time.time - new Date().getTime();
				sendResultMessage(TAG, getServerTimeBean, TaskConstants.TASK_SUCCESS, 0);

			} else
			{
				sendResultMessage(TAG, getServerTimeBean.status, TaskConstants.TASK_FAILED, 0);
			}
		}
	}
}
