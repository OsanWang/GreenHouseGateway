package com.greenhousegateway.controller;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.network.HttpManager;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 用户登录的task
 * 
 * @author RyanHu
 * 
 */
final class GatewayLoginTask extends BaseTask
{
	private static final int TASK_TAG = TaskConstants.GATEWAY_LOGIN_TASK;

	public GatewayLoginTask(Handler handler, Context _context)
	{
		super(handler, _context);
	}

	@Override
	public void run()
	{
		LoginDataBean loginDataBean = new LoginDataBean();
		loginDataBean = (LoginDataBean) httpManager.requestServer(Constants.Login_Url, loginDataBean, true);
		if (loginDataBean == null)
		{
			sendResultMessage(TASK_TAG, Constants.REQUEST_SERVER_RETURN_NULL, TaskConstants.TASK_FAILED, 0);
			return;
		}
		if (loginDataBean.status.equals(Constants.Status_Success))
		{
			L.d("login success!");
			L.d("loginDataBean is " + loginDataBean.toString());
			GreenHouseApplication.gwid = loginDataBean.gwid;
			GreenHouseApplication.gwToken = loginDataBean.gwToken;
			GreenHouseApplication.apkPath = loginDataBean.clientApkUrl;
			sendResultMessage(TASK_TAG, loginDataBean, TaskConstants.TASK_SUCCESS, 0);
		} else
		{
			L.e("登录接口出错 ---->" + loginDataBean.status);
			sendResultMessage(TASK_TAG, "连接服务器出错！错误代码:" + loginDataBean.status, TaskConstants.TASK_FAILED, 0);
		}
	}
}
