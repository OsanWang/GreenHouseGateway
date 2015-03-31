package com.greenhousegateway.controller;

import java.util.Iterator;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.DetectorBean;
import com.greenhousegateway.databean.RegDetectorBean;
import com.greenhousegateway.util.Constants;

import android.content.Context;
import android.os.Handler;

/**
 * 
 * @author RyanHu
 * @Description:网关删除一个探头的task 
 * @date Mar 17, 2015 10:15:39 PM
 */
public class GatewayDelDetectorTask extends BaseTask
{
	private static final int TAG = TaskConstants.DEL_GATEWAY_TASK;
	private String dmac;


	public GatewayDelDetectorTask(Handler handler, Context _context,String _dmac)
	{
		super(handler, _context);
		dmac = _dmac;
	}

	@Override
	public void run()
	{
		RegDetectorBean detectorBean = new RegDetectorBean();
		detectorBean.gwid = GreenHouseApplication.gwid;
		detectorBean.gwToken = GreenHouseApplication.gwToken;
		detectorBean.dmac = dmac;
		detectorBean.action = 2;//删除的action是2
		detectorBean = (RegDetectorBean) httpManager.requestServer(Constants.RegDetector_Url, detectorBean, true);

		if (detectorBean == null)
		{
			sendResultMessage(TAG, Constants.REQUEST_SERVER_RETURN_NULL, TaskConstants.TASK_FAILED, 0);
			return;
		} else
		{
			if (detectorBean.status.equals(Constants.Status_Success))
			{
				Iterator<DetectorBean> it = DataKeeper.dataKeeper_detectorList.iterator();
				while (it.hasNext())
				{
					DetectorBean t_bean = it.next();
					if (t_bean.dmac .equals(dmac))
					{
						DataKeeper.dataKeeper_detectorList.remove(t_bean);
					}
				}
				sendResultMessage(TAG, detectorBean, TaskConstants.TASK_SUCCESS, 0);
			} else
			{
				sendResultMessage(TAG, detectorBean.status, TaskConstants.TASK_FAILED, 0);
			}
		}
	
		
	}

}
