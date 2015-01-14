package com.ryancat.greenhousegateway.controller;

import com.ryancat.greenhousegateway.GreenHouseApplication;
import com.ryancat.greenhousegateway.databean.LoginDataBean;
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
final class GatewayLoginTask extends BaseTask
{

	private static final int TASK_TAG = TaskConstants.GATEWAY_LOGIN;

	public GatewayLoginTask(Handler handler, Context _context)
	{
		super(handler, _context);
	}

	@Override
	public void run()
	{
			LoginDataBean loginDataBean = new LoginDataBean();
//			loginDataBean.gwid = GreenHouseApplication.gwid;
			loginDataBean = (LoginDataBean) httpManager.requestServer(Constants.Login_Url, loginDataBean, true);
			if (loginDataBean.status.equals(Constants.Status_Success))
			{
				L.d("login success!");
				L.d("loginDataBean is " +loginDataBean.toString());
				GreenHouseApplication.gwid = loginDataBean.gwid;
				GreenHouseApplication.gwToken = loginDataBean.gwToken;
				sendResultMessage(TASK_TAG, loginDataBean, TaskConstants.TASK_SUCCESS, 0);
			} else
			{
				L.e("登录接口出错 ---->" + loginDataBean.status);
				sendResultMessage(TASK_TAG, loginDataBean, TaskConstants.TASK_FAILED, 0);
			}
	}
}
