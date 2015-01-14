package com.ryancat.greenhousegateway.controller;

import com.ryancat.greenhousegateway.network.HttpManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 业务Task的基类。 该基类及子类都应是default的访问级别,并且子类应该是final不可继承的。外面的包不可访问，
 * 上层通过ClientController调用各个task，保证与上层UI不耦合。
 * 
 * @author RyanHu
 * 
 */
abstract class BaseTask implements Runnable
{
	protected Handler mTaskHandler;
	protected Context context;
	protected HttpManager httpManager;
	public BaseTask (Handler handler,Context _context)
	{
		mTaskHandler = handler;
		context = _context;
		httpManager = HttpManager.getInstance();
	}
	
	/**
	 * 通知上层界面，任务执行的结果
	 * @param what
	 * @param obj
	 * @param arg1
	 * @param arg2
	 */
	public void sendResultMessage(int what,Object obj)
	{
		sendResultMessage(what,obj,0,0);
	}
	public void sendResultMessage(int what, Object obj,int arg1 ,int arg2)
	{
		if(mTaskHandler!=null)
		{
			Message msg = Message.obtain();
			msg.what = what;
			msg.obj = obj;
			msg.arg1 = arg1;
			msg.arg2 = arg2;
			mTaskHandler.sendMessage(msg);
		}
	}
}
