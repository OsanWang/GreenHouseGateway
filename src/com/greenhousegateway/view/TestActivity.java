package com.greenhousegateway.view;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.GatewayController;
import com.greenhousegateway.controller.ReadHardwareDataTask;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.L;
import com.greenhousegateway.util.Lw;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends BaseActivity
{
	private Button mReadDataBtn1;
	private Button mReadDataBtn2;
	private Button mReadDataBtn3;
	private Button mClearLogBtn;
	private Button mReadLogBtn;
	private TextView log_tv;
	private GatewayController controller;

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.clear_log_btn)
		{
			Lw.ClearLog(mApp);
			log_tv.setText("暂无日志");
			return;
		}
		if (v.getId() == R.id.read_log_btn)
		{
			new Thread()
			{
				public void run()
				{
					final String log_str = Lw.ReadLog(mApp);
					runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							log_tv.setText(log_str);
						}
					});
				};
			}.start();
			return;
		}
		UploadDataService.isUploadWorking = UploadDataService.isUploadWorking ? false : true;
		String msg = UploadDataService.isUploadWorking ? "准备读取数据，请稍等十秒左右" : "停止读取数据";
		Toast.makeText(mApp, msg, 1).show();
		if (v.getId() == R.id.read_data_test_btn1)
		{
			Lw.WriteLog(mApp, "准备开始两秒一次读数据~！");
			ReadHardwareDataTask.SLEEP_TIME = 2 * 1000;// 每两秒读取数据
			controller.getTestDataFromHardware(taskHandler);
		} else if (v.getId() == R.id.read_data_test_btn2)
		{
			Lw.WriteLog(mApp, "准备开始十秒一次读数据~！");
			ReadHardwareDataTask.SLEEP_TIME = 10 * 1000;// 每10秒读取数据
			controller.getTestDataFromHardware(taskHandler);
		} else if (v.getId() == R.id.read_data_test_btn3)
		{
			Lw.WriteLog(mApp, "准备开始500毫秒一次读数据~！");
			ReadHardwareDataTask.SLEEP_TIME = 500;// 每10秒读取数据
			controller.getTestDataFromHardware(taskHandler);
		}
	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.test_activity, null);
		mReadDataBtn1 = (Button) showView.findViewById(R.id.read_data_test_btn1);
		mReadDataBtn2 = (Button) showView.findViewById(R.id.read_data_test_btn2);
		mReadDataBtn3 = (Button) showView.findViewById(R.id.read_data_test_btn3);
		mClearLogBtn = (Button) showView.findViewById(R.id.clear_log_btn);
		mReadLogBtn = (Button) showView.findViewById(R.id.read_log_btn);
		log_tv = (TextView) showView.findViewById(R.id.log_tv);
		mReadDataBtn1.setOnClickListener(this);
		mReadDataBtn2.setOnClickListener(this);
		mReadDataBtn3.setOnClickListener(this);
		mReadLogBtn.setOnClickListener(this);
		mClearLogBtn.setOnClickListener(this);
	}

	@Override
	protected void progressLogic()
	{
		controller = GatewayController.getInstance(mApp);
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
				case TaskConstants.GATEWAY_READHARDWARE_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{

						// 读取硬件数据成功，准备上传
						L.d("GATEWAY_READHARDWARE");

						double[] readResult = (double[]) msg.obj;
						double temp = readResult[0];
						double humi = readResult[1];
						double beam = readResult[2];
						// 把数据放入DataKeeper
						HardwareDataBean dataBean = new HardwareDataBean();
						dataBean.temperature = temp;
						dataBean.humidity = humi;
						dataBean.beam = beam;
//						DataKeeper.dataKeeper_hour.add(dataBean);
//						while (DataKeeper.dataKeeper_hour.size() > 60)
//						{
//							DataKeeper.dataKeeper_hour.remove(0);
//						}
						if (ShowDetectorActivity.getHandler() != null)
						{
							ShowDetectorActivity.getHandler().sendEmptyMessage(TaskConstants.GATEWAY_READHARDWARE_TASK);
						}
						if (ShowChartActivity.getHandler() != null)
						{
							ShowChartActivity.getHandler().sendEmptyMessage(TaskConstants.GATEWAY_READHARDWARE_TASK);
						}
						// 上传数据
						Toast.makeText(TestActivity.this, "硬件数据读取成功！温度：" + temp + ",湿度：" + humi + ",照度：" + beam + "!", Toast.LENGTH_LONG).show();
						// controller.gatewayUpload(this, dataBean);
					} else if (msg.arg1 == TaskConstants.TASK_PROCESS)
					{
						double[] readResult = (double[]) msg.obj;
						double temp = readResult[0];
						double humi = readResult[1];
						double beam = readResult[2];
						// 把数据放入DataKeeper
						HardwareDataBean dataBean = new HardwareDataBean();
						dataBean.temperature = temp;
						dataBean.humidity = humi;
						dataBean.beam = beam;
						Toast.makeText(TestActivity.this, "硬件数据读取成功！温度：" + temp + ",湿度：" + humi + ",照度：" + beam + "!", Toast.LENGTH_LONG).show();
					}
					break;
				default:
					break;
				}
			}

		};
	}

}
