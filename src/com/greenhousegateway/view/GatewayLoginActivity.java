package com.greenhousegateway.view;

import java.util.ArrayList;
import java.util.List;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.GetDetectorListBean;
import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.util.L;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/***
 * 探头数据网格activity。 本activity应当只有界面逻辑，不牵扯业务逻辑。
 * 
 * @author RyanHu
 * 
 */
public class GatewayLoginActivity extends BaseActivity
{
	/** -------------------- **/
	/**
	 * 添加探头的按钮
	 */
	private Button mAddDetectorBtn;

	/**
	 * 无探头时的显示区域
	 */
	private ViewGroup mAddDetectorLayout;

	/**
	 * 提示对话框
	 */
	private AlertDialog mAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initViews()
	{
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.login_activity, null);
		mAddDetectorLayout = (ViewGroup) showView.findViewById(R.id.login_center_layout);
		mAddDetectorBtn = (Button) showView.findViewById(R.id.add_detector_btn);
		mAddDetectorBtn.setOnClickListener(this);
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setCancelable(false);
		mAlertDialog.setButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				controller.gatewayLogin(taskHandler);
				mAlertDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.add_detector_btn:
			// controller.gatewayAddDetector();
			// 跳转扫描二维码界面
			startActivityByName(ScannerActivity.class);
			GatewayLoginActivity.this.finish();

			break;
		}
	}

	@Override
	protected void progressLogic()
	{// 启动时，就去执行网络任务，开始登陆
		controller.gatewayLogin(taskHandler);
	}

	@Override
	protected void setTaskHandler()
	{
		taskHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				switch (msg.what)
				{
				case TaskConstants.GATEWAY_LOGIN_TASK:
					// 登陆部分
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{// 如果登陆成功，就去拿服务器时间
						controller.getServerTime(taskHandler);
					} else if (msg.arg1 == TaskConstants.TASK_FAILED)
					{// 如果登陆失败，就弹窗，让用户再次登陆
						mAlertDialog.setTitle("网关登陆");
						mAlertDialog.setMessage((String) msg.obj);
						mAlertDialog.show();
					}
					break;
				case TaskConstants.GET_SERVER_TIME_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						controller.getDetectorList(taskHandler);
					} else if (msg.arg1 == TaskConstants.TASK_FAILED)
					{
						mAlertDialog.setTitle("网关登陆");
						mAlertDialog.setMessage((String) msg.obj);
						mAlertDialog.show();
					}
					break;
				case TaskConstants.REQUEST_DETECTORLIST_TASK:
					// 获取探头列表
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{// 如果拿探头列表成功，就判断探头列表的内容
						GetDetectorListBean detectorListBean = (GetDetectorListBean) msg.obj;
						boolean detectorListEmpty = detectorListBean.detectorList.length == 0 ? true : false;
						if (detectorListEmpty)
						{// 如果探头列表为空，就给用户提示界面添加探头
							mAddDetectorLayout.setVisibility(View.VISIBLE);
						} else
						{// 如果探头列表不为空，就跳转探头列表Activity
							startActivityByName(ShowDetectorActivity.class);
							//初始化一下DataKeeper
							for (int i = 0; i < detectorListBean.detectorList.length; i++)
							{
								initDetectorsList(detectorListBean.detectorList[i].dmac);
							}
							GatewayLoginActivity.this.finish();
						}
					} else if (msg.arg1 == TaskConstants.TASK_FAILED)
					{// 如果拿探头列表失败，就弹窗，让用户再次登陆->拿探头列表
						mAlertDialog.setTitle("网关登陆");
						mAlertDialog.setMessage((String) msg.obj);
						mAlertDialog.show();
					}
					break;
				default:
					break;
				}
			}
		};
	}

	public void initDetectorsList(String mac)
	{
		List<UploadDataBean> t_list = DataKeeper.detectorDataMap.get(mac);
		if (t_list == null)
		{// 有数据才去new 一个LIST 确保 dataMap里面不会出现空值，如果没有接到数据，就不会走这一步
			t_list = new ArrayList<UploadDataBean>();
		}
		DataKeeper.detectorDataMap.put(mac, t_list);

	}
}
