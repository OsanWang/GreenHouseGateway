package com.ryancat.greenhousegateway.view;

import com.ryancat.greenhouseclient.R;
import com.ryancat.greenhouseclient.R.layout;
import com.ryancat.greenhousegateway.controller.TaskConstants;
import com.ryancat.greenhousegateway.databean.LoginDataBean;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/***
 * 显示界面的activity。
 * 本activity应当只有界面逻辑，不牵扯业务逻辑。
 * @author RyanHu
 * 
 */
public class LoginActivity extends BaseActivity 
{
	private Button register_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.login_activity, null);
		register_btn = (Button) showView.findViewById(R.id.login_register_btn);
		register_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.login_register_btn:
//			Intent intent = new Intent(this,ScannerActivity.class);
//			startActivity(intent);
			//点击上传数据
			controller.gatewayUpload(getTaskHandler(),20,30,40);
			break;
		default:
			break;
		}
	}

	@Override
	protected void progressLogic()
	{
		//首先去拿网关id
		controller.gatewayLogin(getTaskHandler());
	}

	@Override
	protected void  setTaskHandler()
	{
		taskHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case TaskConstants.GATEWAY_LOGIN:
					if(msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						LoginDataBean bean = (LoginDataBean) msg.obj;
						Toast.makeText(mApp, "登录成功："+bean.toString(), Toast.LENGTH_LONG).show();
					}
					break;

				default:
					break;
				}
			}
			
		};
	}
}
