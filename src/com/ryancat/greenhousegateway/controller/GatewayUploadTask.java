package com.ryancat.greenhousegateway.controller;

import com.ryancat.greenhousegateway.GreenHouseApplication;
import com.ryancat.greenhousegateway.databean.LoginDataBean;
import com.ryancat.greenhousegateway.databean.UploadDataBean;
import com.ryancat.greenhousegateway.network.HttpManager;
import com.ryancat.greenhousegateway.util.Constants;
import com.ryancat.greenhousegateway.util.L;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 用户登录的task
 * 
 * @author RyanHu
 * 
 */
final class GatewayUploadTask extends BaseTask
{

	private static final int TASK_TAG = TaskConstants.GATEWAY_UPLOAD;
	private int temperature;
	private int humidity;
	private int beam;

	public GatewayUploadTask(Handler handler, Context _context , int _temperature , int _humidity,int _beam )
	{
		super(handler, _context);
		this.temperature = _temperature;
		this.humidity = _humidity;
		this.beam = _beam;
	}

	@Override
	public void run()
	{
		UploadDataBean uploadDataBean = new UploadDataBean();
		uploadDataBean.gwid = GreenHouseApplication.gwid;
		uploadDataBean.gwToken = GreenHouseApplication.gwToken;
		uploadDataBean.temperature =temperature;
		uploadDataBean.humidity =humidity;
		uploadDataBean.beam =beam;
		
		uploadDataBean = (UploadDataBean)httpManager.requestServer(Constants.Upload_Url, uploadDataBean, true);
		if(uploadDataBean.equals(Constants.Status_Success))
		{
			sendResultMessage(TASK_TAG, uploadDataBean,TaskConstants.TASK_SUCCESS,0);
		}
		else
		{
			sendResultMessage(TASK_TAG, uploadDataBean,TaskConstants.TASK_FAILED,0);
		}
	}
}
