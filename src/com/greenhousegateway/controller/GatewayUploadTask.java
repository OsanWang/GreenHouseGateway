package com.greenhousegateway.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.DetectorListDataBean;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.UploadBean;
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

	private static final int TASK_TAG = TaskConstants.GATEWAY_UPLOAD_TASK;
	private List<UploadDataBean> uploadDataBean;

	public GatewayUploadTask(Handler handler, Context _context, List<UploadDataBean> _uploadDataBeans)
	{
		super(handler, _context);
		uploadDataBean = _uploadDataBeans;
	}

	@Override
	public void run()
	{
		L.d("即将上传" + uploadDataBean.size() + "条数据");
		UploadBean uploadBean = new UploadBean();
		uploadBean.gwid = GreenHouseApplication.gwid;
		uploadBean.gwToken = GreenHouseApplication.gwToken;
		DetectorListDataBean dldb = new DetectorListDataBean();
		dldb.logTime = new Date().getTime() + GreenHouseApplication.SERVER_TIME;
		dldb.detectorDatas = new UploadDataBean[uploadDataBean.size()];
		int count = 0;
		Iterator<UploadDataBean> it = uploadDataBean.iterator();
		while (it.hasNext())
		{
			UploadDataBean uploadDataBean = it.next();
			dldb.detectorDatas[count] = uploadDataBean;
			count++;
		}
		String json = new Gson().toJson(dldb);
		uploadBean = (UploadBean) httpManager.requestServer(Constants.Upload_Url, uploadBean, true, json);
		if (uploadBean == null)
		{
			L.e("没有从服务器拿到数据");
		}
		if (uploadBean.status.equals(Constants.Status_Success))
		{
			sendResultMessage(TASK_TAG, "上传数据成功！", TaskConstants.TASK_SUCCESS, 0);
			L.d("上传成功");
		} else
		{
			sendResultMessage(TASK_TAG, "上传数据成功！", TaskConstants.TASK_FAILED, 0);
			L.d("上传失败");
		}
	}
}
