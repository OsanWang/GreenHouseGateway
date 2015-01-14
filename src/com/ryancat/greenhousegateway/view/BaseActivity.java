package com.ryancat.greenhousegateway.view;

import com.ryancat.greenhouseclient.R;
import com.ryancat.greenhousegateway.GreenHouseApplication;
import com.ryancat.greenhousegateway.controller.GatewayController;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Activity的基类
 * 
 * @author RyanHu
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener
{
	/** 显示的view **/
	private ViewGroup mBackgroundView;
	private ViewGroup containerView;
	protected ViewGroup showView;
	protected GatewayController controller;
	protected GreenHouseApplication mApp;
	protected Handler taskHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		mBackgroundView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.container_activity, null);
		setContentView(mBackgroundView);
		containerView = (ViewGroup) mBackgroundView.findViewById(R.id.container_layout);
		initViews();
		init();
		if (showView.getLayoutParams() == null)
		{
			showView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		containerView.addView(showView);
	
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		setTaskHandler();
		progressLogic();

	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * 此函数需要给showView赋值
	 */
	protected abstract void initViews();

	private void init()
	{
		mApp = (GreenHouseApplication) getApplication();
		controller = mApp.getClientController();
	}
	
	/**
	 * 初始化逻辑部分的函数，放一些进入界面时就必须要实现的函数。
	 * 会在onStart的时候调用
	 */
	protected abstract void progressLogic();
	/**
	 * 设置执行task时的回调handler
	 * 需要实现一个handler给return回去
	 * 需要对taskHandler赋值
	 * @param h
	 * @return
	 */
	protected abstract void setTaskHandler();
	protected Handler getTaskHandler (){
		return taskHandler;
	}
}
