package com.greenhousegateway.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.view.MAChart;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.ReadHardwareDataTask;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.DetectorBean;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 探头数据网格activity。 本activity应当只有界面逻辑，不牵扯业务逻辑。
 * 
 * @author RyanHu
 * 
 */
public class ShowDetectorActivity extends BaseActivity
{
	/** -------------------- **/
	Dialog deleteDialog;

	private int detectorNum;
	private GridLayout mGridLayout;
	private Button showGwInfoBtn;
	private Button showQRCodeBtn;
	private Button showClientApkUrlBtn;
	AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.detector_activity, null);
		deleteDialog = new Dialog(ShowDetectorActivity.this);
		dialog = new AlertDialog.Builder(ShowDetectorActivity.this).create();
		showGwInfoBtn = (Button) showView.findViewById(R.id.show_device_info);
		showQRCodeBtn = (Button) showView.findViewById(R.id.show_gwid_qrcode_btn);
		showClientApkUrlBtn = (Button) showView.findViewById(R.id.show_client_apk_qrcode_btn);
		showClientApkUrlBtn.setOnClickListener(this);
		showQRCodeBtn.setOnClickListener(this);
		showGwInfoBtn.setOnClickListener(this);
		initDetectorsGridLayout();
	}

	/**
	 * 初始化gridLayout
	 */
	private void initDetectorsGridLayout()
	{
		mGridLayout = (GridLayout) showView.findViewById(R.id.detectors_gridlayout);
		if (mGridLayout.getChildCount() > 0)
		{
			mGridLayout.removeAllViews();
		}
		// 得到数量
		detectorNum = DataKeeper.dataKeeper_detectorList.size();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		for (int i = 0; i < detectorNum; i++)
		{
			final String dmac = DataKeeper.dataKeeper_detectorList.get(i).dmac;
			ViewGroup vg = (ViewGroup) getLayoutInflater().inflate(R.layout.detector_grid_layout_item, null);
			vg.setTag(dmac);
			TextView d_id_tv = (TextView) vg.findViewById(R.id.detector_id_tv2);
			TextView d_mac_tv = (TextView) vg.findViewById(R.id.detector_mac_tv2);
			d_id_tv.setText(DataKeeper.dataKeeper_detectorList.get(i).did + "");
			d_mac_tv.setText(dmac);
			vg.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(ShowDetectorActivity.this, ShowChartActivity.class);
					intent.putExtra("dmac", dmac);
					startActivity(intent);
				}
			});
			mGridLayout.addView(vg, new LayoutParams(wm.getDefaultDisplay().getWidth() / 2, 300));
		}
	}

	private void updateDetectorsGridLayout(HardwareDataBean _uploadDataBean)
	{
		// 根据mac找到需要更新的view
		L.d("正在更新探头框体: mac--->" + _uploadDataBean.dmac);
		ViewGroup itemView = (ViewGroup) mGridLayout.findViewWithTag(_uploadDataBean.dmac);
		if (itemView != null)
		{

			TextView d_temp_tv = (TextView) itemView.findViewById(R.id.detector_temp_tv2);
			TextView d_humi_tv = (TextView) itemView.findViewById(R.id.detector_humi_tv2);
			TextView d_beam_tv = (TextView) itemView.findViewById(R.id.detector_beam_tv2);
			TextView d_power_tv = (TextView) itemView.findViewById(R.id.detector_power_tv2);
			d_temp_tv.setText(_uploadDataBean.temperature + "");
			d_humi_tv.setText(_uploadDataBean.humidity + "");
			d_beam_tv.setText(_uploadDataBean.beam + "");
			d_power_tv.setText(_uploadDataBean.power + "");
		} else
		{
			L.e("待更新的探头框体不存在！");
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.title_menu_imgbtn:
		{
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("探头管理");
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, "添加探头", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					startActivityByName(ScannerActivity.class);
					dialog.dismiss();
				}
			});
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "删除探头", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					if (DataKeeper.dataKeeper_detectorList.size() > 0)
					{
						deleteDialog.setContentView(deleteDetectorDialog());
						deleteDialog.show();
					} else
					{
						Toast.makeText(mApp, "没有可删除的探头！", Toast.LENGTH_LONG).show();
					}
				}
			});
			dialog.show();
		}
			break;
		case R.id.show_client_apk_qrcode_btn:
		{
			if (GreenHouseApplication.apkPath != null)
			{
				Intent i = new Intent(this, QRCodeActivity.class);
				i.putExtra(Constants.QRCODE_TYPE, Constants.CLENT_APKPATH_QRCODE);
				startActivity(i);
			} else
			{
				Toast.makeText(this, "网关未登录服务器，请先登录~", Toast.LENGTH_LONG).show();
			}
		}
			break;
		case R.id.show_gwid_qrcode_btn:
		{
			if (GreenHouseApplication.gwid != 0)
			{
				Intent i = new Intent(this, QRCodeActivity.class);
				i.putExtra(Constants.QRCODE_TYPE, Constants.GWID_QRCODE);
				startActivity(i);
			} else
			{
				Toast.makeText(this, "网关未登录服务器，请先登录~", Toast.LENGTH_LONG).show();

			}
		}
			break;
		case R.id.show_device_info:
		{

			StringBuffer sb = new StringBuffer();
			sb.append("网关设备信息：\n");
			sb.append("IMEI：" + GreenHouseApplication.IMEI + "\n");
			sb.append("MAC：" + GreenHouseApplication.Mac + "\n");
			sb.append("网关ID：" + GreenHouseApplication.gwid + "\n");
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setMessage(sb);
			dialog.show();
		}
			break;
		default:
			break;
		}
	}

	@Override
	protected void progressLogic()
	{
		startService(new Intent(this, UploadDataService.class));
	}

	@Override
	protected void setTaskHandler()
	{
		taskHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case TaskConstants.ADD_DETECTOR_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						startActivityByName(GatewayLoginActivity.class);
						ShowDetectorActivity.this.finish();
					} else
					{
						Toast.makeText(mApp, "添加探头失败！", Toast.LENGTH_LONG).show();
					}
					break;
				case TaskConstants.DEL_GATEWAY_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						Toast.makeText(mApp, "删除探头成功！", Toast.LENGTH_LONG).show();
					} else
					{
						Toast.makeText(mApp, "删除探头失败！", Toast.LENGTH_LONG).show();
					}
				case TaskConstants.GATEWAY_READHARDWARE_TASK:
					if (msg.arg1 == TaskConstants.CREATE_SERIAL_FAILED || msg.arg1 == TaskConstants.OPEN_SERIAL_FAILED)
					{

						dialog.setTitle("初始化控制器");
						dialog.setMessage("初始化失败");
						dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "重试", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								synchronized (ReadHardwareDataTask.CreateSerialLock)
								{
									ReadHardwareDataTask.CreateSerialLock.notify();
								}
							}
						});
						if (!dialog.isShowing())
						{
							dialog.show();
						}

					}
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{// 数据到来刷新界面
						HardwareDataBean uploadDataBean = (HardwareDataBean) msg.obj;
						updateDetectorsGridLayout(uploadDataBean);
					}
					break;
				default:
					break;
				}
			}

		};
		handler = taskHandler;
	}

	public static Handler handler;

	public static Handler getHandler()
	{

		return handler;
	}

	/**
	 * 删除探头的dialog
	 * 
	 * @return
	 */
	public View deleteDetectorDialog()
	{
		ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.detectorlist_dialog, null);
		ListView t_ListView = (ListView) viewGroup.findViewById(R.id.detector_list);

		final BaseAdapter adapter = new BaseAdapter()
		{

			@Override
			public int getCount()
			{
				return DataKeeper.dataKeeper_detectorList.size();
			}

			@Override
			public Object getItem(int position)
			{
				return DataKeeper.dataKeeper_detectorList.get(position);
			}

			@Override
			public long getItemId(int position)
			{
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				ViewHolder holder;
				if (convertView == null)
				{
					holder = new ViewHolder();
					convertView = getLayoutInflater().inflate(R.layout.detectorlist_item, null);
					holder.tv = (TextView) convertView.findViewById(R.id.detector_item_tv);
				} else
				{
					holder = (ViewHolder) convertView.getTag();
				}
				holder.tv.setText(DataKeeper.dataKeeper_detectorList.get(position).dmac);
				convertView.setTag(holder);
				return convertView;
			}

			class ViewHolder
			{
				TextView tv;
			}
		};
		t_ListView.setAdapter(adapter);
		t_ListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				controller.delDetector(taskHandler, DataKeeper.dataKeeper_detectorList.get(arg2).dmac);
				if (deleteDialog.isShowing())
				{
					deleteDialog.dismiss();
				}
			}
		});
		return viewGroup;
	}
}
