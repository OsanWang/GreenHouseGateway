package com.greenhousegateway.controller;

import java.util.Date;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.network.HttpManager;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 网关上传数据的task
 * 
 * @author RyanHu
 * 
 */
final class GatewayUploadTask extends BaseTask
{

	private static final int TASK_TAG = TaskConstants.GATEWAY_UPLOAD;
	private UploadDataBean uploadDataBean;

	public GatewayUploadTask(Handler handler, Context _context ,UploadDataBean _uploadDataBean )
	{
		super(handler, _context);
		uploadDataBean = _uploadDataBean;

	}

	@Override
	public void run()
	{
		L.d("GatewayUploadTask run");
		uploadDataBean.gwid = GreenHouseApplication.gwid;
		uploadDataBean.gwToken = GreenHouseApplication.gwToken;
		uploadDataBean.logTime = new Date().getTime();
		uploadDataBean = (UploadDataBean)httpManager.requestServer(Constants.Upload_Url, uploadDataBean, true);
		if(uploadDataBean==null)
		{
			return ;
		}
		if(uploadDataBean.status.equals(Constants.Status_Success))
		{
			sendResultMessage(TASK_TAG, uploadDataBean,TaskConstants.TASK_SUCCESS,0);
		}
		else
		{
			sendResultMessage(TASK_TAG, uploadDataBean,TaskConstants.TASK_FAILED,0);
		}
	}
}
