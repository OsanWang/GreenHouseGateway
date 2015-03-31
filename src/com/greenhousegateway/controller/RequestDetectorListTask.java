package com.greenhousegateway.controller;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.GetDetectorListBean;
import com.greenhousegateway.util.Constants;

import android.content.Context;
import android.os.Handler;

/**
 * 
 * @author RyanHu
 * @Description: 请求网关所带有的探头列表
 * @date Mar 26, 2015 5:10:26 PM
 */
public class RequestDetectorListTask extends BaseTask
{

	private static final int TAG = TaskConstants.REQUEST_DETECTORLIST_TASK;

	public RequestDetectorListTask(Handler handler, Context _context)
	{
		super(handler, _context);
	}

	@Override
	public void run()
	{
		GetDetectorListBean t_gdlb = new GetDetectorListBean();
		t_gdlb.gwid = GreenHouseApplication.gwid;
		t_gdlb.gwToken = GreenHouseApplication.gwToken;
		t_gdlb = (GetDetectorListBean) httpManager.requestServer(Constants.DetectorList_Url, t_gdlb, true);
		if (t_gdlb == null)
		{
			sendResultMessage(TAG, Constants.REQUEST_SERVER_RETURN_NULL, TaskConstants.TASK_FAILED, 0);
			return;
		} else
		{
			if(t_gdlb.status .equals(Constants.Status_Success))
			{
				DataKeeper.dataKeeper_detectorList.clear();
				//下发的探头列表缓存起来
				for(int i = 0; i<t_gdlb.detectorList.length ;i++)
				{
					DataKeeper.dataKeeper_detectorList.add(t_gdlb.detectorList[i]);
				}
				sendResultMessage(TAG, t_gdlb, TaskConstants.TASK_SUCCESS, 0);
			}
			else
			{
				sendResultMessage(TAG, t_gdlb.status, TaskConstants.TASK_FAILED, 0);
			}
		}
	}

}
